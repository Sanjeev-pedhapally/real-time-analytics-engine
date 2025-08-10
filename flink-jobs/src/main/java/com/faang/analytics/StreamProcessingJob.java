package com.faang.analytics;

import com.faang.analytics.model.Purchase;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.cassandra.CassandraSink;
import org.apache.flink.streaming.api.windowing.time.Time;
import redis.clients.jedis.Jedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class StreamProcessingJob {
    private static final Logger logger = LoggerFactory.getLogger(StreamProcessingJob.class);

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(10000); // 10s
        env.getCheckpointConfig().setCheckpointingMode(org.apache.flink.api.common.state.CheckpointingMode.EXACTLY_ONCE);
        env.setParallelism(8);

        Properties kafkaProps = new Properties();
        kafkaProps.setProperty("bootstrap.servers", "kafka:9092");
        kafkaProps.setProperty("group.id", "flink-analytics-group");

        FlinkKafkaConsumer<Object> consumer = new FlinkKafkaConsumer<>(
                "ecommerce-events",
                new EventDeserializer(),
                kafkaProps
        );
        consumer.assignTimestampsAndWatermarks(WatermarkStrategy.forMonotonousTimestamps());

        DataStream<Purchase> purchases = env
                .addSource(consumer)
                .filter(e -> e instanceof Purchase)
                .map(e -> (Purchase) e);

        // 1min, 5min, 1hr window aggregations
        purchases
            .keyBy(Purchase::getProductId)
            .window(org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows.of(Time.minutes(1)))
            .aggregate(new WindowAggregator(), new EventProcessor())
            .addSink(new CassandraSink<String>() {
                @Override
                public void invoke(String value, Context context) {
                    // Write to Cassandra (pseudo-code, replace with actual DAO)
                    logger.info("CassandraSink: {}", value);
                }
            });

        purchases
            .keyBy(Purchase::getProductId)
            .window(org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows.of(Time.minutes(5)))
            .aggregate(new WindowAggregator(), new EventProcessor())
            .addSink(new RedisSink());

        purchases
            .keyBy(Purchase::getProductId)
            .window(org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows.of(Time.hours(1)))
            .aggregate(new WindowAggregator(), new EventProcessor())
            .addSink(new RedisSink());

        env.execute("FAANG Real-Time Analytics Flink Job");
    }

    // Redis sink implementation
    public static class RedisSink extends org.apache.flink.streaming.api.functions.sink.SinkFunction<String> {
        private transient Jedis jedis;
        @Override
        public void invoke(String value, Context context) {
            if (jedis == null) jedis = new Jedis("redis", 6379);
            jedis.publish("analytics-updates", value);
        }
    }
}

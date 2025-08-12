package com.faang.analytics;

import com.faang.analytics.model.Purchase;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.windowing.time.Time;
import redis.clients.jedis.Jedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamProcessingJob {
    private static final Logger logger = LoggerFactory.getLogger(StreamProcessingJob.class);

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(10000); // 10s
        env.getCheckpointConfig().setCheckpointingMode(org.apache.flink.streaming.api.CheckpointingMode.EXACTLY_ONCE);
        env.setParallelism(8);

        KafkaSource<Object> source = KafkaSource.<Object>builder()
                .setBootstrapServers("kafka:9092")
                .setTopics("ecommerce-events")
                .setGroupId("flink-analytics-group")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new EventDeserializer())
                .build();

        DataStream<Object> stream = env.fromSource(
                source,
                WatermarkStrategy.forMonotonousTimestamps(),
                "Kafka Source");

        DataStream<Purchase> purchases = stream
                .filter(e -> e instanceof Purchase)
                .map(e -> (Purchase) e);

        // 1min, 5min, 1hr window aggregations
        purchases
            .keyBy(Purchase::getProductId)
            .window(org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows.of(Time.minutes(1)))
            .aggregate(new WindowAggregator(), new EventProcessor())
            .map(value -> {
                // Write to Cassandra (pseudo-code, replace with actual DAO)
                logger.info("CassandraSink: {}", value);
                return value;
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
    public static class RedisSink implements org.apache.flink.streaming.api.functions.sink.SinkFunction<String> {
        private transient Jedis jedis;

        @Override
        public void invoke(String value, org.apache.flink.streaming.api.functions.sink.SinkFunction.Context context) throws Exception {
            if (jedis == null) {
                jedis = new Jedis("redis", 6379);
            }
            try {
                jedis.publish("analytics-updates", value);
            } catch (Exception e) {
                if (jedis != null) {
                    jedis.close();
                    jedis = null;
                }
                throw e;
            }
        }
    }
}

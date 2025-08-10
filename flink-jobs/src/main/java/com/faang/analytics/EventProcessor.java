package com.faang.analytics;

import com.faang.analytics.model.Purchase;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.util.Collector;

public class EventProcessor extends ProcessWindowFunction<WindowAggregator.Result, String, String, TimeWindow> {
    @Override
    public void process(String key, Context context, Iterable<WindowAggregator.Result> results, Collector<String> out) {
        for (WindowAggregator.Result result : results) {
            String summary = String.format("Window [%s-%s] Key: %s, Events: %d, Revenue: %.2f, TopProduct: %s",
                    context.window().getStart(), context.window().getEnd(), key, result.count, result.revenue, result.topProduct);
            out.collect(summary);
        }
    }
}

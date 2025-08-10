package com.faang.analytics;

import com.faang.analytics.model.Purchase;
import org.apache.flink.api.common.functions.AggregateFunction;

public class WindowAggregator implements AggregateFunction<Purchase, WindowAggregator.Accumulator, WindowAggregator.Result> {
    public static class Accumulator {
        public long count = 0;
        public double revenue = 0.0;
        public java.util.Map<String, Integer> productCounts = new java.util.HashMap<>();
    }
    public static class Result {
        public long count;
        public double revenue;
        public String topProduct;
    }
    @Override
    public Accumulator createAccumulator() {
        return new Accumulator();
    }
    @Override
    public Accumulator add(Purchase value, Accumulator acc) {
        acc.count++;
        acc.revenue += value.getAmount();
        acc.productCounts.merge(value.getProductId(), value.getQuantity(), Integer::sum);
        return acc;
    }
    @Override
    public Result getResult(Accumulator acc) {
        Result result = new Result();
        result.count = acc.count;
        result.revenue = acc.revenue;
        result.topProduct = acc.productCounts.entrySet().stream()
            .max(java.util.Map.Entry.comparingByValue())
            .map(java.util.Map.Entry::getKey)
            .orElse(null);
        return result;
    }
    @Override
    public Accumulator merge(Accumulator a, Accumulator b) {
        a.count += b.count;
        a.revenue += b.revenue;
        b.productCounts.forEach((k, v) -> a.productCounts.merge(k, v, Integer::sum));
        return a;
    }
}

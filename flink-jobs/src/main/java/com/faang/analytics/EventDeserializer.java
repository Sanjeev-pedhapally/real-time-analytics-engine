package com.faang.analytics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.faang.analytics.model.*;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;

public class EventDeserializer implements DeserializationSchema<Object> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object deserialize(byte[] message) throws IOException {
        String json = new String(message);
        // Simple type detection (could use a field in JSON)
        if (json.contains("productId") && json.contains("amount")) {
            return objectMapper.readValue(json, Purchase.class);
        } else if (json.contains("pageUrl")) {
            return objectMapper.readValue(json, PageView.class);
        } else if (json.contains("actionType")) {
            return objectMapper.readValue(json, CartAction.class);
        } else if (json.contains("activityType")) {
            return objectMapper.readValue(json, UserActivity.class);
        }
        return null;
    }

    @Override
    public boolean isEndOfStream(Object nextElement) {
        return false;
    }

    @Override
    public TypeInformation<Object> getProducedType() {
        return TypeInformation.of(Object.class);
    }
}

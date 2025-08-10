package com.faang.analytics.config;

import com.datastax.oss.driver.api.core.CqlSession;
import java.net.InetSocketAddress;

public class CassandraConfig {
    public static CqlSession createSession() {
        return CqlSession.builder()
                .addContactPoint(new InetSocketAddress("cassandra", 9042))
                .withLocalDatacenter("datacenter1")
                .withKeyspace("analytics")
                .build();
    }
}

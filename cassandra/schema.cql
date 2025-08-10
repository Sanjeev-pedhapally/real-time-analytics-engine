-- Time-series optimized schema for e-commerce analytics
CREATE KEYSPACE IF NOT EXISTS analytics WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

CREATE TABLE IF NOT EXISTS analytics.purchase_events (
    user_id TEXT,
    event_time TIMESTAMP,
    product_id TEXT,
    amount DOUBLE,
    quantity INT,
    PRIMARY KEY ((user_id), event_time, product_id)
) WITH CLUSTERING ORDER BY (event_time DESC);

CREATE TABLE IF NOT EXISTS analytics.pageview_events (
    user_id TEXT,
    event_time TIMESTAMP,
    page_url TEXT,
    PRIMARY KEY ((user_id), event_time, page_url)
) WITH CLUSTERING ORDER BY (event_time DESC);

CREATE TABLE IF NOT EXISTS analytics.cart_actions (
    user_id TEXT,
    event_time TIMESTAMP,
    product_id TEXT,
    action_type TEXT,
    quantity INT,
    PRIMARY KEY ((user_id), event_time, product_id)
) WITH CLUSTERING ORDER BY (event_time DESC);

CREATE TABLE IF NOT EXISTS analytics.user_activities (
    user_id TEXT,
    event_time TIMESTAMP,
    activity_type TEXT,
    details TEXT,
    PRIMARY KEY ((user_id), event_time, activity_type)
) WITH CLUSTERING ORDER BY (event_time DESC);

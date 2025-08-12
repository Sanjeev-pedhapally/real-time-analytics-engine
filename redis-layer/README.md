# Redis Layer
Connection pooling, pub/sub for real-time updates, and circuit breaker patterns. Production-grade error handling.

# Redis Caching Layer

## Overview
Production-grade Redis caching with connection pooling, pub/sub, clustering, and cache-aside pattern. Includes error handling and circuit breaker support.

## Setup
1. Build and run Redis with clustering:
   ```sh
   docker build -t analytics-redis .
   docker run -d --name redis -p 6379:6379 analytics-redis
   ```
2. Configure `redis.conf` for clustering and performance.
3. Integrate `RedisService` and `CacheManager` in your Java services.

## Features
- Connection pooling (Jedis)
- Pub/Sub for real-time updates
- Cache-aside pattern for fast reads
- Clustering and LRU eviction
- Error handling, logging

## Performance Notes
- Pool size: configurable
- Sub-ms cache latency, 10K+ ops/sec
- Circuit breaker: fallback on errors

me: what I did, like, I created three separate individual queries. First, one getting all the data from this V_enriched membership and I included controls ISM/A1a in the first one and in the second query from Visit_hx and the third one is from call_hx. So if you go to the first query, so this is I named as base and getting PSUID, and all those things and case when this number indicator is called to Y as this like test number flag and when like I take in for like last month for July month. So I created like when this date is between a coverage, it affected date and exp expression rate, then has valid coverage flag. and I also added this ISM A controls. And for like from visits, I just to all the things like indiv_id, visit date with all those things from visit_hx. And again, all those things from call_hx. And in last table, like in member visit call, I am like joining those things on individual analaytics_id side. So after that, like I got like this table where we where we'll be having like exec date , subscriber ID, individual_analytcs_ID, LOB, and customer segment and all other things  as well like platform, like mobile, web, those things and call event time and all those things. So, like this is how you wanted Mark or I mean, I need to change this thing. 

lead: Okay, so this is... This is all the member data. Yeah. And you also added the visit and the call data on the same table. 

me: Yeah, like, I left join  all the data to that from visits and calls. so I can send you the queries.. Okay, it attach it to the file And this is the final table mark,

lead: So what? Can you walk me through the logic of this all queries It is. So what's the model of the final table? The final table has one row per what? The final table, like this is the final. table here. 

me: This is a member visit called. So it will be having a subscriber ID, individual_analytcs_iD, LOB, customer segment, state CD valid is it valid coverage or not, and test member flag? And I just added this thing we can remove if you want, like product information, like products category, product type, has medical as gle. Here it's getting in case when like ISM number is going to one, then ISM. And like we wanted this to as well, like ISM and A with commercial those things.

lead:So what's the primary key on this table? 

me: Indiv_analytcs_identifier. That's the primary key. 

lead: So what if I have more than one visit during the time period? Okay. Yep. Yeah, I like the fact that it has all the members. And then. it has all the visits and all the calls. And then, so like, if someone has 12 visits and two calls, they're gonna have 24 records. Is that right? Yeah. And some of them will have a one for call after a visit, and some of them won't. Okay. I'm I'm trying to think the Let me let me think about it. I think there might be a I't only having everything in one single table is the best solution for querying about this. Okay. Because it could get really big. For people have multip calls or multiple visits.. It's basically a cross joint, right? Yeah. And we don't really want that. Okay. The other thing you're doing is you're joining on the member ID, individual analytic identifier. Yeah. And when we join calls and visits, we're supposed to use a indiv_analytcs_subscriber ID. Hmm. Again, in all of our call after visits, we join on subscriber ID, right? Yeah. But here you're joining on member ID. M Okay, So this is the table where everything's stored..I think technically, this absolutely works to solve the problem. I just think there's a. Let me think about it.
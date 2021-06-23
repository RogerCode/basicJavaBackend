package com.example.demo.configuration;

import com.example.demo.pojo.redis.UserAliveResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;
import lombok.SneakyThrows;

public class RedisRoomConnection {
    String roomId;
    ObjectMapper objectMapper;
    RedisPubSubAsyncCommands<String, String> publisher;

    public RedisRoomConnection(String roomId) throws JsonProcessingException {
        this.roomId = roomId;

        objectMapper = new ObjectMapper();
        RedisClient subscriberRedisClient = RedisClient.create("redis://ra1re2ri3@localhost:6379/1");
        StatefulRedisPubSubConnection<String, String> subscriberConnection = subscriberRedisClient.connectPubSub();
        RedisClient publisherRedisClient = RedisClient.create("redis://ra1re2ri3@localhost:6379/1");
        StatefulRedisPubSubConnection<String, String> publisherConnection = publisherRedisClient.connectPubSub();

        RedisPubSubCommands<String, String> subscriber = subscriberConnection.sync();
        publisher = publisherConnection.async();

        RedisPubSubListener<String, String> listener = new RedisPubSubAdapter<String, String>() {
            @SneakyThrows
            @Override
            public void message(String channel, String message) {
                System.out.println(String.format("Channel: %s, Message: %s", channel, message));
            }
        };
        subscriberConnection.addListener(listener);
        subscriber.subscribe(roomId);
        UserAliveResponse userAliveResponse = new UserAliveResponse();
        userAliveResponse.setUserName("paco");
        publishToRoom(userAliveResponse);
    }

    private void publishToRoom(Object object) throws JsonProcessingException {
        publisher.publish(roomId, objectMapper.writeValueAsString(object));
    }
}

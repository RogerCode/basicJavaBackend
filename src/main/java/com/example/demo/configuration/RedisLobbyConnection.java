package com.example.demo.configuration;

import com.example.demo.pojo.authentication.LoginRequest;
import com.example.demo.pojo.redis.UserMatchMakingRequest;
import com.example.demo.pojo.redis.UserMatchMakingResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;


public class RedisLobbyConnection {


    public void RedisLobbyConnection() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String userID = UUID.randomUUID().toString();
        System.out.println(String.format("User ID"));
        System.out.println(String.format(userID));
        RedisClient subscriberRedisClient = RedisClient.create("redis://ra1re2ri3@localhost:6379/1");
        StatefulRedisPubSubConnection<String, String> subscriberConnection = subscriberRedisClient.connectPubSub();
        RedisClient publisherRedisClient = RedisClient.create("redis://ra1re2ri3@localhost:6379/1");
        StatefulRedisPubSubConnection<String, String> publisherConnection = publisherRedisClient.connectPubSub();

        RedisPubSubCommands<String, String> subscriber = subscriberConnection.sync();
        RedisPubSubAsyncCommands<String, String> publisher = publisherConnection.async();

        RedisPubSubListener<String, String> listener = new RedisPubSubAdapter<String, String>() {
            @SneakyThrows
            @Override
            public void message(String channel, String message) {
                System.out.println(String.format("Channel: %s, Message: %s", channel, message));
                UserMatchMakingResponse userMatchMakingResponse = objectMapper.readValue(message, UserMatchMakingResponse.class);
                new RedisRoomConnection(userMatchMakingResponse.getChannelId());
            }
        };
        subscriberConnection.addListener(listener);
        subscriber.subscribe(userID);
        UserMatchMakingRequest userMatchMakingRequest = new UserMatchMakingRequest();
        userMatchMakingRequest.setUserId(userID);
        publisher.publish("Lobby", objectMapper.writeValueAsString(userMatchMakingRequest));
        publisher.publish("Lobby", objectMapper.writeValueAsString(userMatchMakingRequest));
    }



}

package com.example.demo.controller;

import com.example.demo.pojo.websockets.WebSocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat")
    @SendToUser("/topic/messages")
    public String getMessages(WebSocketMessage dto) {
        System.out.println(String.format("Respuesta web socket para "));
        return  "{\"success\":1}";
    }


}
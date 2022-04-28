package com.inside.InsideTest.controller;

import com.inside.InsideTest.domain.Message;
import com.inside.InsideTest.domain.RequestMessageDetails;
import com.inside.InsideTest.service.MessageService;
import com.inside.InsideTest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST com.inside.InsideTest.controller.
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MessageController {

    private final static String HISTORY_10 = "history 10";

    private final UserService userService;
    private final MessageService messageService;

    /**
     * Process post command from user
     * @param message Message in JSON format that user sends.
     * @return Response which depends on user message.
     */

    @PostMapping("/user/message")
    public ResponseEntity<?> sendMessage(@RequestBody RequestMessageDetails message) {
        if(message.getMessage().equals(HISTORY_10)) {
            List<Message> messages = messageService.getLastTenUserMessages(userService.getUser(message.getUsername()));
            return ResponseEntity.ok(messages);
        } else {
            messageService.saveMessage(message.getMessage(), message.getUsername());
            return ResponseEntity.ok().build();
        }
    }
}

package com.inside.InsideTest.service;

import com.inside.InsideTest.domain.Message;
import com.inside.InsideTest.domain.User;

import java.util.List;

/**
 * Message com.inside.InsideTest.service
 */

public interface MessageService {
    Message saveMessage(String message, String username);
    List<Message> getLastTenUserMessages(User user);
}

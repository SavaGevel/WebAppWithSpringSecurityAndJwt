package com.inside.InsideTest.main.service;

import com.inside.InsideTest.main.domain.Message;
import com.inside.InsideTest.main.domain.User;

import java.util.List;

/**
 * Message service
 */

public interface MessageService {
    Message saveMessage(String message, String username);
    List<Message> getLastTenUserMessages(User user);
}

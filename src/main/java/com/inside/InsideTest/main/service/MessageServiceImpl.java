package com.inside.InsideTest.main.service;

import com.inside.InsideTest.main.domain.Message;
import com.inside.InsideTest.main.domain.User;
import com.inside.InsideTest.main.repos.MessageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Message service implementation
 */

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{
    private final MessageRepo messageRepo;
    private final UserService userService;

    @Override
    @Transactional
    public Message saveMessage(String message, String username) {
        User user = userService.getUser(username);
        Message msg = new Message(null, message, LocalDateTime.now(), user);
        return messageRepo.save(msg);
    }

    @Override
    public List<Message> getLastTenUserMessages(User user) {
        return messageRepo.findFirst10ByUser(user, Sort.by(Sort.Direction.DESC, "date"));
    }
}

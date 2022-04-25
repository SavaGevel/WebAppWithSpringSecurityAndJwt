package com.inside.InsideTest.main.repos;

import com.inside.InsideTest.main.domain.Message;
import com.inside.InsideTest.main.domain.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Message repository
 */

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {
    List<Message> findFirst10ByUser(User user, Sort sort);
}

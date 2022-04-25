package com.inside.InsideTest.main.repos;

import com.inside.InsideTest.main.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * User repository
 */

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

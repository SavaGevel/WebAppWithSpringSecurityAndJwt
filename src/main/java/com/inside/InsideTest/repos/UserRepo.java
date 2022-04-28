package com.inside.InsideTest.repos;

import com.inside.InsideTest.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * User repository
 */

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

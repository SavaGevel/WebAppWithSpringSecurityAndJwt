package com.inside.InsideTest.main.service;

import com.inside.InsideTest.main.domain.Role;
import com.inside.InsideTest.main.domain.User;

/**
 * User service
 */

public interface UserService {

    void saveUser(User user);
    User getUser(String username);
    void addRoleToUser(User user, Role role);
    void saveRole(Role role);
    Role getRole(String roleName);
}

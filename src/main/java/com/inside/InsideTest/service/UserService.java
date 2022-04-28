package com.inside.InsideTest.service;

import com.inside.InsideTest.domain.Role;
import com.inside.InsideTest.domain.User;

/**
 * User com.inside.InsideTest.service
 */

public interface UserService {

    void saveUser(User user);
    User getUser(String username);
    void addRoleToUser(User user, Role role);
    void saveRole(Role role);
    Role getRole(String roleName);
}

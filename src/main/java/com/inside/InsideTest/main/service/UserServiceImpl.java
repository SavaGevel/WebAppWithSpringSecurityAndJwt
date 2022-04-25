package com.inside.InsideTest.main.service;

import com.inside.InsideTest.main.domain.Role;
import com.inside.InsideTest.main.domain.User;
import com.inside.InsideTest.main.repos.RoleRepo;
import com.inside.InsideTest.main.repos.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

/**
 * User service implementation
 */

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException(String.format("User %s not found", username));
        }

        Collection<SimpleGrantedAuthority> authorities = new LinkedList<>();
        user.getRoles().forEach(authority -> authorities.add(new SimpleGrantedAuthority(authority.getName())));

        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    @Override
    public User getUser(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    @Transactional
    public void addRoleToUser(User user, Role role) {
        Set<Role> roles = user.getRoles();
        roles.add(role);
        user.setRoles(roles);
        userRepo.save(user);
    }

    @Override
    @Transactional
    public void saveRole(Role role) {
        roleRepo.save(role);
    }

    @Override
    public Role getRole(String roleName) {
        return roleRepo.findByName(roleName);
    }
}

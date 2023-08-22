package com.benson.bensonservice.config.security;

import com.benson.bensonservice.exception.ResourceNotFoundException;
import com.benson.bensonservice.model.entity.User;
import com.benson.bensonservice.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LogManager.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserRepo userRepo;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        User user = userRepo.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email : " + username)
                );

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Integer id) {
        User user = userRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(user);
    }
}

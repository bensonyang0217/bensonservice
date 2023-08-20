package com.benson.bensonservice.service.impl;

import com.benson.bensonservice.constants.Role;
import com.benson.bensonservice.model.entity.User;
import com.benson.bensonservice.model.vo.AddUserVo;
import com.benson.bensonservice.repo.UserRepo;
import com.benson.bensonservice.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepo userRepo;


    @Override
    public AddUserVo addUser(AddUserVo addUserVo) {
        logger.debug("Add User: {}", addUserVo);
        User user = User.builder().username(addUserVo.getUsername())
                .role(Role.USER)
                .password(passwordEncoder.encode(addUserVo.getPassword())).build();
//        User user = new User();
//        user.setUsername(addUserVo.getUsername());
//        user.setPassword(passwordEncoder.encode(addUserVo.getPassword()));
        userRepo.save(user);
        return addUserVo;
    }
}

package com.benson.bensonservice.service.impl;

import com.benson.bensonservice.constants.AuthProvider;
import com.benson.bensonservice.constants.Role;
import com.benson.bensonservice.model.entity.User;
import com.benson.bensonservice.model.vo.AddUserVo;
import com.benson.bensonservice.model.vo.UserMeVo;
import com.benson.bensonservice.repo.UserRepo;
import com.benson.bensonservice.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.benson.bensonservice.utils.AuthUtils.getUserId;

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
                .password(passwordEncoder.encode(addUserVo.getPassword()))
                .provider(AuthProvider.local)
                .build();
        userRepo.save(user);
        return addUserVo;
    }

    @Override
    public UserMeVo currentUser() {
        Integer id = getUserId();
        Optional<User> optUser = Optional.of(userRepo.getById(id));
        AtomicReference<UserMeVo> userMeVo = new AtomicReference<>();
        ;

        optUser.ifPresent(user -> {
            userMeVo.set(UserMeVo.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .name(user.getName())
                    .role(user.getRole())
                    .build());
        });
        return userMeVo.get();
    }

    @Override
    public UserMeVo findById(Integer userId) {
        Optional<User> optUser = userRepo.findById(userId);
        UserMeVo userMeVo = null;
        if (optUser.isPresent()) {
            var username = optUser.get().getUsername();
            var id = optUser.get().getId();
            var name = optUser.get().getName();
            var email = optUser.get().getEmail();
            var role = optUser.get().getRole();
            userMeVo = UserMeVo.builder()
                    .id(id)
                    .username(username)
                    .name(name)
                    .email(email)
                    .role(role)
                    .build();
        }
        return userMeVo;
    }
}

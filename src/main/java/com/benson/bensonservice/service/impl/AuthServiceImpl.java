package com.benson.bensonservice.service.impl;

import com.benson.bensonservice.model.entity.User;
import com.benson.bensonservice.model.vo.LoginVo;
import com.benson.bensonservice.repo.UserRepo;
import com.benson.bensonservice.service.AuthService;
import com.benson.bensonservice.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public String getToken(LoginVo loginVo) {
        String username = loginVo.getUsername();
        String password = loginVo.getPassword();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        User user = (User) authentication.getPrincipal();
        String token = jwtTokenProvider.createToken(user);
        return token;
    }
}

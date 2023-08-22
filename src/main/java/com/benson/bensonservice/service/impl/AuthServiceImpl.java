package com.benson.bensonservice.service.impl;

import com.benson.bensonservice.model.vo.LoginVo;
import com.benson.bensonservice.model.vo.TokenResponseVo;
import com.benson.bensonservice.repo.UserRepo;
import com.benson.bensonservice.service.AuthService;
import com.benson.bensonservice.utils.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LogManager.getLogger(AuthServiceImpl.class);
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public TokenResponseVo getToken(LoginVo loginVo) {
        String username = loginVo.getUsername();
        String password = loginVo.getPassword();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
//            User user = (User) authentication.getPrincipal();
            String token = jwtTokenProvider.createToken(authentication);
            TokenResponseVo tokenResponseVo = TokenResponseVo.builder()
//                    .username(user.getUsername())
                    .token(token).build();
            return tokenResponseVo;
        } catch (Exception e) {
            logger.error("getToken error: {}", e.getMessage());
            TokenResponseVo tokenResponseVo = null;
            return tokenResponseVo;
        }

    }

    @Override
    public String refreshToken(HttpServletRequest request) {
        String currToken = jwtTokenProvider.resolveToken(request);
        String newToken = jwtTokenProvider.refreshToken(currToken);
        return newToken;
    }
}

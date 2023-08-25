package com.benson.bensonservice.controller;

import com.benson.bensonservice.model.vo.LoginVo;
import com.benson.bensonservice.model.vo.TokenResponseVo;
import com.benson.bensonservice.response.Body;
import com.benson.bensonservice.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/token")
    public ResponseEntity<Body> getToken(@RequestBody LoginVo loginVo) {
        Optional<TokenResponseVo> tokenResponseVo = Optional.ofNullable(authService.getToken(loginVo));
        if (!tokenResponseVo.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body((Body.build().fail("Invalid username or password.")));
        }
        return ResponseEntity.ok(Body.build().ok("success", tokenResponseVo));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<Body> refreshToken(HttpServletRequest request) {
        return ResponseEntity.ok(Body.build().ok("success", authService.refreshToken(request)));
    }
}

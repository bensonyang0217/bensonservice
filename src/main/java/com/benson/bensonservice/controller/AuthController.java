package com.benson.bensonservice.controller;

import com.benson.bensonservice.model.vo.LoginVo;
import com.benson.bensonservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/token")
    public String getToken(@RequestBody LoginVo loginVo) {
        return authService.getToken(loginVo);
    }
}

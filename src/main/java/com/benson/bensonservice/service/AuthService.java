package com.benson.bensonservice.service;

import com.benson.bensonservice.model.vo.LoginVo;
import com.benson.bensonservice.model.vo.TokenResponseVo;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    TokenResponseVo getToken(LoginVo loginVo);

    String refreshToken(HttpServletRequest request);
}

package com.benson.bensonservice.service;

import com.benson.bensonservice.model.vo.LoginVo;

public interface AuthService {

    String getToken(LoginVo loginVo);
}

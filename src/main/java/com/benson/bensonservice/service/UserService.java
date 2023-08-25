package com.benson.bensonservice.service;

import com.benson.bensonservice.model.vo.AddUserVo;
import com.benson.bensonservice.model.vo.UserMeVo;

public interface UserService {

    AddUserVo addUser(AddUserVo addUserVo);

    UserMeVo currentUser();
}

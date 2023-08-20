package com.benson.bensonservice.controller;

import com.benson.bensonservice.model.vo.AddUserVo;
import com.benson.bensonservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<AddUserVo> AddUser(@RequestBody AddUserVo addUserVo) {
        return ResponseEntity.ok(userService.addUser(addUserVo));
    }
}

package com.benson.bensonservice.controller;

import com.benson.bensonservice.model.vo.AddUserVo;
import com.benson.bensonservice.model.vo.UserMeVo;
import com.benson.bensonservice.response.Body;
import com.benson.bensonservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<Body> addUser(@RequestBody AddUserVo addUserVo) {
        return ResponseEntity.ok(Body.build().ok("success", userService.addUser(addUserVo)));
    }

    @GetMapping("/me")
    public ResponseEntity<Body> me() {
        Optional<UserMeVo> optUser = Optional.ofNullable(userService.currentUser());
        if (!optUser.isPresent()) {
            return ResponseEntity.ok(Body.build().fail("not found"));
        }
        return ResponseEntity.ok(Body.build().ok("success", optUser.get()));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("")
    public ResponseEntity<Body> userList() {
        return null;
    }
}

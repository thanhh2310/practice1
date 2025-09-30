package com.example.practice_security_build.Controller;

import com.example.practice_security_build.Model.User;
import com.example.practice_security_build.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/api")
public class TestController {
    @Autowired
    private UserService userService;
    @GetMapping("/public/homeTest")
    public String HelloWorld(){
        return "Xin chao thanh";
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUser(){
        List<User> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }
}

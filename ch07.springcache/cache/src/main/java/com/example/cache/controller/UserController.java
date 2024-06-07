package com.example.cache.controller;

import com.example.cache.domain.entity.User;
import com.example.cache.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("users/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }
}

package com.demo.aop.rest.user;

import com.demo.aop.aspects.UseControllerAdvice;
import com.demo.aop.service.UserService;
import com.demo.aop.service.model.UserProperties;
import com.demo.aop.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@UseControllerAdvice
@RequestMapping(value = "/v1/user")
public class UserControllerV1 {

    private UserService userService;

    @Autowired
    public UserControllerV1(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value="{id}")
    public ResponseEntity<User> getUser(@PathVariable(value = "id") UUID id) {
        return ResponseEntity.ok(userService.get(id));
    }

    @GetMapping
    public ResponseEntity<List<User>> getUserList() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserProperties user) {
        User created = userService.create(user);
        System.out.println("User created: " + created);
        return ResponseEntity.ok(created);
    }
}
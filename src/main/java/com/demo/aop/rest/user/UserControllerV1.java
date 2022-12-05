package com.demo.aop.rest.user;

import com.demo.aop.repository.RepositoryException;
import com.demo.aop.service.UserService;
import com.demo.aop.service.exception.ValidationException;
import com.demo.aop.service.model.CreateUser;
import com.demo.aop.service.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
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
    public ResponseEntity<User> createUser(@RequestBody CreateUser user) {
        User created = userService.create(user);
        System.out.println("User created: " + created);
        return ResponseEntity.ok(created);
    }
}
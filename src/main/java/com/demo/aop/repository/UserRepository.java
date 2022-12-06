package com.demo.aop.repository;

import com.demo.aop.service.model.UserProperties;
import com.demo.aop.service.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository {
    List<User> getAll();
    User get(UUID id);
    User find(String email);
    User create(UserProperties properties);
    User update(UUID id, UserProperties properties);
    void delete(UUID id);
}



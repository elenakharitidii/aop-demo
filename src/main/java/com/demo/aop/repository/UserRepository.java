package com.demo.aop.repository;

import com.demo.aop.service.model.CreateUser;
import com.demo.aop.service.model.UpdateUser;
import com.demo.aop.service.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository {
    List<User> getAll();
    User get(UUID id);
    User find(String email);
    User create(CreateUser properties);
    User update(UpdateUser properties);
    void delete(UUID id);
}



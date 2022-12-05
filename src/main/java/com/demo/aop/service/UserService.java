package com.demo.aop.service;

import com.demo.aop.repository.UserRepository;
import com.demo.aop.service.exception.ValidationException;
import com.demo.aop.service.model.CreateUser;
import com.demo.aop.service.model.UpdateUser;
import com.demo.aop.service.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.demo.aop.service.exception.ErrorMessageFactory.*;

public interface UserService {
    List<User> getAll();
    User get(UUID id);
    User create(CreateUser properties);
    User update(UpdateUser properties);
    void delete(UUID id);
}

@Service
class UserServiceImpl implements UserService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> getAll() {
        return repository.getAll();
    }

    @Override
    public User get(UUID id) {
        logger.info("event=EnterMethodCall, signature={}.{}(..), id={}", this.getClass().getSimpleName(), "create", id);
        // return repository.get(id);

        Instant start = Instant.now();
        try {
            User user =  repository.get(id);
            logger.info("event=ExitMethodCall, signature={}.{}(..), user={}, duration={}",
                    this.getClass().getSimpleName(), "create", user, Duration.between(start, Instant.now()));
            return user;
        }
        catch(Exception e) {
            logger.info("event=ExitMethodCallWithError, signature={}.{}(..), id={}, duration={}",
                    this.getClass().getSimpleName(), "create", id, Duration.between(start, Instant.now()).toMillis(), e);
            throw e;
        }
    }

    @Override
    public User create(CreateUser properties) {
        logger.info("event=EnterMethodCall, signature={}.{}(..), input={}", this.getClass().getSimpleName(), "create", properties);
        try {
            validate(properties);
            checkUniqueEmail(properties.getEmail());
            User user =  repository.create(properties);
            logger.info("event=ExitMethodCall, signature={}.{}(..), result={}", this.getClass().getSimpleName(), "create", user);
            return user;
        }
        catch(Exception e) {
            logger.info("event=ExitMethodCallWithError, signature={}.{}(..), input={}", this.getClass().getSimpleName(), "create", properties, e);
            logger.error("event=createUserFailed, inputs={}", properties, e);
            throw e;
        }
    }

    @Override
    public User update(UpdateUser properties) {
        return repository.update(properties);
    }

    @Override
    public void delete(UUID id) {
        repository.delete(id);
    }

    private void checkUniqueEmail(String email) {
        User existing = repository.find(email);
        if (existing != null) {
            throw new ValidationException(userEmailExists(email));
        }
    }
    private void validate(CreateUser properties) {
        if (properties.getEmail() == null || properties.getEmail().trim().length() == 0) {
            throw new ValidationException(invalidEmail(properties.getEmail()));
        }

        if (properties.getName() == null || properties.getName().trim().length() == 0) {
            throw new ValidationException(invalidValue("name", properties.getName()));
        }

        if (properties.getUsername() == null || properties.getUsername().trim().length() == 0) {
            throw new ValidationException(invalidValue("username", properties.getName()));
        }
    }
}

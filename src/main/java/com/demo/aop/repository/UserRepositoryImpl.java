package com.demo.aop.repository;

import com.demo.aop.service.model.CreateUser;
import com.demo.aop.service.model.UpdateUser;
import com.demo.aop.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.demo.aop.repository.RepositoryException.RepositoryError.*;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private Map<UUID, User> store = new HashMap<>();

    @Autowired
    public UserRepositoryImpl() {}

    @Override
    public List<User> getAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public User get(UUID id) {
        return Optional.ofNullable(store.get(id))
            .orElseThrow(() -> new RepositoryException(NOT_FOUND, "User"));
    }

    @Override
    public User find(String email) {
        if (email == null || email.trim().length() == 0)
            throw new RepositoryException(INVALID, "email");
        return store.values().stream()
                .filter(user -> email.trim().equals(user.getEmail()))
                .findAny()
                .orElse(null);
    }

    @Override
    public User create(CreateUser properties) {
        checkUniqueEmail(properties.getEmail());
        checkPhone(properties.getPhone());
        User user = new User(
                UUID.randomUUID(),
                properties.getName(),
                properties.getUsername(),
                properties.getEmail(),
                properties.getPhone(),
                true);
        store.put(user.getId(), user);
        return store.get(user.getId());
    }

    @Override
    public User update(UpdateUser properties) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }

    private synchronized void checkUniqueEmail(String email) {
        User existing = find(email);
        if (existing != null && existing.isActive()) {
            throw new RepositoryException(CONFLICT, "email");
        }
    }

    private synchronized void checkPhone(String phone) {
        if (phone == null || phone.length() == 0) {
            throw new RepositoryException(INVALID, "phone");
        }
    }
}

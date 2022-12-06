package com.demo.aop.repository;

import com.demo.aop.service.model.UserProperties;
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
    public User update(UUID id, UserProperties properties) {
        if (store.containsKey(id)) {
            return writeToStore(id, properties);
        }
        throw new RepositoryException(NOT_FOUND, "user");
    }

    @Override
    public User create(UserProperties properties) {
        checkUniqueEmail(properties.getEmail());
        checkPhone(properties.getPhone());
        return writeToStore(UUID.randomUUID(), properties);
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

    private void checkPhone(String phone) {
        if (phone == null || phone.length() == 0) {
            throw new RepositoryException(INVALID, "phone");
        }
    }

    private User writeToStore(UUID id, UserProperties properties) {
        User user = new User(
                id,
                properties.getName(),
                properties.getUsername(),
                properties.getEmail(),
                properties.getPhone(),
                true);
        store.put(user.getId(), user);
        return store.get(user.getId());
    }
}

package com.demo.aop.rest.user;

import com.demo.aop.repository.RepositoryException;
import com.demo.aop.service.UserService;
import com.demo.aop.service.exception.ValidationException;
import com.demo.aop.service.model.UserProperties;
import com.demo.aop.service.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/v2/user")
public class UserControllerV2 {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private UserService userService;

    @Autowired
    public UserControllerV2(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value="{id}")
    public ResponseEntity getUser(@PathVariable(value = "id") String id) {
        try {
            return ResponseEntity.ok(userService.get(UUID.fromString(id)));
        }
        catch (ValidationException e) {
            logger.error("event=getUserFailed, id={}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse(
                                    HttpStatus.BAD_REQUEST.value(),
                                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                    "Failed to get user, input failed validation, please see logs for more details",
                                    "/v2/user"
                            )
                    );
        }
        catch (RepositoryException e) {
            logger.error("event=getUserFailed, id={}", id, e);
            if (e.getLocalizedMessage().contains("INVALID")) {
                return ResponseEntity
                        .badRequest()
                        .body("Failed to get user, input failed validation, please see logs for more details");
            } else  if (e.getLocalizedMessage().contains("NOT_FOUND")) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Failed to get user, user does not exist, please see logs for more details");
            } else {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to create user, please see logs for more details");
            }
        }
        catch (Exception e) {
            logger.error("event=getUserFailed, id={}", id, e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create user, please see logs for more details");
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getUserList() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody UserProperties user) {
        try {
            return ResponseEntity.ok(userService.create(user));
        }
        catch (ValidationException e) {
            logger.error("event=createUserFailed, user={}", user, e);
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "Failed to create user, input failed validation, please see logs for more details",
                            "/v2/user"
                        )
                    );
        }
        catch (RepositoryException e) {
            logger.error("event=createUserFailed, user={}", user, e);
            if (e.getLocalizedMessage().contains("INVALID")) {
                return ResponseEntity
                        .badRequest()
                        .body("Failed to create user, input failed validation, please see logs for more details");
            } else {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to create user, please see logs for more details");
            }
        }
        catch (Exception e) {
            logger.error("event=createUserFailed, user={}", user, e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create user, please see logs for more details");
        }
    }

    @PutMapping(value="{id}")
    public ResponseEntity updateUser(@PathVariable(value = "id") String id, @RequestBody UserProperties user) {
        try {
            return ResponseEntity.ok(userService.update(UUID.fromString(id), user));
        }
        catch (ValidationException e) {
            logger.error("event=updateUserFailed, user={}", user, e);
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse(
                                    HttpStatus.BAD_REQUEST.value(),
                                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                    "Failed to update user, input failed validation, please see logs for more details",
                                    "/v2/user"
                            )
                    );
        }
        catch (RepositoryException e) {
            logger.error("event=updateUserFailed, id={}, user={}", id, user, e);
            if (e.getLocalizedMessage().contains("INVALID")) {
                return ResponseEntity
                        .badRequest()
                        .body("Failed to update user, input failed validation, please see logs for more details");
            } else {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to update user, please see logs for more details");
            }
        }
        catch (Exception e) {
            logger.error("event=updateUserFailed, id={}, user={}", id, user, e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update user, please see logs for more details");
        }
    }
}

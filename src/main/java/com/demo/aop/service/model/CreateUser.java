package com.demo.aop.service.model;

import com.demo.aop.service.exception.ValidationException;
import lombok.*;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Optional;

import static com.demo.aop.service.exception.ErrorMessageFactory.invalidEmail;

@Getter
@Builder
// @ToString
// @AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CreateUser {
    private String name;
    private String username;
    private String email;
    private String phone;

    public static CreateUserBuilder builder() {
        return new CustomCreateUserBuilder();
    }

    private static class CustomCreateUserBuilder extends CreateUserBuilder {
        public CreateUser build() {
            try {
                InternetAddress address = new InternetAddress(super.email);
                address.validate();
            } catch (AddressException ex) {
                throw new ValidationException(invalidEmail(super.email), ex);
            }
            return super.build();
        }
    }

    public CreateUser(String name, String username, String email, String phone) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "CreateUser{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}

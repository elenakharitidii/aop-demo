package com.demo.aop.service.model;

import com.demo.aop.service.exception.ValidationException;
import lombok.*;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import static com.demo.aop.service.exception.ErrorMessageFactory.invalidEmail;

@Getter
@Builder
// @ToString
// @AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UserProperties {
    private String name;
    private String username;
    private String email;
    private String phone;

    public static UserPropertiesBuilder builder() {
        return new CustomUserPropertiesBuilder();
    }

    private static class CustomUserPropertiesBuilder extends UserPropertiesBuilder {
        public UserProperties build() {
            try {
                InternetAddress address = new InternetAddress(super.email);
                address.validate();
            } catch (AddressException ex) {
                throw new ValidationException(invalidEmail(super.email), ex);
            }
            return super.build();
        }
    }

    public UserProperties(String name, String username, String email, String phone) {
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

package com.trello.trello.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.trello.trello.model.Users;

@Service
public class UserService {

    public List<Users> maskEmails(List<Users> users) {
        return users.stream()
                .map(user -> {
                    user.setEmail(maskEmail(user.getEmail()));
                    return user;
                })
                .collect(Collectors.toList());
    }

    public String maskEmail(String email) {
        String[] parts = email.split("@");
        if (parts.length == 2) {
            String domain = parts[1];
            return "****@" + domain;
        } else {
            return email;
        }
    }
}

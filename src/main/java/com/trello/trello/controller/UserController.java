package com.trello.trello.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trello.trello.model.Users;
import com.trello.trello.repository.UserRepository;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    Users getUser(@PathVariable String id) {
        return userRepository.findById(id).orElse(null);
    }

    @PostMapping("/")
    Users createUser(@RequestBody Users user) {
        String email = user.getEmail();

        if (userRepository.existsByEmail(email)) {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Kayıtlı kullanıcı bulunamadı."));
        }
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    Users updateUser(@PathVariable String id, @RequestBody Users user) {
        Users oldUser = userRepository.findById(id).orElse(null);
        oldUser.setName(user.getName());
        oldUser.setAvatar(user.getAvatar());
        oldUser.setProvider(user.getProvider());
        oldUser.setUsername(user.getUsername());
        return userRepository.save(oldUser);
    }

    @DeleteMapping("/{id}")
    String deleteUser(@PathVariable String id) {
        userRepository.deleteById(id);
        return id;
    }
}

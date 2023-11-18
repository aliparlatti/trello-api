package com.trello.trello.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        oldUser.setProjects(user.getProjects());
        return userRepository.save(oldUser);
    }

    @PatchMapping("/{id}")
    public Users updatePartialUser(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        Users oldUser = userRepository.findById(id).orElse(null);

        if (oldUser != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            Users updateUser = objectMapper.convertValue(updates, Users.class);

            if (updateUser.getName() != null) {
                oldUser.setName(updateUser.getName());
            }

            if (updateUser.getEmail() != null) {
                oldUser.setEmail(updateUser.getEmail());
            }

            if (updateUser.getProjects() != null) {
                oldUser.setProjects(updateUser.getProjects());
            }

            if (updateUser.getUsername() != null) {
                oldUser.setUsername(updateUser.getUsername());
            }
            if (updateUser.getProvider() != null) {
                oldUser.setProvider(updateUser.getProvider());
            }

            return userRepository.save(oldUser);
        } else {
            throw new RuntimeException("Kullanıcı bulunamadı.");
        }
    }

    @DeleteMapping("/{id}")
    String deleteUser(@PathVariable String id) {
        userRepository.deleteById(id);
        return id;
    }
}

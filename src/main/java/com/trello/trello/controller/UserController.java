package com.trello.trello.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trello.trello.model.Users;
import com.trello.trello.repository.UserRepository;
import com.trello.trello.security.UserTokenDecoderService;
import com.trello.trello.service.UserService;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private UserTokenDecoderService userTokenDecoderService;

    @GetMapping("/")
    List<Users> getAllUsers() {
        String userId = userTokenDecoderService.getUserId();
        Users user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            List<String> memberIds = user.getMembers();

            List<Users> allUsers = userRepository.findAll();

            List<Users> result = allUsers.stream()
                    .map(u -> memberIds.contains(u.getId()) ? u
                            : userService.maskEmails(Collections.singletonList(u)).get(0))
                    .collect(Collectors.toList());

            return result;
        }
        return Collections.emptyList();
    }

    @GetMapping("/{id}")
    Users getUser(@PathVariable String id) {
        return userRepository.findById(id).orElse(null);
    }

    @GetMapping("/members/{id}")
    List<Users> getMembers(@PathVariable String id) {
        Optional<Users> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            List<String> memberIds = user.getMembers();
            List<Users> members = userRepository.findByIdIn(memberIds);
            return members;
        } else {
            return Collections.emptyList();
        }
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
            if (updateUser.getNotifications() != null) {
                oldUser.setNotifications(updateUser.getNotifications());
            }
            if (updateUser.getMembers() != null) {
                oldUser.setMembers(updateUser.getMembers());
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

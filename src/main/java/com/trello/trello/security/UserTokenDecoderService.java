package com.trello.trello.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.trello.trello.model.Users;
import com.trello.trello.repository.UserRepository;

import java.util.Map;
import java.util.Optional;

@Service
public class UserTokenDecoderService {
    @Autowired
    private UserRepository userRepository;

    public String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;
            Map<String, Object> claims = jwt.getClaims();
            Optional<Users> userOptional = userRepository.findByEmail((String) claims.get("email"));
            return userOptional.map(Users::getId).orElse(null);
        }
        return null;
    }
}
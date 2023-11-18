package com.trello.trello.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trello.trello.model.Users;

public interface UserRepository extends MongoRepository<Users, String> {
    boolean existsByEmail(String email);

    Optional<Users> findByEmail(String email);

}

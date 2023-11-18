package com.trello.trello.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trello.trello.model.Projects;

public interface ProjectRepository extends MongoRepository<Projects, String> {

}

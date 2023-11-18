package com.trello.trello.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trello.trello.model.Issues;

public interface IssueRepository extends MongoRepository<Issues, String> {

}

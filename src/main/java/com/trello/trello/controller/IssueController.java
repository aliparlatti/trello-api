package com.trello.trello.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.trello.trello.model.Issues;
import com.trello.trello.repository.IssueRepository;

@RestController
@CrossOrigin
@RequestMapping("/issues")
public class IssueController {
    @Autowired
    private IssueRepository issueRepository;

    @GetMapping("/")
    List<Issues> getAllIssue() {
        return issueRepository.findAll();
    }

    @GetMapping("/{id}")
    Issues getIssue(@PathVariable String id) {
        return issueRepository.findById(id).orElse(null);
    }

    @PostMapping("/")
    Issues createIssue(@RequestBody Issues issue) {
        return issueRepository.save(issue);
    }

    @PatchMapping("/{id}")
    public Issues updateIssue(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        Issues oldIssue = issueRepository.findById(id).orElse(null);

        if (oldIssue != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            Issues updatedIssue = objectMapper.convertValue(updates, Issues.class);

            if (updatedIssue.getTitle() != null) {
                oldIssue.setTitle(updatedIssue.getTitle());
            }

            if (updatedIssue.getStatus() != null) {
                oldIssue.setStatus(updatedIssue.getStatus());
            }

            if (updatedIssue.getAssignee() != null) {
                oldIssue.setAssignee(updatedIssue.getAssignee());
            }

            if (updatedIssue.getUser() != null) {
                oldIssue.setUser(updatedIssue.getUser());
            }

            if (updatedIssue.getAttachments() != null) {
                oldIssue.setAttachments(updatedIssue.getAttachments());
            }

            if (updatedIssue.getMembers() != null) {
                oldIssue.setMembers(updatedIssue.getMembers());
            }

            if (updatedIssue.getCreatedDate() != null) {
                oldIssue.setCreatedDate(updatedIssue.getCreatedDate());
            }

            oldIssue.setArchived(updatedIssue.isArchived());

            if (updatedIssue.getCover() != null) {
                oldIssue.setCover(updatedIssue.getCover());
            }

            if (updatedIssue.getDueDate() != null) {
                oldIssue.setDueDate(updatedIssue.getDueDate());
            }

            if (updatedIssue.getDescription() != null) {
                oldIssue.setDescription(updatedIssue.getDescription());
            }

            if (updatedIssue.getCheckLists() != null) {
                oldIssue.setCheckLists(updatedIssue.getCheckLists());
            }

            return issueRepository.save(oldIssue);
        } else {
            throw new RuntimeException("Issue bulunamadı.");
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteIssue(@PathVariable String id) {
        issueRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}

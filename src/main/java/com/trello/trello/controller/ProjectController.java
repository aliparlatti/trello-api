package com.trello.trello.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.trello.trello.model.Projects;
import com.trello.trello.model.Users;
import com.trello.trello.repository.ProjectRepository;
import com.trello.trello.repository.UserRepository;
import com.trello.trello.security.UserTokenDecoderService;

@RestController
@CrossOrigin
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTokenDecoderService userTokenDecoderService;

    @GetMapping("/")
    List<Projects> getAllProject() {
        Optional<Users> userOptional = userRepository.findById(userTokenDecoderService.getUserId());
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            if (user.getProjects() != null) {
                List<String> projectIds = user.getProjects();
                List<Projects> allProjects = projectRepository.findAll();
                List<Projects> userProjects = allProjects.stream()
                        .filter(project -> projectIds.contains(project.getId()))
                        .collect(Collectors.toList());
                return userProjects;
            } else {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
    }

    @GetMapping("/{id}")
    Projects getProjectById(@PathVariable String id) {
        return projectRepository.findById(id).orElse(null);
    }

    @PostMapping("/")
    Projects createProject(@RequestBody Projects project) {
        return projectRepository.save(project);
    }

    @PatchMapping("/{id}")
    public Projects updateProject(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        Projects oldProject = projectRepository.findById(id).orElse(null);

        if (oldProject != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            Projects updatedProject = objectMapper.convertValue(updates, Projects.class);

            if (updatedProject.getTitle() != null) {
                oldProject.setTitle(updatedProject.getTitle());
            }

            oldProject.setActive(updatedProject.isActive());

            if (updatedProject.getAssigned() != null) {
                oldProject.setAssigned(updatedProject.getAssigned());
            }

            if (updatedProject.getIssues() != null) {
                oldProject.setIssues(updatedProject.getIssues());
            }

            if (updatedProject.getTheme() != null) {
                oldProject.setTheme(updatedProject.getTheme());
            }

            return projectRepository.save(oldProject);
        } else {
            throw new RuntimeException("Proje bulunamadı.");
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Object> deleteProject(@PathVariable String id) {
        projectRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}

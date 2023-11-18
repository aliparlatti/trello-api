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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trello.trello.model.Projects;
import com.trello.trello.repository.ProjectRepository;

@RestController
@CrossOrigin
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping("/")
    List<Projects> getAllProject() {
        return projectRepository.findAll();
    }

    @GetMapping("/{id}")
    Projects getProject(@PathVariable String id) {
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
    String deleteProject(@PathVariable String id) {
        projectRepository.deleteById(id);
        return id;
    }
}

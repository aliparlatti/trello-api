package com.trello.trello.model;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    @Id
    public String id;
    public String name;
    public String avatar;
    public String provider;
    public String username;
    public String email;
    private List<String> projects;
}

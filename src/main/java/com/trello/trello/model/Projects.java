package com.trello.trello.model;

import lombok.Data;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Data
public class Projects {
    @Id
    private String id;
    private boolean active;
    private String title;
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId assigned;
    private List<String> issues;
    private Theme theme;

    @Data
    public static class Theme {
        private Long id;
        private String color;
        private String topBar;
        private String secondThemeColor;
        private String light;
        private String extraLight;
        private String border;
    }
}

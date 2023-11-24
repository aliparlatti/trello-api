package com.trello.trello.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

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
    public List<String> projects;
    public List<Notification> notifications;
    public List<String> members;

    @Data
    public static class Notification {

        @JsonSerialize(using = ToStringSerializer.class)
        public ObjectId senderUser;
        public String type;
        public String senderUserName;
        public Boolean status;
    }
}

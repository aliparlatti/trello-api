package com.trello.trello.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Data
public class Issues {
    @Id
    private String id;
    private String title;
    private String status;
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId assignee;
    private Users user;
    private List<Attachment> attachments;
    private List<Users> members;
    private Date createdDate;
    private boolean archived;
    private Cover cover;
    private Date dueDate;
    private String description;
    private List<CheckList> checkLists;

    @Data
    public static class Attachment {
        private String id;
        private String filename;
        private String url;
        private Date uploadDate;
        private Users user;
        private String comment;
        private List<Emoji> emojies;
    }

    @Data
    public static class Emoji {
        private String emoji;
        private int count;
    }

    @Data
    public static class CheckList {
        private String title;
        private List<CheckListData> data;
    }

    @Data
    public static class CheckListData {
        private String name;
        private boolean checked;
    }

    @Data
    public static class Cover {
        private String type;
        private String value;
    }
}

package com.example.TaskManagementSystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @ManyToOne
    private User author;
    @ManyToOne
    private User assignee;
    @OneToMany(mappedBy = "task")
    private List<Comment> comments;

    public enum Status {
        PENDING, IN_PROGRESS, COMPLETED
    }

    public enum Priority {
        HIGH, MIDDLE, LOW
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }
}

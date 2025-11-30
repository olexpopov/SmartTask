package com.proj.Management.models;

import jakarta.persistence.*;
import lombok.Data; // Provides Getters, Setters, toString, equals, and hashCode
import lombok.NoArgsConstructor; // REQUIRED for JPA
import lombok.AllArgsConstructor; // Recommended for Hibernate reflection

import java.time.Instant;

@Data // Provides all accessors and utility methods
@NoArgsConstructor // REQUIRED: Generates the no-argument constructor for JPA
@AllArgsConstructor // Generates a constructor with all fields for Hibernate/Lombok
@Entity
@Table(name = "comments") // Maps to the plural table name
public class Comments { // Renamed class to singular: Comment

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Instant getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Instant commentTime) {
        this.commentTime = commentTime;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // --- Relationships ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // --- Fields ---
    @Column(name = "comment_text", columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(name = "comment_time", nullable = false)
    private Instant commentTime = Instant.now();


}
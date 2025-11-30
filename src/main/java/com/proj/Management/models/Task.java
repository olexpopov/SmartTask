package com.proj.Management.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import java.time.Instant;
@AllArgsConstructor
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Task_id")
    private Long id;

    @Column(name = "title",length =200, nullable=false)
    private String title;

    @Column(name = "status",length =20,nullable=false)
    private String  status="Open";

    @Column(name = "description",nullable=false)
    private String description;

    @Column(name = "priority",length =20,nullable=false)
    private String priority="Low";

    @Column(name = "due_date",nullable=false)
    private Instant  due_to=Instant.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User assignedUser;

    public Task() {}

    public Task(String title, String description, Project project, User assignedUser,String priority) {
        this.title = title;
        this.description = description;
        this.project = project;
        this.assignedUser = assignedUser;
        this.priority=priority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Instant getDueDate() {
        return due_to;
    }

    public void setDueDate(Instant dueDate) {
        this.due_to = dueDate;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }
}

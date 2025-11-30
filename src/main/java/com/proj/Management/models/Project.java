package com.proj.Management.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

import java.util.List;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

@Entity
@Data
@NoArgsConstructor // Lombok annotation for the required no-arg constructor
@AllArgsConstructor // Lombok annotation for the constructor with ALL fields (required by Hibernate for reflection)
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @OneToMany(mappedBy = "project")
    @JsonManagedReference
    private List<ProjectMember> members;



    @Column(name = "project_name", length = 100, nullable = false)
    private String projectName;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private User owner;





    @Column(name = "date_of_creation", nullable = false)
    private Instant dateOfCreation = Instant.now();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Instant getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Instant dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Project( User owner,String projectName,String description) {
       this.projectName = projectName;
        this.description = description;
        this.owner = owner;
    }
}

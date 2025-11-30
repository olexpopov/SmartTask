package com.proj.Management.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})


@Entity
@Table(name = "project_members", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"project_id", "user_id"})
})

public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;


    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Column(name = "user_role", length = 20, nullable = false)
    private String userRole = "MEMBER";

    @Column(name = "date_of_join", nullable = false)
    private Instant dateOfJoin = Instant.now();


    public ProjectMember() {
    }


    public ProjectMember(Project project, User user, String userRole) {
        this.project = project;
        this.user = user;
        this.userRole = userRole;
    }



    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Instant getDateOfJoin() {
        return dateOfJoin;
    }

    public void setDateOfJoin(Instant dateOfJoin) {
        this.dateOfJoin = dateOfJoin;
    }
}

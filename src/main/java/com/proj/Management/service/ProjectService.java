package com.proj.Management.service;

import com.proj.Management.models.Project;
import com.proj.Management.models.ProjectMember;
import com.proj.Management.models.User;
import com.proj.Management.repo.ProjectMemberRepo;
import com.proj.Management.repo.ProjectRepo;
import com.proj.Management.repo.UserRepo;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;


@Service
public class ProjectService {
    private final ProjectRepo projectRepo;
    private final UserRepo userRepo;
    private final ProjectMemberRepo projectMemberRepo;

    public ProjectService(ProjectRepo projectRepo, UserRepo userRepo,ProjectMemberRepo projectMemberRepo) {
        this.projectRepo = projectRepo;
        this.userRepo = userRepo;
        this.projectMemberRepo=projectMemberRepo;
    }


    @Transactional // CRITICAL FIX: Keeps the DB session open during the entire method execution.
    public List<Project> findProjectsByUserEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in database."));

        List<Project> projects = projectRepo.findAllUserProjects(user.getId());

        return projects;
    }
    public Project createProjectByEmail(String projectName, String description) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();


        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


        Project project = new Project(user,projectName, description);


        return projectRepo.save(project);
    }
    public Project getProjectById(long id)
    {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user=userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Project project = projectRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Project not found with ID: " + id
                ));

        boolean isOwner = projectRepo.findByIdAndOwnerId(id, user.getId()).isPresent();


        boolean isMember = projectMemberRepo.findByProjectIdAndUserId(id, user.getId()).isPresent();


        if (!isOwner && !isMember) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied. You are not a member of this project."
            );
        }

        return project;
    }
    @Transactional
    public Project updateProjectById(String name,String description,long id)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user=userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Project project=projectRepo.findByIdAndOwnerId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Project not found or you do not have access"));
        project.setProjectName(name);
        project.setDescription(description);

        return projectRepo.save(project);



    }
    public void deleteProjectById(long id)
    {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user=userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Project p = projectRepo.findByIdAndOwnerId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Project not found or you do not have access"));


        projectRepo.delete(p);


    }



}

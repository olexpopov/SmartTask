package com.proj.Management.service;

import com.proj.Management.ProjectMemberDTO;
import com.proj.Management.models.Project;
import com.proj.Management.models.ProjectMember;
import com.proj.Management.models.User;
import com.proj.Management.repo.ProjectMemberRepo;
import com.proj.Management.repo.ProjectRepo;
import com.proj.Management.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectMemberService {


    private final ProjectRepo projectRepo;
    private final UserRepo userRepo;
    private final ProjectMemberRepo projectMemberRepo;

    public ProjectMemberService(ProjectRepo projectRepo, UserRepo userRepo, ProjectMemberRepo projectMemberRepo) {
        this.projectRepo = projectRepo;
        this.userRepo = userRepo;
        this.projectMemberRepo = projectMemberRepo;
    }


    @Transactional
    public List<ProjectMemberDTO> getProjectMembers(Long projectId, String authenticatedEmail) {

        // 1. Authenticate user
        User authUser = userRepo.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found."));

        // 2. Load project
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found."));

        // 3. Permission check (Owner or Member)
        boolean isOwner = project.getOwner().getId().equals(authUser.getId());
        boolean isMember = projectMemberRepo.findByProjectIdAndUserId(projectId, authUser.getId()).isPresent();

        if (!isOwner && !isMember) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.");
        }

        // 4. Fetch real project members
        List<ProjectMember> realMembers = projectMemberRepo.findByProjectId(projectId);

        // 5. Convert real members to DTO
        List<ProjectMemberDTO> dtoList = realMembers.stream()
                .map(m -> new ProjectMemberDTO(
                        m.getUser().getId(),
                        m.getUser().getFirstName(),
                        m.getUser().getLastName(),
                        m.getUser().getEmail(),
                        m.getUser().getUserRole(),
                        m.getUserRole(),
                        LocalDateTime.ofInstant(m.getDateOfJoin(), ZoneId.systemDefault())
                ))
                .collect(Collectors.toList());

        // 6. Ensure OWNER is included only once
        Long ownerId = project.getOwner().getId();
        boolean ownerExists = dtoList.stream().anyMatch(m -> m.userId().equals(ownerId));

        if (!ownerExists) {
            User owner = project.getOwner();
            dtoList.add(0, new ProjectMemberDTO(
                    owner.getId(),
                    owner.getFirstName(),
                    owner.getLastName(),
                    owner.getEmail(),
                    owner.getUserRole(),
                    "OWNER",
                    LocalDateTime.ofInstant(project.getDateOfCreation(), ZoneId.systemDefault())
            ));
        }

        return dtoList;
    }





    public ProjectMember addProjectMember(long id, String inviterEmail, String email) {
        User inviter = userRepo.findByEmail(inviterEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Inviter user not found."));
        Project project = projectRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found."));
        User user2 = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


        boolean isOwner = project.getOwner().getId().equals(inviter.getId());
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the project owner can invite members.");
        }
        if (projectMemberRepo.findByProjectIdAndUserId(id, user2.getId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already a member of this project.");
        }
        ProjectMember newMembership = new ProjectMember(project, user2, "MEMBER");

        return projectMemberRepo.save(newMembership);
    }

    public void deleteProjectMember(long id, String deleterEmail, Long delete) {
        User deleter = userRepo.findByEmail(deleterEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Inviter user not found."));
        Project project = projectRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found."));
        User user2 = userRepo.findById(delete)
                .orElseThrow(() -> new RuntimeException("User not found"));


        boolean isOwner = project.getOwner().getId().equals(deleter.getId());
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the project owner can invite members.");
        }
        if (projectMemberRepo.findByProjectIdAndUserId(id, user2.getId()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "no such user.");
        }

        Optional<ProjectMember> p = projectMemberRepo.findByProjectIdAndUserId(id, delete);
        ProjectMember membershipRecord = p
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not a member of this project."));
        projectMemberRepo.delete(membershipRecord);
    }
    @Transactional
    public void updateProjectOwner(Long projectId, String inviterEmail, Long newOwnerId) {
        User currentOwner = userRepo.findByEmail(inviterEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current owner not found."));
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found."));
        User newOwner = userRepo.findById(newOwnerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "New owner user not found."));

        boolean isCurrentOwner = project.getOwner().getId().equals(currentOwner.getId());

        if (!isCurrentOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the project owner can transfer project ownership.");
        }


        project.setOwner(newOwner);
        projectRepo.save(project);

        if (projectMemberRepo.findByProjectIdAndUserId(projectId, currentOwner.getId()).isEmpty()) {
            ProjectMember oldOwnerMembership = new ProjectMember(
                    project,
                    currentOwner,
                    "MEMBER"
            );
            projectMemberRepo.save(oldOwnerMembership);
        }


    }

}

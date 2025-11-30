package com.proj.Management.service;

import com.proj.Management.models.Comments;
import com.proj.Management.models.Project;
import com.proj.Management.models.Task;
import com.proj.Management.models.User;
import com.proj.Management.repo.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
@Service
public class CommentsService {

    private final ProjectRepo projectRepo;
    private final UserRepo userRepo;
    private final ProjectMemberRepo projectMemberRepo;
    private final TaskRepo taskRepo;
    private final CommentsRepo commentsRepo;


    public CommentsService(ProjectRepo projectRepo, UserRepo userRepo, ProjectMemberRepo projectMemberRepo, TaskRepo taskRepo,CommentsRepo commentsRepo) {
        this.projectRepo = projectRepo;
        this.userRepo = userRepo;
        this.projectMemberRepo = projectMemberRepo;
        this.taskRepo = taskRepo;
        this.commentsRepo=commentsRepo;
    }
    public List<Comments> getCommentsById(long id)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Task task =taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Project p=task.getProject();
        boolean isOwner = p.getOwner().getId().equals(user.getId());
        boolean isMember= projectMemberRepo.findByProjectIdAndUserId(p.getId(), user.getId()).isPresent();
        if(!isMember&&!isOwner)
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. ");
        }

        return commentsRepo.findByTaskId(id);


    }
    public void deleteCommentsById(long id)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Comments comment=commentsRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found with ID: " + id));
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        boolean isAuthor=comment.getUser().getId().equals(user.getId());
        if(!isAuthor)
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. ");
        }
        commentsRepo.delete(comment);
    }
}

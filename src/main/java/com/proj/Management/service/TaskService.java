package com.proj.Management.service;

import com.proj.Management.models.Project;
import com.proj.Management.models.ProjectMember;
import com.proj.Management.models.Task;
import com.proj.Management.models.User;
import com.proj.Management.repo.ProjectMemberRepo;
import com.proj.Management.repo.ProjectRepo;
import com.proj.Management.repo.TaskRepo;
import com.proj.Management.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service

public class TaskService {


        private final ProjectRepo projectRepo;
        private final UserRepo userRepo;
        private final ProjectMemberRepo projectMemberRepo;
        private final TaskRepo taskRepo;


    public TaskService(ProjectRepo projectRepo, UserRepo userRepo, ProjectMemberRepo projectMemberRepo, TaskRepo taskRepo) {
        this.projectRepo = projectRepo;
        this.userRepo = userRepo;
        this.projectMemberRepo = projectMemberRepo;
        this.taskRepo = taskRepo;
    }
    public List<Task> getAuthenticatedUserTasks()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return taskRepo.findByAssignedUserId(user.getId());

    }
    public List<Task> getProjectTasks(long id)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        boolean isOwner = projectRepo.findByIdAndOwnerId(id, user.getId()).isPresent();
        boolean isMember = projectMemberRepo.findByProjectIdAndUserId(id, user.getId()).isPresent();
        if(!isOwner&&!isMember)
        {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied. You are not a member of this project."
            );
        }
        List<Task> taskList =taskRepo.findByProjectId(id);
        return taskList;
    }
    @Transactional
    public Task addTask(long projectId, String emailTask, String description, String title,String priority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User owner = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found"));
        User assignee = userRepo.findByEmail(emailTask)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assigned user not found"));
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (!project.getOwner().getId().equals(owner.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. You are not an owner of this project.");
        }

        boolean isMember = projectMemberRepo.findByProjectIdAndUserId(projectId, assignee.getId()).isPresent();
        if (!isMember) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assigned user must be a member of the project.");
        }

        Task task = new Task(title, description, project, assignee,priority);
        return taskRepo.save(task);
    }
public void deleteTaskByid(long id)
{
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();

    User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found"));

    Task task = taskRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

    Project p=projectRepo.findById(task.getProject().getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
    boolean  isOwner = projectRepo.findByIdAndOwnerId(p.getId(), user.getId()).isPresent();
    if (!isOwner) { // Assuming, for simplicity, only the owner can delete/update tasks for now.
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. Only the project owner can modify this task.");
    }
   taskRepo.delete(task);


}
    public void updateTaskByid (long id,String status)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found"));

        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        Project p=projectRepo.findById(task.getProject().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        boolean isOwner = p.getOwner().getId().equals(user.getId());
        boolean isAssignedUser = task.getAssignedUser().getId().equals(user.getId());
        if (!isOwner && !isAssignedUser) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Access denied. Only the task owner or assigned user may update.");
        }
        task.setStatus(status);
        taskRepo.save(task);
    }
}

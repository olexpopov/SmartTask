package com.proj.Management.controllers;

import com.proj.Management.TaskRequest;
import com.proj.Management.TaskStatusDTO;
import com.proj.Management.models.Comments;
import com.proj.Management.models.Project;
import com.proj.Management.models.Task;
import com.proj.Management.service.CommentsService;
import com.proj.Management.service.ProjectMemberService;
import com.proj.Management.service.ProjectService;
import com.proj.Management.service.TaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
@SecurityRequirement(name = "bearerAuth")
public class CommentsController {
    private final ProjectService projectService;
    private final ProjectMemberService projectMemberService;
    private final TaskService taskService;
    private final CommentsService commentsService;

    public CommentsController(ProjectService projectService, ProjectMemberService projectMemberService, TaskService taskService, CommentsService commentsService) {
        this.projectService = projectService;
        this.projectMemberService = projectMemberService;
        this.taskService = taskService;
        this.commentsService = commentsService;
    }
    @DeleteMapping("/{id}")
    public  ResponseEntity<String> deleteComment(@PathVariable long id) {
        commentsService.deleteCommentsById(id);
        return ResponseEntity.ok("Comment deleted");
    }
}

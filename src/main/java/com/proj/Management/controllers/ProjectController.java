package com.proj.Management.controllers;

import com.proj.Management.DeleteMemberRequest;
import com.proj.Management.ProjectMemberDTO;
import com.proj.Management.TaskRequest;
import com.proj.Management.models.Project;
import com.proj.Management.models.ProjectMember;
import com.proj.Management.models.Task;
import com.proj.Management.models.User;
import com.proj.Management.service.ProjectMemberService;
import com.proj.Management.service.ProjectService;
import com.proj.Management.service.TaskService;
import com.proj.Management.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
@SecurityRequirement(name = "bearerAuth")
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectMemberService projectMemberService;
    private final TaskService taskService;

    public ProjectController(ProjectService projectService,ProjectMemberService projectMemberService,TaskService taskService) {
        this.projectService = projectService;
        this.projectMemberService = projectMemberService;
        this.taskService = taskService;
    }

    @GetMapping ("")
    public List<Project> userProjects()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedEmail = authentication.getName();

        List<Project> projects = projectService.findProjectsByUserEmail(authenticatedEmail);

//        return projectService.findAllProjects();
        return projects;
    }
    @PostMapping ("")
    public Project userProject(@RequestBody Map<String, String> request)
    {
        String projectName = request.get("projectName");
        String description = request.get("description");
        return  projectService.createProjectByEmail(projectName, description);
    }
    @GetMapping ("/{id}")
    public Project getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }
    @PutMapping  ("/{id}")
    public Project updateProjectById(@RequestBody Map<String, String> request, @PathVariable Long id) {
        String name = request.get("projectName");
        String desc = request.get("description");
       return projectService.updateProjectById(name,desc,id);

    }
    @DeleteMapping   ("/{id}")
    public ResponseEntity<String> deleteProjectById( @PathVariable Long id) {
        projectService.deleteProjectById(id);
        return ResponseEntity.ok("Project deleted successfully.");
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<ProjectMemberDTO>> getMembers(@PathVariable Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(projectMemberService.getProjectMembers(id, email)); // <- method name fixed
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<ProjectMember> addProjectMembers(@PathVariable Long id,@RequestBody Map<String,String> request){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String inviterEmail = authentication.getName();
        String email = request.get("email");

        ProjectMember newMember = projectMemberService.addProjectMember(id, inviterEmail, email);

        return new ResponseEntity<>(newMember, HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}/members")
    public ResponseEntity<String> deleteProjectMembers(@PathVariable Long id, @RequestBody DeleteMemberRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String Email = authentication.getName();
        Long memberId = request.getId();

        projectMemberService.deleteProjectMember(id, Email, memberId);

        return ResponseEntity.ok("ProjectMember deleted successfully.");
    }
    @PutMapping("/{id}/members")
    public ResponseEntity<String> updateProjectMembers(@PathVariable Long id, @RequestBody DeleteMemberRequest request)
    {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String Email = authentication.getName();
        Long memberId= request.getId();

        projectMemberService.updateProjectOwner(id, Email, memberId);
        return ResponseEntity.ok("New owner added succesfully.");


    }
    @PostMapping("/{id}/members/tasks")
    public ResponseEntity<Task> addTaskForUser(@PathVariable long id, @RequestBody TaskRequest request) {
        Task task = taskService.addTask(
                id,
                request.getEmail(),       // assigned user
                request.getDescription(), // task description
                request.getTitle(),
                request.getPriority()
        );
        return ResponseEntity.ok(task);
    }
    @GetMapping("/{id}/tasks")
    public List<Task> getTasksByProjectId(@PathVariable long id)
    {
        return taskService.getProjectTasks(id);
    }


}

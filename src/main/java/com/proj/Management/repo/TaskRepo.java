package com.proj.Management.repo;

import com.proj.Management.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepo extends JpaRepository<Task, Long> {

    // The correct method to find tasks by the user they are assigned to.
    List<Task> findByAssignedUserId(Long assignedUserId);

    // The correct method to find tasks belonging to a specific project.
    List<Task> findByProjectId(Long projectId);
    // Inside TaskRepo.java (This method is automatically available!)


    // Ensure the old, incorrect findByUserId is DELETED from this file.
}
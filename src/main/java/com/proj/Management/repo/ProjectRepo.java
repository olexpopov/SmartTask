package com.proj.Management.repo;

import com.proj.Management.ProjectDTO;
import com.proj.Management.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Long> {
    List<Project> findByOwnerId(Long ownerId);

    Optional<Project> findByIdAndOwnerId(Long id, Long ownerId);
    @Query("SELECT p FROM Project p " +
            "LEFT JOIN ProjectMember pm ON pm.project = p " + // Join the join table
            "WHERE p.owner.id = :userId OR pm.user.id = :userId") // Check Owner OR Member
    List<Project> findAllUserProjects(@Param("userId") Long userId);
}
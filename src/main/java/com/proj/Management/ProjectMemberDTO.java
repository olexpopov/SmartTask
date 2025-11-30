package com.proj.Management;

import java.time.LocalDateTime;

/**
 * DTO for returning project member information safely.
 * Avoids exposing JPA entities directly and prevents circular JSON issues.
 */
public record ProjectMemberDTO(
        Long userId,
        String firstName,
        String lastName,
        String email,
        String userRole,     // Role of the user in the system (e.g., USER, ADMIN)
        String roleInProject, // Role of the user in this project (e.g., OWNER, MEMBER)
        LocalDateTime dateOfJoin
) {}

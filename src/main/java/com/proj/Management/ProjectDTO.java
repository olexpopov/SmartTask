package com.proj.Management;

import lombok.Data;

import java.util.List;

@Data
public class ProjectDTO {
    private Long id;
    private String projectName;
    private String description;
    private List<ProjectMemberDTO> members;
}
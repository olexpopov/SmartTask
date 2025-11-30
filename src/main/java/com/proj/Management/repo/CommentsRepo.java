package com.proj.Management.repo;

import com.proj.Management.models.Comments;
import com.proj.Management.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentsRepo extends JpaRepository<Comments, Long> {
    List<Comments> findByTaskId(long task_id);
}
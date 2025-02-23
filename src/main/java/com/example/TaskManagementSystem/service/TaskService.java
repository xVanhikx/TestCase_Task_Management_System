package com.example.TaskManagementSystem.service;

import com.example.TaskManagementSystem.entity.Task;
import com.example.TaskManagementSystem.repository.TaskRepository;
import com.example.TaskManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.TaskManagementSystem.entity.Task.*;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    public Task createTask(Task task) {
        task.setStatus(Status.PENDING);
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task taskDetails) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setStatus(taskDetails.getStatus());
        task.setPriority(taskDetails.getPriority());
        task.setAssignee(taskDetails.getAssignee());
        task.setComments(taskDetails.getComments());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Task updateTaskStatus(Long id, String status) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        Status statusEnum;
        try {
            statusEnum = Status.valueOf(status);
        } catch (Exception e) {
            throw new RuntimeException("Incorrect status");
        }
        task.setStatus(statusEnum);
        return taskRepository.save(task);
    }

    public Task updateTaskStatusByAssignee(Long id, String status) {
        String assignee = SecurityContextHolder.getContext().getAuthentication().getName();
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        if (!task.getAssignee().getEmail().equals(assignee)) {
            throw new RuntimeException("You cant change other task");
        }
        Status statusEnum;
        try {
            statusEnum = Status.valueOf(status);
        } catch (Exception e) {
            throw new RuntimeException("Incorrect status");
        }
        task.setStatus(statusEnum);
        return taskRepository.save(task);
    }

    public Page<Task> getTasksByAuthor(Long authorId, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return taskRepository.findByAuthorId(authorId, pageable);
    }

    public Page<Task> getTasksByAssignee(Long assigneeId, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return taskRepository.findByAssigneeId(assigneeId, pageable);
    }

}

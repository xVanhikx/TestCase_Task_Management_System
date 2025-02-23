package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.entity.Task;
import com.example.TaskManagementSystem.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    TaskService taskService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @PutMapping("/{task_id}")
    @PreAuthorize("hasRole('ADMIN)")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @PathVariable Task task) {
        Task updatedTask = taskService.updateTask(taskId, task);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{task_id}/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Task> updateStatus(@PathVariable Long taskId, @PathVariable String status) {
        Task task = taskService.updateTaskStatus(taskId, status);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/user/{task_id}/{status}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Task> updateStatusByAssignee(@PathVariable Long taskId, @PathVariable String status) {
        Task task = taskService.updateTaskStatusByAssignee(taskId, status);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<Page<Task>> getTasksForAuthor(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy) {
        Page<Task> tasks = taskService.getTasksByAuthor(authorId, page, size, sortBy);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<Page<Task>> getTasksForAssignee(
            @PathVariable Long assigneeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy) {
        Page<Task> tasks = taskService.getTasksByAssignee(assigneeId, page, size, sortBy);
        return ResponseEntity.ok(tasks);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return "Task was deleted";
    }
}

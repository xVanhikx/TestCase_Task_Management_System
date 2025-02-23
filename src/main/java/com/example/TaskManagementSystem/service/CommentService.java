package com.example.TaskManagementSystem.service;

import com.example.TaskManagementSystem.entity.Comment;
import com.example.TaskManagementSystem.entity.Task;
import com.example.TaskManagementSystem.entity.User;
import com.example.TaskManagementSystem.repository.CommentRepository;
import com.example.TaskManagementSystem.repository.TaskRepository;
import com.example.TaskManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    public Comment addComment(Long taskId, String text) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

        String authorName = SecurityContextHolder.getContext().getAuthentication().getName();

        User author = userRepository.findByEmail(authorName).orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment =  new Comment();
        comment.setText(text);
        comment.setTask(task);
        comment.setAuthor(author);

        return commentRepository.save(comment);
    }

    public Comment addCommentByAssignee(Long taskId, String text) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

        String authorName = SecurityContextHolder.getContext().getAuthentication().getName();
        User author = userRepository.findByEmail(authorName).orElseThrow(() -> new RuntimeException("User not found"));

        if (!task.getAssignee().equals(author)) {
            throw new RuntimeException("You cant change status this task");
        }
        Comment comment = new Comment();
        comment.setText(text);
        comment.setAuthor(author);
        comment.setTask(task);

        return commentRepository.save(comment);
    }
}

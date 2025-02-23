package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.entity.Comment;
import com.example.TaskManagementSystem.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Operation(summary = "Добавление комментария администратором",
                description = "Добавляет комментарий к задаче. Требует ввести id задачи и содержание комментария.",
                parameters = {
                        @Parameter(name = "taskId", description = "ID задачи, к которой добавляется комментарий", required = true),
                        @Parameter(name = "text", description = "Содержание комментария", required = true)
                },
                responses = {
                        @ApiResponse(responseCode = "201", description = "Комментарий успешно добавлен"),
                        @ApiResponse(responseCode = "400", description = "Ошибка при добавлении комментария")
                })
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Comment> addComment(
            @RequestParam @NotNull Long taskId,
            @RequestParam @NotNull String text) {
        Comment comment = commentService.addComment(taskId, text);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @Operation(summary = "Добавление комментария исполнителем",
                description = "Добавляет комментарий к задаче. Требует ввести id задачи и содержание комментария.",
            parameters = {
                    @Parameter(name = "taskId", description = "ID задачи, к которой добавляется комментарий", required = true),
                    @Parameter(name = "text", description = "Содержание комментария", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "201", description = "Комментарий успешно добавлен"),
                    @ApiResponse(responseCode = "400", description = "Ошибка при добавлении комментария")
            })
    @PostMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Comment> addCommentAssignee(
            @RequestParam @NotNull Long taskId,
            @RequestParam @NotNull String text) {
        Comment comment = commentService.addCommentByAssignee(taskId, text);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

}

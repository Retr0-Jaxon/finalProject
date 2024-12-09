package org.example.finalproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.finalproject.model.Task;

import org.example.finalproject.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;


// ... 其他导入 ...

/**
 * TaskController
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");


    @Autowired
    private TaskService taskService;

    /**
     * Create a new task
     *
     * @param task the task data from the frontend
     * @return ResponseEntity with the created task
     */
    @PostMapping("/createTask")
    public ResponseEntity<Task> createTask(@RequestBody Task task) throws IOException {
        Task createdTask = taskService.createTask_Local(task);
        return ResponseEntity.ok(createdTask);
    }

    

    // 处理验证错误
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder("Validation errors: ");
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ")
        );
        return ResponseEntity.badRequest().body(errors.toString());
    }

    @DeleteMapping("/{index_Id}/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable String index_Id, @PathVariable Long id) {
        try {
            taskService.deleteTaskByIdFromJson(id, index_Id);
            return ResponseEntity.ok("Task deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete task: " + e.getMessage());
        }
    }

    /**
     * Update an existing task by ID
     *
     * @param updatedTask the new task data
     * @return ResponseEntity with the updated task
     */
    @PostMapping("/update")
    public ResponseEntity<Task> updateTask(@RequestBody Task updatedTask) {
        Task task = taskService.updateTask(updatedTask.getId(), updatedTask);
        return ResponseEntity.ok(task);
    }

//    @PostMapping("/sync")
//    public ResponseEntity<String> syncIndex(@RequestBody String index_Id) {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.registerModule(new JavaTimeModule());
//            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//            List<Task> tasks = taskService.syncIndex(index_Id);
//            String json = objectMapper.writeValueAsString(tasks);
//            return ResponseEntity.ok(json);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to sync task: " + e.getMessage());
//        }
//    }
}
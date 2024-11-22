package org.example.finalproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;
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
    @PostMapping("/createTasks")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
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

    @DeleteMapping("/{task_Id}/{index_Id}")
    public ResponseEntity<String> deleteTask(@PathVariable String task_Id, @PathVariable String index_Id) {
        try {
            taskService.deleteTaskByIdFromJson(task_Id, index_Id);
            return ResponseEntity.ok("Task deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete task: " + e.getMessage());
        }
    }

    @PostMapping("/sync")
    public ResponseEntity<String> syncIndex(@RequestBody String index_Id) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            List<Task> tasks = taskService.syncIndex(index_Id);
            String json = objectMapper.writeValueAsString(tasks);
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to sync task: " + e.getMessage());
        }
    }
}
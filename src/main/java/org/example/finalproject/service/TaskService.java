package org.example.finalproject.service;

import org.example.finalproject.model.Task;
//import org.example.finalproject.service.IndexService;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * TaskService
 */
@Service
public class TaskService {
    /**
     * Create a task from frontend data
     *
     * 
     */
    public Task createTask_Local(Task task) throws IOException {
        // 数据验证逻辑
        if (task == null || task.gettask_Id() == null || task.gettask_Id().isEmpty()) {
            throw new IllegalArgumentException("Task name cannot be null or empty");
        }

        // 检查是否存在重名的task_Id
        checkDuplicateTaskId(task);

        // 使用任务的index_Id作为文件名
        String fileName = task.getindex_Id() + ".json";

        // 将任务对象转换为JSON字符串并保存到本地文件
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        List<Task> tasks;

        // 检查文件是否存在
        if (Files.exists(Paths.get("data", fileName)) && Files.size(Paths.get("data", fileName)) > 0) {
            try {
                // 读取现有任务
                Task[] existingTasks = objectMapper.readValue(new File("data", fileName), Task[].class);
                tasks = new ArrayList<>(List.of(existingTasks));
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to read existing tasks", e);
            }
        } else {
            tasks = new ArrayList<>();
        }

        // 添加新任务
        tasks.add(task);

        // 写回JSON文件
        try {
            objectMapper.writeValue(new File("data", fileName), tasks);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save task as JSON", e);
        }

        return task;
    }

    private void checkDuplicateTaskId(Task task) throws IOException {
        String fileName = task.getindex_Id() + ".json";
        if (Files.exists(Paths.get(fileName)) && Files.size(Paths.get(fileName)) > 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            try {
                List<Task> existingTasks = List.of(objectMapper.readValue(new File("data",fileName), Task[].class));
                for (Task existingTask : existingTasks) {
                    if (existingTask.gettask_Id().equals(task.gettask_Id())) {
                        throw new IllegalArgumentException("Task ID already exists for this index");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to read existing tasks", e);
            }
        }
    }

    /**
     * 从 JSON 文件中根据 task_Id 删除任务
     *
     * @param task_Id 要删除的任务的 ID
     * @param index_Id 索引 ID
     */
    public void deleteTaskByIdFromJson(String task_Id, String index_Id) {
        String fileName = "data/" + index_Id + ".json";
        // 读取 JSON 文件
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        List<Task> tasks;

        try {
            // 读取现有任务列表
            Task[] existingTasks = objectMapper.readValue(new File(fileName), Task[].class);
            tasks = new ArrayList<>(List.of(existingTasks));

            // 删除指定 task_Id 的任务
            tasks.removeIf(task -> task_Id.equals(task.gettask_Id()));

            // 写回 JSON 文件
            objectMapper.writeValue(new File(fileName), tasks);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read or write tasks", e);
        }
    }

//    public List<Task> syncIndex(String index_Id) {
//        String fileName = "data/" + index_Id + ".json";
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        List<Task> tasks = new ArrayList<>();
//
//        try {
//            // Read the existing task list from the JSON file
//            Task[] existingTasks = objectMapper.readValue(new File(fileName), Task[].class);
//            tasks = List.of(existingTasks);
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Failed to read tasks from file", e);
//        }
//
//        return tasks;
//    }

}

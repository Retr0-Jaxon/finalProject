package org.example.finalproject.service;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.finalproject.model.Task;
import org.example.finalproject.model.TaskIndex;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class IndexService {
//    private final List<TaskIndex> indices = new ArrayList<>();
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    private final String jsonFilePath = "task_indices.json";

    public IndexService() {

    }

    public void createIndex(String name) {
        // 检查name对应的json文件是否存在
        Path filePath = Paths.get("data", name + ".json");
        if (!Files.exists(filePath)) {
            try {
                // 如果不存在就创建一个以name为名字的空json文件
                Files.createFile(filePath);
            } catch (IOException e) {
                throw new RuntimeException("无法创建文件: " + name, e);
            }
        }
    }

    public void deleteIndex(String indexname) {
        // 删除path下以indexname为名字的json文件
        Path filePath = Paths.get("data", indexname + ".json");
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("无法删除文件: " + indexname, e);
        }
    }

    public List<Task> syncIndex(String index_Id) {
        String fileName = "data/" + index_Id + ".json";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<Task> tasks = new ArrayList<>();

        try {
            // Read the existing task list from the JSON file
            Task[] existingTasks = objectMapper.readValue(new File(fileName), Task[].class);
            tasks = new ArrayList<>(List.of(existingTasks));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read tasks from file", e);
        }

        return tasks;
    }


    public List<String> getAllIndices() {
        File folder = new File("data");
        File[] listOfFiles = folder.listFiles();
        List<String> fileNames = new ArrayList<>();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    fileNames.add(file.getName());
                }
            }
        }
        return fileNames;
    }
}

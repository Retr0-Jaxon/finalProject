package org.example.finalproject.service;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.finalproject.model.Task;
import org.example.finalproject.model.TaskIndex;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
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
        File lastUseJson = new File("lastUse/lastUse.json");
        if (lastUseJson.exists()) {
            try (FileWriter fileWriter = new FileWriter(lastUseJson)) {
                fileWriter.write("");
                fileWriter.write(index_Id); // 清空文件内容
            } catch (IOException e) {
                throw new RuntimeException("无法清空文件: " + lastUseJson.getName(), e);
            }
        }
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
        String lastUseage;
        try {
            lastUseage = new String(Files.readAllBytes(Paths.get("lastUse/lastUse.json")));
        } catch (IOException e) {
            throw new RuntimeException("无法读取文件: lastUse/lastUse.json", e);
        }
        List<String> fileNames = new ArrayList<>();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    fileNames.add(file.getName());
                }
            }
        }
        fileNames.add(lastUseage);
        return fileNames;
    }

    public ResponseEntity<String> rename(String index_Id, String newName){
        try {
            File oldFile = new File("data/" + index_Id + ".json");
            File newFile = new File("data/" + newName + ".json");
            if (oldFile.exists() && !newFile.exists()) {
                if (oldFile.renameTo(newFile)) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.registerModule(new JavaTimeModule());
                    try {
                        List<Task> tasks = objectMapper.readValue(newFile, new TypeReference<List<Task>>() {});
                        for (Task task : tasks) {
                            task.setindex_Id(newName);
                        }
                        objectMapper.writeValue(newFile, tasks);
                    } catch (IOException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update tasks in file: " + e.getMessage());
                    }
                    return ResponseEntity.ok("File renamed successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to rename file");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File does not exist or new file name already taken");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}

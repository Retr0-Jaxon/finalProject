package org.example.finalproject.controller;

import net.fortuna.ical4j.model.Property;
import org.example.finalproject.model.Task;
import org.example.finalproject.service.IcsFileService;
import org.example.finalproject.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.example.finalproject.service.IndexService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ics")
public class IcsFileController {

    private final IcsFileService icsFileService;
    private TaskService taskService;

    @Autowired
    public IcsFileController(IcsFileService icsFileService, TaskService taskService) {
        this.icsFileService = icsFileService;
        this.taskService = taskService;
    }

    @GetMapping("/parseFromUrl")
    public ResponseEntity<List<Task>> parseIcsFileFromUrl(@RequestParam String icsUrl , @RequestParam String index_Id) {
        try {
            // 使用 HttpClient 获取 ICS 文件内容
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(icsUrl))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            IndexService indexService = new IndexService();
            indexService.createIndex(index_Id);

            // 解析 ICS 文件内容
            List<Task> tasks = icsFileService.parseIcsFile(response.body())
                    .stream()
                    .map(event -> {
                        String summary = event.getSummary() != null ? event.getSummary().getValue() : "";
                        String category = event.getProperty(Property.CATEGORIES) != null ? event.getProperty(Property.CATEGORIES).getValue() : "";
                        LocalDate startDate = event.getStartDate() != null ? event.getStartDate().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
                        LocalDate endDate = event.getEndDate() != null ? event.getEndDate().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
                        return new Task(summary + category, index_Id, startDate, endDate, 2, false);
                    })
                    .collect(Collectors.toList());

            for (Task task : tasks){
                taskService.createTask_Local(task);
            }

            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

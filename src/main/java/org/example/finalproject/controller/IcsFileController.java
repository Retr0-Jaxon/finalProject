package org.example.finalproject.controller;

import org.example.finalproject.service.IcsFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ics")
public class IcsFileController {

    private final IcsFileService icsFileService;

    @Autowired
    public IcsFileController(IcsFileService icsFileService) {
        this.icsFileService = icsFileService;
    }

    @PostMapping("/parseFromUrl")
    public ResponseEntity<List<String>> parseIcsFileFromUrl(@RequestBody String icsUrl) {
        try {
            // 使用 HttpClient 获取 ICS 文件内容
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(icsUrl))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            // 解析 ICS 文件内容
            List<String> eventSummaries = icsFileService.parseIcsFile(response.body())
                    .stream()
                    .map(event -> event.getSummary().getValue())
                    .collect(Collectors.toList());
            
            return new ResponseEntity<>(eventSummaries, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

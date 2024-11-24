package org.example.finalproject.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.finalproject.model.Task;
import org.example.finalproject.model.TaskIndex;
import org.example.finalproject.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
@RequestMapping("/indices")

public class IndexController {
    @Autowired
    private IndexService indexService;

    @PostMapping("/create")
    public void createIndex(@RequestParam String name) {
        indexService.createIndex(name);
    }

    @DeleteMapping("/{id}")
    public void deleteIndex(@PathVariable("id") String id) {
        indexService.deleteIndex(id);
    }

    @GetMapping("/sync")
    public List<Task> getIndex(@RequestParam String index_Id) {
        return indexService.syncIndex(index_Id);
    }

    @GetMapping("/getall")
    public List<String> getAllIndices(){
        return indexService.getAllIndices();
    }

    @PostMapping("/rename")
    public ResponseEntity<String> renameIndexFile(@RequestParam String index_Id, @RequestParam String newName) {
        return indexService.rename(index_Id,newName);
    }
}

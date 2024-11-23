package org.example.finalproject.controller;

import org.example.finalproject.model.Task;
import org.example.finalproject.model.TaskIndex;
import org.example.finalproject.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}

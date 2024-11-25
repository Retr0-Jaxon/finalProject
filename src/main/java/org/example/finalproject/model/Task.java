package org.example.finalproject.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class Task {

    private Long id;
    private String task_Id;

    private String index_Id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime start_Date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime end_Date;

    private Integer priority;

    private int status;

    private String details;

    // Constructors
    public Task() {
    }

    public Task(Long id,String task_Id, String index_Id, LocalDateTime start_Date, LocalDateTime end_Date, Integer priority, int status, String details) {
        this.id = id;
        this.task_Id = task_Id;
        this.index_Id = index_Id;
        this.start_Date = start_Date;
        this.end_Date = end_Date;
        this.priority = priority;
        this.status = status;
        this.details = details;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String gettask_Id() {
        return task_Id;
    }

    public void settask_Id(String task_Id) {
        this.task_Id = task_Id;
    }

    public String getindex_Id() {
        return index_Id;
    }

    public void setindex_Id(String index_Id) {
        this.index_Id = index_Id;
    }

    public LocalDateTime getstart_Date() {
        return start_Date;
    }

    public void setstart_Date(LocalDateTime start_Date) {
        this.start_Date = start_Date;
    }

    public LocalDateTime getend_Date() {
        return end_Date;
    }

    public void setend_Date(LocalDateTime end_Date) {
        this.end_Date = end_Date;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    // toString method for easy printing
//    @Override
//    public String toString() {
//        return "Task{" +
//                "task_Id='" + task_Id + '\'' +
//                ", index_Id='" + index_Id + '\'' +
//                ", start_Date=" + start_Date +
//                ", end_Date=" + end_Date +
//                ", priority='" + priority + '\'' +
//                ", status='" + status + '\'' +
//                '}';
//    }
}
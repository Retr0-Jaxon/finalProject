package org.example.finalproject.model;

import java.time.LocalDate;

public class Task {

    private String task_Id;

    private String index_Id;

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDate start_Date;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate end_Date;

    private Integer priority;

    private boolean status;

    // Constructors
    public Task() {
    }

    public Task(String task_Id, String index_Id, LocalDate start_Date, LocalDate end_Date, Integer priority, boolean status) {
        this.task_Id = task_Id;
        this.index_Id = index_Id;
        this.start_Date = start_Date;
        this.end_Date = end_Date;
        this.priority = priority;
        this.status = status;
    }

    // Getters and Setters
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

    public LocalDate getstart_Date() {
        return start_Date;
    }

    public void setstart_Date(LocalDate start_Date) {
        this.start_Date = start_Date;
    }

    public LocalDate getend_Date() {
        return end_Date;
    }

    public void setend_Date(LocalDate end_Date) {
        this.end_Date = end_Date;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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
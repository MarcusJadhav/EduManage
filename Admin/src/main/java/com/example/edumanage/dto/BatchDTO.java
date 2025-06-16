package com.example.edumanage.dto;

import lombok.Data; import java.util.List;

@Data
public class BatchDTO {
    private Long id;
    private String batchName;
    private String schedule;
    private String course;
    private Long trainerId;
    private List studentIds; }


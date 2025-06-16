package com.example.edumanage.controller;

import com.example.edumanage.dto.BatchDTO;
import com.example.edumanage.model.Batch;
import com.example.edumanage.service.BatchService;
import com.example.edumanage.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap; import java.util.List; import java.util.Map;

@RestController
@RequestMapping("/api/batches")
public class BatchController {
    @Autowired
    private BatchService batchService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Batch> createBatch(@RequestBody Batch batch) {
        return ResponseEntity.ok(batchService.createBatch(batch));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<BatchDTO>> getAllBatches() {
        return ResponseEntity.ok(batchService.getAllBatchesDTO());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<BatchDTO> getBatchById(@PathVariable Long id) {
        return ResponseEntity.ok(batchService.getBatchDTOById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Batch> updateBatch(@PathVariable Long id, @RequestBody Batch batchDetails) {
        return ResponseEntity.ok(batchService.updateBatch(id, batchDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteBatch(@PathVariable Long id) {
        batchService.deleteBatch(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/trainer")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<List<BatchDTO>> getBatchesForTrainer() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long trainerId = jwtUtil.extractId(auth.getCredentials().toString().substring(7));
        return ResponseEntity.ok(batchService.getAllBatchesDTO().stream()
                .filter(batch -> batch.getTrainerId().equals(trainerId))
                .toList());
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> getAdminDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalUsers", batchService.getTotalUsers());
        dashboard.put("totalBatches", batchService.getTotalBatches());
        dashboard.put("totalTrainers", batchService.getTotalTrainers());
        dashboard.put("totalStudents", batchService.getTotalStudents());
        return ResponseEntity.ok(dashboard);
    }

}


package com.example.edumanage.service;

import com.example.edumanage.dto.BatchDTO;
import com.example.edumanage.model.Batch;
import com.example.edumanage.model.Student;
import com.example.edumanage.model.Trainer;
import com.example.edumanage.repository.BatchRepository;
import com.example.edumanage.repository.StudentRepository;
import com.example.edumanage.repository.TrainerRepository;
import com.example.edumanage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchService {

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    public Batch createBatch(Batch batch) {
        validateBatch(batch);
        return batchRepository.save(batch);
    }

    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }

    public Batch getBatchById(Long id) {
        return batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found"));
    }

    public Batch updateBatch(Long id, Batch batchDetails) {
        Batch batch = getBatchById(id);
        batch.setBatchName(batchDetails.getBatchName());
        batch.setSchedule(batchDetails.getSchedule());
        batch.setCourse(batchDetails.getCourse());
        batch.setTrainer(batchDetails.getTrainer());
        batch.setStudents(batchDetails.getStudents());
        validateBatch(batch);
        return batchRepository.save(batch);
    }

    public void deleteBatch(Long id) {
        batchRepository.deleteById(id);
    }

    public BatchDTO convertToDTO(Batch batch) {
        BatchDTO dto = new BatchDTO();
        dto.setId(batch.getId());
        dto.setBatchName(batch.getBatchName());
        dto.setSchedule(batch.getSchedule());
        dto.setCourse(batch.getCourse());
        dto.setTrainerId(batch.getTrainer().getId());
        dto.setStudentIds(batch.getStudents().stream().map(Student::getId).toList());
        return dto;
    }

    public List<BatchDTO> getAllBatchesDTO() {
        return getAllBatches().stream().map(this::convertToDTO).toList();
    }

    public BatchDTO getBatchDTOById(Long id) {
        return convertToDTO(getBatchById(id));
    }

    private void validateBatch(Batch batch) {
        // Check if trainer is already assigned to a batch with overlapping schedule
        Trainer trainer = batch.getTrainer();
        List<Batch> trainerBatches = batchRepository.findAll().stream()
                .filter(b -> b.getTrainer().getId().equals(trainer.getId()) && !b.getId().equals(batch.getId()))
                .toList();
        for (Batch existingBatch : trainerBatches) {
            if (isScheduleOverlap(batch.getSchedule(), existingBatch.getSchedule())) {
                throw new RuntimeException("Trainer is already assigned to a batch with overlapping schedule: " + existingBatch.getBatchName());
            }
        }
    }

    private boolean isScheduleOverlap(String schedule1, String schedule2) {
        // Simplified overlap check (assumes schedule format like "Mon-Fri 10AM-12PM")
        return schedule1.equals(schedule2); // In a real app, parse days/times to check for actual overlap
    }

    // Methods for admin dashboard
    public long getTotalUsers() {
        return userRepository.count();
    }

    public long getTotalBatches() {
        return batchRepository.count();
    }

    public long getTotalTrainers() {
        return trainerRepository.count();
    }

    public long getTotalStudents() {
        return studentRepository.count();
    }

}


package com.example.edumanage.service;

import com.example.edumanage.model.Trainer;
import com.example.edumanage.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {
    @Autowired
    private TrainerRepository trainerRepository;

    public Trainer createTrainer(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    public Trainer getTrainerById(Long id) {
        return trainerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trainer not found"));
    }

    public Trainer updateTrainer(Long id, Trainer trainerDetails) {
        Trainer trainer = getTrainerById(id);
        trainer.setName(trainerDetails.getName());
        trainer.setUser(trainerDetails.getUser());
        return trainerRepository.save(trainer);
    }

    public void deleteTrainer(Long id) {
        trainerRepository.deleteById(id);
    }

}


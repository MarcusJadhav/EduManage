package com.example.edumanage.service;

import com.example.edumanage.model.Admin;
import com.example.edumanage.model.User;
import com.example.edumanage.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public Admin createAdmin(String name, User user) {
        Admin admin = new Admin();
        admin.setName(name);
        admin.setUser(user);
        return adminRepository.save(admin);
    }
}
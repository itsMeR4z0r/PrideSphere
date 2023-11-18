package com.r4z0r.pridesphere.services;

import com.r4z0r.pridesphere.entity.AdminSession;
import com.r4z0r.pridesphere.repositories.AdminSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AdminSessionService {
    @Autowired
    private AdminSessionRepository adminSessionRepository;

    public Optional<AdminSession> findById(UUID sid) {
        return adminSessionRepository.findById(sid);
    }

    public AdminSession save(AdminSession sessionAdmin) {
        return adminSessionRepository.save(sessionAdmin);
    }


}

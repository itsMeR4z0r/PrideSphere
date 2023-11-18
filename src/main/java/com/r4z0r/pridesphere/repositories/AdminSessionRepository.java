package com.r4z0r.pridesphere.repositories;

import com.r4z0r.pridesphere.entity.AdminSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminSessionRepository extends JpaRepository<AdminSession, UUID> {
}
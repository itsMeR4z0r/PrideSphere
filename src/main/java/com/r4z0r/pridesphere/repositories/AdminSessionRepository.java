package com.r4z0r.pridesphere.repositories;

import com.r4z0r.pridesphere.entity.AdminSession;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AdminSessionRepository extends CrudRepository<AdminSession, UUID> {
}
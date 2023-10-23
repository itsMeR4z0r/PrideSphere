package com.r4z0r.pridesphere.repositories;

import com.r4z0r.pridesphere.entity.Admin;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface AdminRepository extends CrudRepository<Admin, UUID> {

    @Query(value = "SELECT a FROM Admin a WHERE a.email = ?1")
    Optional<Admin> findByEmail(String email);
}

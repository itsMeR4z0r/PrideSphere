package com.r4z0r.pridesphere.repositories;

import com.r4z0r.pridesphere.entity.Admin;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AdminRepository extends CrudRepository<Admin, UUID> {

}

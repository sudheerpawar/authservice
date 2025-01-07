package com.loontao.authservice.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loontao.authservice.entity.Role;
import com.loontao.authservice.entity.RoleEnum;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleEnum name);
}
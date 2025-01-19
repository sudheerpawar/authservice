package com.loontao.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loontao.authservice.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailId(String emailId); // Ensure this matches the property name in User entity
    Optional<User> findByFullname(String fullname); // Ensure this matches the property name in User entity
    Optional<User> findByPhoneNumber(String phoneNumber); // Ensure this matches the property name in User entity
    Optional<User> findByEmailIdOrPhoneNumberOrFullname(String emailId, String fullname, String phoneNumber); // Ensure this matches the property name in User entity
}
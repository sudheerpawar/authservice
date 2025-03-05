package com.loontao.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loontao.authservice.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmailId(String emailId); // Ensure this matches the property name in User entity
    Optional<User> findByFirstName(String firstName); // Ensure this matches the property name in User entity
    Optional<User> findByPhoneNumber(String phoneNumber); // Ensure this matches the property name in User entity
    Optional<User> findByEmailIdOrPhoneNumberOrFirstName(String emailId, String firstName, String phoneNumber); // Ensure this matches the property name in User entity
}
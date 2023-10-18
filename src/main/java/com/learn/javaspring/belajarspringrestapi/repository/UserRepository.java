package com.learn.javaspring.belajarspringrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learn.javaspring.belajarspringrestapi.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}

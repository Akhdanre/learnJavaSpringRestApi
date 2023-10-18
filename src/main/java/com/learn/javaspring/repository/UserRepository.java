package com.learn.javaspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learn.javaspring.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}

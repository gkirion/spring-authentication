package com.george.auth.repository;

import com.george.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    public List<User> findByUsername(String username);

}

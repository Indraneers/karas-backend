package com.twistercambodia.karasbackend.auth.repository;

import com.twistercambodia.karasbackend.auth.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {
    @Query("select u from User u")
    List<User> findAll();
    Optional<User> findByUsername(String username);
}

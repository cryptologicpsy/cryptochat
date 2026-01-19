package com.example.crypto.repositories;

import com.example.crypto.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    
    Optional<User> findByUsername(String username);
    
    Iterable<User> findByOnline(boolean online);
}
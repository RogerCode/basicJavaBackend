package com.example.demo.entity;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {

    @Cacheable
    Optional<User> findByUserNameAndPassword(String username, String password);

    @Transactional
    @Cacheable
    Optional<User> findByUserName(String username);

}

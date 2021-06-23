package com.example.demo.entity;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String>, UserRepositoryCustom {

    @Transactional
    @Cacheable
    Optional<User> findByUserName(String username);

    @Transactional
    @Cacheable
    Optional<User> findByUserNameAndEnabled(String username, boolean enabled);


}

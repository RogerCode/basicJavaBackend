package com.example.demo.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;

public interface UserRepositoryCustom {

    void createVerificationToken(VerificationToken verificationToken);

    VerificationToken getVerificationToken(String VerificationToken);
}

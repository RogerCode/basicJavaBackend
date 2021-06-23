package com.example.demo.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepositoryCustom {


    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Override
    public void createVerificationToken(VerificationToken verificationToken) {
        tokenRepository.save(verificationToken);
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }
}

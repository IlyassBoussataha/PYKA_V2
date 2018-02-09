/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.service;

import fr.pyka.lavage.model.Login;
import fr.pyka.lavage.model.Verificationtoken;
import fr.pyka.lavage.repo.LoginRepository;
import fr.pyka.lavage.repo.VerificationTokenRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Publicab
 */@Service("loginService")
public class LoginServiceImpl implements LoginService{

    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private VerificationTokenRepository tokenRepository;
    
    
    @Override
    public Login findLoginByEmail(String Email) {
        return loginRepository.findByEmail(Email);
    }

    @Override
    public void AddLogin(Login login) {
        loginRepository.save(login);
    }


    @Override
    public Login findLoginById(int id) {
        return loginRepository.findById(id);
    }

    @Override
    public void createVerificationToken(Login login, String token) {
        Verificationtoken myToken = new Verificationtoken(token, login);
        tokenRepository.save(myToken);
    }
    
    @Override
    public Verificationtoken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }
    
    @Override
    public Verificationtoken generateNewVerificationToken(final String existingVerificationToken) {
        Verificationtoken oldtoken = tokenRepository.findByToken(existingVerificationToken);
        oldtoken.updateToken(UUID.randomUUID().toString());
        oldtoken = tokenRepository.save(oldtoken);
        return oldtoken;
    }
    
}

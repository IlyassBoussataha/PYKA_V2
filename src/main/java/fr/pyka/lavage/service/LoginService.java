/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.service;

import fr.pyka.lavage.model.Login;
import fr.pyka.lavage.model.Verificationtoken;

/**
 *
 * @author Publicab
 */
public interface LoginService {
    
    public void AddLogin(Login login);
    public Login findLoginById(int id);
    public Login findLoginByEmail(String Email);
    void createVerificationToken(Login login, String token);
    public Verificationtoken generateNewVerificationToken(final String existingVerificationToken);
    Verificationtoken getVerificationToken(String VerificationToken);
    
}

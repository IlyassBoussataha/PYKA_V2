/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.repo;

import fr.pyka.lavage.model.Login;
import fr.pyka.lavage.model.Verificationtoken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Publicab
 */
public interface VerificationTokenRepository  extends JpaRepository<Verificationtoken, Long> {
 
    Verificationtoken findByToken(String token);
 
    Verificationtoken findByLoginId(Login login);
}


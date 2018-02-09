/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.repo;

import fr.pyka.lavage.model.Login;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Publicab
 */
@Repository("loginRepository")
public interface LoginRepository extends JpaRepository<Login, Long>{
    
    Login findByEmail(String Email);
    Login findById(int id);
    List<Login> findByValideAndLimited(boolean valide,boolean limited);
    List<Login> findByLimitedAndValide(boolean limited,boolean valide);
}

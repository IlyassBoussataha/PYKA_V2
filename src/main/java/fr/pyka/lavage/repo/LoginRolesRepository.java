/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.repo;

import fr.pyka.lavage.model.Login;
import fr.pyka.lavage.model.LoginRoles;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Publicab
 */
@Repository("loginrolesRepository")
public interface LoginRolesRepository extends JpaRepository<LoginRoles, Long>{
    
    LoginRoles findById(int id);
    List<LoginRoles> findByLogin(Login login);
    
}

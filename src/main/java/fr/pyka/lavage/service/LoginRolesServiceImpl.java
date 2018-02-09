/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.service;

import fr.pyka.lavage.model.Login;
import fr.pyka.lavage.model.LoginRoles;
import fr.pyka.lavage.repo.LoginRolesRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Publicab
 */
@Service("loginRolesService")
public class LoginRolesServiceImpl implements LoginRolesService{
    
    
    @Autowired
    private LoginRolesRepository loginRolesRepository;
    
    @Override
    public void AddLoginRoles(LoginRoles loginRoles) {
        loginRolesRepository.save(loginRoles);
    }
    

    @Override
    public LoginRoles findLoginRolesById(int id) {
        return loginRolesRepository.findById(id);
    }

    @Override
    public List<LoginRoles> findLoginRolesByLogin(Login login) {
        return loginRolesRepository.findByLogin(login);
    }

    
}

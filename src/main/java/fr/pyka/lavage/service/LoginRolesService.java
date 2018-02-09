/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.service;

import fr.pyka.lavage.model.Login;
import fr.pyka.lavage.model.LoginRoles;
import java.util.List;

/**
 *
 * @author Publicab
 */
public interface LoginRolesService {
    
    public void AddLoginRoles(LoginRoles loginRoles);
    public LoginRoles findLoginRolesById(int id);
    public List<LoginRoles> findLoginRolesByLogin(Login login);
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.service;

import fr.pyka.lavage.model.Login;
import fr.pyka.lavage.model.Profil;
import java.util.List;

/**
 *
 * @author Publicab
 */
public interface ProfilService {
    
    public void AddProfil(Profil profil, Login login,String Role);
    public void UpdateProfil(int Id, Profil profil, Login login, boolean admin, boolean changepwd);
    public Profil findProfilById(int id);
    public Profil findProfilByEmail(String email);
    public List<Profil> findAllClientsValider();
    public List<Profil> findAllLaveursValiderAndNotExclu();
    public List<Profil> findAllLaveursValiderAndExclu();
    public List<Profil> findAllProfilsValider();
    public List<Profil> findAllProfilsAValider();
    public List<Profil> findAllProfilsInactif();
    public List<Profil> findAllProfils();
    
}

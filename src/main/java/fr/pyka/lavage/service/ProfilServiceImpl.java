/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.service;

import fr.pyka.lavage.model.Login;
import fr.pyka.lavage.model.LoginRoles;
import fr.pyka.lavage.model.Profil;
import fr.pyka.lavage.repo.LoginRepository;
import fr.pyka.lavage.repo.LoginRolesRepository;
import fr.pyka.lavage.repo.ProfilRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Publicab
 */
@Service("profilService")
public class ProfilServiceImpl implements ProfilService{
    
    @Autowired
    private ProfilRepository profilrepository;
    @Autowired
    private LoginRepository loginrepository;
    
    @Autowired
    private LoginRolesRepository loginrolesrepository;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void AddProfil(Profil profil, Login login,String Role) {
        System.out.println("fr.pyka.lavage.service.ProfilServiceImpl.AddProfil()");
        login.setProfilId(profil);
        login.setEmail(profil.getEmail());
        login.setPassword(bCryptPasswordEncoder.encode(login.getPassword()));
        login.setValide(false);
        
        loginrepository.save(login);
        
        profil.setLogin(login);
        
        profil.setNbCommandes(0);
        profil.setLastCommandes(0);
        profil.setActive(true);
        
	profilrepository.save(profil);
        
        LoginRoles lr = new LoginRoles();
        lr.setLoginId(profil.getLogin());
        lr.setRoles(Role);
        System.out.println("fr.pyka.lavage.service.ProfilServiceImpl.AddProfil()");
        loginrolesrepository.save(lr);
    }
    
    
    
    
    @Override
    public void UpdateProfil(int Id, Profil profil, Login login, boolean admin, boolean changepwd) {
        
        
        Profil nProfil = profilrepository.findById(Id);
        
        Login lg = loginrepository.findById(nProfil.getLogin().getId());
        lg.setEmail(profil.getEmail());
        if(admin == true) lg.setAdmin(true);
        else lg.setAdmin(false);
        if(changepwd == false) lg.setPassword(login.getPassword());
        else lg.setPassword(bCryptPasswordEncoder.encode(login.getPassword()));
        lg.setExclu(login.getExclu());
        loginrepository.save(lg);
        
        
        
        nProfil.setCivilite(profil.getCivilite());
        nProfil.setNom(profil.getNom());
        nProfil.setPrenom(profil.getPrenom());
        nProfil.setDatedenaissance(profil.getDatedenaissance());
        nProfil.setAdresse(profil.getAdresse());
        nProfil.setCodepostale(profil.getCodepostale());
        nProfil.setVille(profil.getVille());
        nProfil.setEmail(profil.getEmail());
        nProfil.setTelephone(profil.getTelephone());
        nProfil.setSiren(profil.getSiren());
        nProfil.setRib(profil.getRib());
        nProfil.setNomEntreprise(profil.getNomEntreprise());
        nProfil.setParcours(profil.getParcours());
        nProfil.setDateentree(profil.getDateentree());
        nProfil.setDatesortie(profil.getDatesortie());
        nProfil.setDateExclusion(profil.getDateExclusion());
        nProfil.setPieceid(profil.getPieceid());
        nProfil.setPieceidverso(profil.getPieceidverso());
        nProfil.setPhoto(profil.getPhoto());
        nProfil.setKbis(profil.getKbis());
        nProfil.setPermis(profil.getPermis());
        nProfil.setCasierJudiciaire(profil.getCasierJudiciaire());
        nProfil.setVtc(profil.getVtc());
        nProfil.setPhotoRib(profil.getPhotoRib());
        nProfil.setNbCommandes(profil.getNbCommandes());
        nProfil.setLastCommandes(profil.getLastCommandes());
        nProfil.setActive(profil.getActive());
        
        
        profilrepository.save(nProfil);
    }
    
    
    

    @Override
    public Profil findProfilByEmail(String email) {
        return profilrepository.findByEmail(email);
    }

    @Override
    public List<Profil> findAllProfils() {
        
        return profilrepository.findAll();
    }

    @Override
    public Profil findProfilById(int id) {
        return profilrepository.findById(id);
    }

    @Override
    public List<Profil> findAllProfilsValider() {
        List<Login> loginList = loginrepository.findByValideAndLimited(true,false);
        if(!loginList.isEmpty()){
            List<Profil> profilList = new ArrayList<>();
            for (int i = 0; i < loginList.size(); i++) {
                profilList.add(profilrepository.findByEmail(loginList.get(i).getEmail()));
            }
            return profilList;
        }
        return Collections.emptyList();
    }

    @Override
    public List<Profil> findAllProfilsAValider() {
        List<Login> loginList = loginrepository.findByValideAndLimited(false,false);
        if(!loginList.isEmpty()){
            List<Profil> profilList = new ArrayList<>();
            for (int i = 0; i < loginList.size(); i++) {
                profilList.add(profilrepository.findByEmail(loginList.get(i).getEmail()));
            }
            return profilList;
        }
        return Collections.emptyList();
    }

    @Override
    public List<Profil> findAllProfilsInactif() {
        List<Login> loginList = loginrepository.findByLimitedAndValide(true,true);
        if(!loginList.isEmpty()){
            List<Profil> profilList = new ArrayList<>();
            for (int i = 0; i < loginList.size(); i++) {
                profilList.add(profilrepository.findByEmail(loginList.get(i).getEmail()));
            }
            return profilList;
        }
        return Collections.emptyList();
    }

    @Override
    public List<Profil> findAllClientsValider() {
        return profilrepository.findAllClients();
    }

    @Override
    public List<Profil> findAllLaveursValiderAndNotExclu() {
        return profilrepository.findAllLaveursNotExclu();
    }
    
    @Override
    public List<Profil> findAllLaveursValiderAndExclu() {
        return profilrepository.findAllLaveursExclu();
    }
    
}

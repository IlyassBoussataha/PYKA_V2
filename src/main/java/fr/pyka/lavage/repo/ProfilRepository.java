/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.repo;

import fr.pyka.lavage.model.Profil;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Publicab
 */
@Repository("profilRepository")
public interface ProfilRepository extends JpaRepository<Profil, Long>{
    
     Profil findById(int id);
     Profil findByEmail(String email);
     Profil findByVille(String ville);
     Profil findByActive(boolean active);
     @Query(value = "SELECT * from profil p,login l,login_roles lr WHERE p.id = l.profil_id AND l.id = lr.login_id AND l.valide = 1 AND lr.roles = 'CLIENT'", nativeQuery = true)
     List<Profil> findAllClients();
     @Query(value = "SELECT * from profil p,login l,login_roles lr WHERE p.id = l.profil_id AND l.id = lr.login_id AND l.valide = 1 AND l.exclu = 0 AND lr.roles = 'LAVEUR'", nativeQuery = true)
     List<Profil> findAllLaveursNotExclu();
     @Query(value = "SELECT * from profil p,login l,login_roles lr WHERE p.id = l.profil_id AND l.id = lr.login_id AND l.valide = 1 AND l.exclu = 1 AND lr.roles = 'LAVEUR'", nativeQuery = true)
     List<Profil> findAllLaveursExclu();
     
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.repo;

import fr.pyka.lavage.model.Commande;
import fr.pyka.lavage.model.Profil;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Publicab
 */
@Repository("commandeRepository")
public interface CommandeRepository extends JpaRepository<Commande, Long>{
    
    Commande findById(int id);
    List<Commande> findByNouvelle(boolean nouvelle);
    List<Commande> findByAssigner(boolean assigner);
    List<Commande> findByEffectuer(boolean effectuer);
    List<Commande> findByAnnuler(boolean annuler);
    List<Commande> findByEffectuerAndDateGreaterThanEqual(boolean effectuer,Date date);
    
    /* Client */
    List<Commande> findByNouvelleAndClientId(boolean nouvelle,Profil clientId);
    List<Commande> findByAssignerAndClientId(boolean assigner,Profil clientId);
    List<Commande> findByEffectuerAndClientId(boolean effectuer,Profil clientId);
    List<Commande> findByAnnulerAndClientId(boolean annuler,Profil clientId);
    
    /* Laveur */
    List<Commande> findByNouvelleAndLaveurId(boolean nouvelle,Profil laveurId);
    List<Commande> findByAssignerAndLaveurId(boolean assigner,Profil laveurId);
    List<Commande> findByEffectuerAndLaveurId(boolean effectuer,Profil laveurId);
    List<Commande> findByAnnulerAndLaveurId(boolean annuler,Profil laveurId);
}

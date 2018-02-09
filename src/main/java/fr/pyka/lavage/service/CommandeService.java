/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.service;

import fr.pyka.lavage.model.Commande;
import fr.pyka.lavage.model.Profil;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Publicab
 */
public interface CommandeService {
    
    public void AddCommande(Commande Commande);
    public void RemoveCommande(Commande Commande);
    public Commande findCommandeById(int id);
    public List<Commande> findAllNouvellesCommandes();
    public List<Commande> findAllCommandesAssigner();
    public List<Commande> findAllCommandesEffectuer();
    public List<Commande> findAllCommandesAnnuler();
    public List<Commande> findAllCommande();
    public List<Commande> findAllCommandesEffectuerAfterDate(Date date);
    
    /* client */
    
    public List<Commande> findAllNouvellesCommandesByClient(Profil client);
    public List<Commande> findAllCommandesAssignerByClient(Profil client);
    public List<Commande> findAllCommandesEffectuerByClient(Profil client);
    public List<Commande> findAllCommandesAnnulerByClient(Profil client);
    
    /* laveur */
    
    public List<Commande> findAllNouvellesCommandesByLaveur(Profil laveur);
    public List<Commande> findAllCommandesAssignerByLaveur(Profil laveur);
    public List<Commande> findAllCommandesEffectuerByLaveur(Profil laveur);
    public List<Commande> findAllCommandesAnnulerByLaveur(Profil laveur);
    
}

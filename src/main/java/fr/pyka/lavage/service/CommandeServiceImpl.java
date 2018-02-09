/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.service;

import fr.pyka.lavage.model.Commande;
import fr.pyka.lavage.model.Profil;
import fr.pyka.lavage.repo.CommandeRepository;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Publicab
 */
@Service("commandeService")
public class CommandeServiceImpl implements CommandeService{
    
    @Autowired
    private CommandeRepository commandeRepository;

    @Override
    public void AddCommande(Commande Commande) {
        commandeRepository.save(Commande);
    }

    @Override
    public void RemoveCommande(Commande Commande) {
        commandeRepository.delete(Commande);
    }

    @Override
    public Commande findCommandeById(int id) {
        return commandeRepository.findById(id);
    }

    @Override
    public List<Commande> findAllCommande() {
        return commandeRepository.findAll();
    }

    @Override
    public List<Commande> findAllNouvellesCommandes() {
        return commandeRepository.findByNouvelle(true);
    }

    @Override
    public List<Commande> findAllCommandesAssigner() {
        return commandeRepository.findByAssigner(true);
    }

    @Override
    public List<Commande> findAllCommandesEffectuer() {
        return commandeRepository.findByEffectuer(true);
    }

    @Override
    public List<Commande> findAllCommandesAnnuler() {
        return commandeRepository.findByAnnuler(true);
    }

    @Override
    public List<Commande> findAllCommandesEffectuerAfterDate(Date date) {
        return commandeRepository.findByEffectuerAndDateGreaterThanEqual(true, date);
    }
    
    @Override
    public List<Commande> findAllNouvellesCommandesByClient(Profil client) {
        return commandeRepository.findByNouvelleAndClientId(true, client);
    }

    @Override
    public List<Commande> findAllCommandesAssignerByClient(Profil client) {
        return commandeRepository.findByAssignerAndClientId(true, client);
    }

    @Override
    public List<Commande> findAllCommandesEffectuerByClient(Profil client) {
        return commandeRepository.findByEffectuerAndClientId(true, client);
    }

    @Override
    public List<Commande> findAllCommandesAnnulerByClient(Profil client) {
        return commandeRepository.findByAnnulerAndClientId(true, client);
    }

    @Override
    public List<Commande> findAllNouvellesCommandesByLaveur(Profil laveur) {
        return commandeRepository.findByNouvelleAndLaveurId(true, laveur);
    }

    @Override
    public List<Commande> findAllCommandesAssignerByLaveur(Profil laveur) {
        return commandeRepository.findByAssignerAndLaveurId(true, laveur);
    }

    @Override
    public List<Commande> findAllCommandesEffectuerByLaveur(Profil laveur) {
        return commandeRepository.findByEffectuerAndLaveurId(true, laveur);
    }

    @Override
    public List<Commande> findAllCommandesAnnulerByLaveur(Profil laveur) {
        return commandeRepository.findByAnnulerAndLaveurId(true, laveur);
    }
}

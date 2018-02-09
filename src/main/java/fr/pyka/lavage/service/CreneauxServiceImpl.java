/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.service;

import fr.pyka.lavage.model.Creneaux;
import fr.pyka.lavage.repo.CreneauxRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Publicab
 */
@Service("creneauxService")
public class CreneauxServiceImpl implements CreneauxService{
    
    @Autowired
    private CreneauxRepository creneauxRepository;

    @Override
    public void AddCreneaux(Creneaux Creneau) {
        creneauxRepository.save(Creneau);
    }

    @Override
    public Creneaux findCreneauxById(int id) {
        return creneauxRepository.findById(id);
    }

    @Override
    public List<Creneaux> findAllCreneaux() {
        return creneauxRepository.findAll();
    }

    @Override
    public void RemoveCreneau(Creneaux Creneau) {
        creneauxRepository.delete(Creneau);
    }

    @Override
    public List<Creneaux> findAllCreneauxOrderedByHeureDebut() {
        return creneauxRepository.findAllByOrderByHeureDebutAsc();
    }
    
}

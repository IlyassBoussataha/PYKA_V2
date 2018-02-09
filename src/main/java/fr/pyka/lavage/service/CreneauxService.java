/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.service;

import fr.pyka.lavage.model.Creneaux;
import java.util.List;

/**
 *
 * @author Publicab
 */
public interface CreneauxService {
    
    public void AddCreneaux(Creneaux Creneau);
    public void RemoveCreneau(Creneaux Creneau);
    public Creneaux findCreneauxById(int id);
    public List<Creneaux> findAllCreneaux();
    public List<Creneaux> findAllCreneauxOrderedByHeureDebut();
}

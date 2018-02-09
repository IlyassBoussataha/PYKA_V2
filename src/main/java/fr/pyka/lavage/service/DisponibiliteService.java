/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.service;

import fr.pyka.lavage.model.Creneaux;
import fr.pyka.lavage.model.Disponibilite;
import fr.pyka.lavage.model.Profil;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Publicab
 */
public interface DisponibiliteService {
    
    public void AddDisponibilite(Disponibilite Disponibilite);
    public Long RemoveDisponibilite(Disponibilite Disponibilite);
    public Disponibilite findDisponibiliteById(int id);
    public List<Disponibilite> findDisponibiliteByDate(Date date);
    public LinkedHashMap<Creneaux, List<Disponibilite>> findDisponibiliteByLaveurAndWeek(Profil laveur,List<Date> calendrier);
    public List<Disponibilite> findDisponibiliteByLaveurAndWeek2(Profil laveur,List<Date> calendrier);
    public List<Disponibilite> findAllDisponibilite();
    
}

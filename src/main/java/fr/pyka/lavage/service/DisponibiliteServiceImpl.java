/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.service;

import fr.pyka.lavage.model.Creneaux;
import fr.pyka.lavage.model.Disponibilite;
import fr.pyka.lavage.model.Profil;
import fr.pyka.lavage.repo.CreneauxRepository;
import fr.pyka.lavage.repo.DisponibiliteRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Publicab
 */
@Service("disponibiliteService")
public class DisponibiliteServiceImpl implements DisponibiliteService{
    
    @Autowired
    private DisponibiliteRepository disponibiliteRepository;
    @Autowired
    private CreneauxRepository creneauxRepository;

    @Override
    public void AddDisponibilite(Disponibilite Disponibilite) {
        disponibiliteRepository.save(Disponibilite);
    }

    @Override
    public Disponibilite findDisponibiliteById(int id) {
        return disponibiliteRepository.findById(id);
    }

    @Override
    public List<Disponibilite> findDisponibiliteByDate(Date date) {
        return disponibiliteRepository.findByDate(date);
    }

    @Override
    public List<Disponibilite> findAllDisponibilite() {
        return disponibiliteRepository.findAll();
    }


    @Override
    public LinkedHashMap<Creneaux, List<Disponibilite>> findDisponibiliteByLaveurAndWeek(Profil laveur, List<Date> calendrier) {
        
        /*for (int i = 0; i < calendrier.size(); i++) {
            List<Disponibilite> dispo = disponibiliteRepository.findByLaveurIdAndDate(laveur, calendrier.get(i));
            if(dispo.isEmpty()){
                    Disponibilite newdispo = new Disponibilite();
                    newdispo.setAttribue(false);
                    newdispo.setCreneau(null);
                    newdispo.setDate(calendrier.get(i));
                    newdispo.setLaveurId(laveur);
                    ListDisponibilite.add(newdispo);
                }
            else{
                for (int j = 0; j < dispo.size(); j++) {
                        ListDisponibilite.add(dispo.get(j));
                }
            }
            
        }
        List<Creneaux> listCreneaux = creneauxRepository.findAll();
        for (int i = 0; i < listCreneaux.size(); i++) {
            for (int j = 0; j < calendrier.size(); j++) {
            Disponibilite dispo = disponibiliteRepository.findByLaveurIdAndDateAndCreneau(laveur, calendrier.get(j),listCreneaux.get(i));
            if(dispo == null){
                    Disponibilite newdispo = new Disponibilite();
                    newdispo.setAttribue(false);
                    newdispo.setCreneau(null);
                    newdispo.setDate(calendrier.get(i));
                    newdispo.setLaveurId(laveur);
                    ListDisponibilite.add(newdispo);
                }
            else if(dispo != null){
                        ListDisponibilite.add(dispo);
                }
            
        }
        }*/
        List<Creneaux> listCreneaux = creneauxRepository.findAllByOrderByHeureDebutAsc();
        LinkedHashMap<Creneaux, List<Disponibilite>> map = new LinkedHashMap<>();
        for (int i = 0; i < listCreneaux.size(); i++) {
            List<Disponibilite> ListDisponibilite = new ArrayList<>();
            for (int j = 0; j < calendrier.size(); j++) { 
            Disponibilite dispo = disponibiliteRepository.findByLaveurIdAndDateAndCreneau(laveur, calendrier.get(j),listCreneaux.get(i));
            if(dispo == null){
                    Disponibilite newdispo = new Disponibilite();
                    newdispo.setAttribue(false);
                    newdispo.setCreneau(null);
                    newdispo.setDate(calendrier.get(j));
                    newdispo.setLaveurId(laveur);
                    ListDisponibilite.add(newdispo);
                }
            else if(dispo != null){
                ListDisponibilite.add(dispo);
                }
            
            }
            System.out.println("fr.pyka.lavage.service.DisponibiliteServiceImpl.findDisponibiliteByLaveurAndWeek() :" + ListDisponibilite);
            map.put(listCreneaux.get(i), ListDisponibilite);
            System.out.println("fr.pyka.lavage.service.DisponibiliteServiceImpl.findDisponibiliteByLaveurAndWeek() : : " + listCreneaux.get(i));
            System.out.println("fr.pyka.lavage.service.DisponibiliteServiceImpl.findDisponibiliteByLaveurAndWeek() :" + map);
        }
        
        return map;
    }

    @Override
    public List<Disponibilite> findDisponibiliteByLaveurAndWeek2(Profil laveur, List<Date> calendrier) {
        List<Disponibilite> ListDisponibilite = new ArrayList<>();
        for (int i = 0; i < calendrier.size(); i++) { 
            List<Disponibilite> dispo = disponibiliteRepository.findByLaveurIdAndDate(laveur, calendrier.get(i));
            if(dispo.isEmpty()){
                    Disponibilite newdispo = new Disponibilite();
                    newdispo.setAttribue(false);
                    newdispo.setCreneau(null);
                    newdispo.setDate(calendrier.get(i));
                    newdispo.setLaveurId(laveur);
                    ListDisponibilite.add(newdispo);
                }
            else{
                for (int j = 0; j < dispo.size(); j++) {
                        ListDisponibilite.add(dispo.get(j));
                }
            }
        }
        return ListDisponibilite;
    }

    @Override
    public Long RemoveDisponibilite(Disponibilite Disponibilite) {
        return disponibiliteRepository.removeById(Disponibilite.getId());
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.repo;

import fr.pyka.lavage.model.Creneaux;
import fr.pyka.lavage.model.Disponibilite;
import fr.pyka.lavage.model.Profil;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Publicab
 */
@Repository("disponibiliteRepository")
public interface DisponibiliteRepository extends JpaRepository<Disponibilite, Long>{
    
    Disponibilite findById(int id);
    Long removeById(int id);
    List<Disponibilite> findByDate(Date date);
    List<Disponibilite> findByLaveurIdAndDate(Profil laveur,Date date);
    Disponibilite findByLaveurIdAndDateAndCreneau(Profil laveur,Date date,Creneaux creneau);
}

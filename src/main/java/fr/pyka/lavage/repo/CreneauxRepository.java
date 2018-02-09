/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.repo;

import fr.pyka.lavage.model.Creneaux;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Publicab
 */
@Repository("creneauxRepository")
public interface CreneauxRepository extends JpaRepository<Creneaux, Long>{
    
    Creneaux findById(int id);
    public List<Creneaux> findAllByOrderByHeureDebutAsc(); 
}

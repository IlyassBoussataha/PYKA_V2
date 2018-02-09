/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.repo;

import fr.pyka.lavage.model.Chat;
import fr.pyka.lavage.model.ChatLine;
import fr.pyka.lavage.model.Profil;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Publicab
 */
@Repository("chatLineRepository")
public interface ChatLineRepository extends JpaRepository<ChatLine, Long>{
    
    ChatLine findById(Long id);
    List<ChatLine> findByReceiverId(Profil profilId);
    List<ChatLine> findBySenderId(Profil profilId);
    List<ChatLine> findByChatId(Chat chatId);
    
}

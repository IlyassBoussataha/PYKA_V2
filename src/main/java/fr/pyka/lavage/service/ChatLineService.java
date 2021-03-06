/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.service;

import fr.pyka.lavage.model.Chat;
import fr.pyka.lavage.model.ChatLine;
import fr.pyka.lavage.model.Profil;
import java.util.List;

/**
 *
 * @author Publicab
 */
public interface ChatLineService {
    
    public void AddChatLine(Chat chat,ChatLine chatLine);
    public ChatLine findChatLineById(Long id);
    public List<ChatLine> findChatLineByReceiverId(Profil profilId);
    public List<ChatLine> findChatLineBySenderId(Profil profilId);
    public List<ChatLine> findChatLineByChatId(Chat chatId);
    public List<ChatLine> findAllChatLine();
    
}

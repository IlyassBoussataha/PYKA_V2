/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.service;

import fr.pyka.lavage.model.Chat;
import fr.pyka.lavage.model.Profil;
import java.util.List;

/**
 *
 * @author Publicab
 */
public interface ChatService {
    
    public Chat findChatBySenderId(Profil profilId);
    public List<Chat> findAllChat();
    
}

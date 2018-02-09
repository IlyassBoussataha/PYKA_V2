/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.service;

import fr.pyka.lavage.model.Chat;
import fr.pyka.lavage.model.Profil;
import fr.pyka.lavage.repo.ChatRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Publicab
 */
@Service("chatService")
public class ChatServiceImpl implements ChatService{
    
    @Autowired
    private ChatRepository chatrepository;

    @Override
    public Chat findChatBySenderId(Profil profilId) {
        return null;
    }

    @Override
    public List<Chat> findAllChat() {
        return chatrepository.findAll();
    }
    
}

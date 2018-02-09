/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.service;


import fr.pyka.lavage.model.Chat;
import fr.pyka.lavage.model.ChatLine;
import fr.pyka.lavage.model.Profil;
import fr.pyka.lavage.repo.ChatLineRepository;
import fr.pyka.lavage.repo.ChatRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Publicab
 */
@Service("chatLineService")
public class ChatLineServiceImpl implements ChatLineService{
    
    @Autowired
    private ChatLineRepository chatLinerepository;
    @Autowired
    private ChatRepository chatrepository;

    @Override
    public void AddChatLine(Chat chat,ChatLine chatLine) {
        chatrepository.save(chat);
        chatLinerepository.save(chatLine);
    }

    @Override
    public ChatLine findChatLineById(Long id) {
        return chatLinerepository.findById(id);
    }
    
    
    @Override
    public List<ChatLine> findChatLineByReceiverId(Profil profilId) {
        return chatLinerepository.findByReceiverId(profilId);
    }

    @Override
    public List<ChatLine> findChatLineBySenderId(Profil profilId) {
        return chatLinerepository.findBySenderId(profilId);
    }

    @Override
    public List<ChatLine> findChatLineByChatId(Chat chatId) {
        return chatLinerepository.findByChatId(chatId);
    }

    @Override
    public List<ChatLine> findAllChatLine() {
        return chatLinerepository.findAll();
    }

    
}

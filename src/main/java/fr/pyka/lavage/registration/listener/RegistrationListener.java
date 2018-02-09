/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.registration.listener;

import fr.pyka.lavage.model.Login;
import fr.pyka.lavage.registration.OnRegistrationCompleteEvent;
import fr.pyka.lavage.service.LoginService;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
/**
 *
 * @author Publicab
 */
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    @Autowired
    private LoginService loginservice;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    // API

   
    @Override
    public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(final OnRegistrationCompleteEvent event) {
        final Login login = event.getLogin();
        final String token = UUID.randomUUID().toString();
        final String role = event.getRole();
        loginservice.createVerificationToken(login, token);
        
        if(role.equals("LAVEUR")){
            SimpleMailMessage email = constructEmailMessage(event, login, token);
            mailSender.send(email);
        }
        else if(role.equals("CLIENT")){
            SimpleMailMessage email = constructEmailMessage2(event, login, token);
            mailSender.send(email);
        }
    }

    //

    
    private SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event, final Login login, final String token) {
        final String recipientAddress = login.getEmail();
        final String subject = "PYKA - Confirmation D'inscription";
        final String confirmationUrl = event.getAppUrl() + "/ConfirmerInscription?token=" + token;
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("Bonjour " + login.getProfilId().getCivilite() + " " + login.getProfilId().getNom() + " " + login.getProfilId().getPrenom() + ","
                + "\r\n \r\n \r\nNous vous remercions d’avoir complété vos informations personnelles afin de confirmer votre compte. \r\n \r\n"
                + "Pour confirmer votre compte, cliquer sur le lien suivant : \r\n \r\n"
                + "http://pyka.fr" + confirmationUrl
                + "\r\n \r\nEquipe PYKA ");
        email.setFrom(env.getProperty("spring.mail.username"));
        return email;
    }
    
    private SimpleMailMessage constructEmailMessage2(final OnRegistrationCompleteEvent event, final Login login, final String token) {
        final String recipientAddress = login.getEmail();
        final String subject = "PYKA - Confirmation D'inscription";
        final String confirmationUrl = event.getAppUrl() + "/ConfirmerInscription?token=" + token;
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("Bonjour " + login.getProfilId().getCivilite() + " " + login.getProfilId().getNom() + " " + login.getProfilId().getPrenom() + ","
                + "\r\n \r\n \r\nNous avons le plaisir de vous confirmer la création de votre espace PYKA. \r\n \r\n"
                + "Pour confirmer votre compte, cliquer sur le lien suivant : \r\n \r\n"
                + "http://pyka.fr" + confirmationUrl
                + "\r\n \r\nPour vous connecter, vous aurez besoin de l’adresse e-mail et du mot de passe renseignés lors de la création de votre espace\r\n \r\n"
                + "Nous vous remercions de votre confiance, A très bientôt www.pyka.fr"     
                + "\r\n \r\nPYKA ");
        email.setFrom(env.getProperty("spring.mail.username"));
        return email;
    }
    
}

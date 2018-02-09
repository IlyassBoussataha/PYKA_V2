
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.registration;

import fr.pyka.lavage.model.Login;
import org.springframework.context.ApplicationEvent;

/**
 *
 * @author Publicab
 */

@SuppressWarnings("serial")
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private final String appUrl;
    private final Login login;
    private final String role;

    public OnRegistrationCompleteEvent(final Login login, final String appUrl, final String role) {
        super(login);
        this.login = login;
        this.appUrl = appUrl;
        this.role = role;
    }

    //

    public String getAppUrl() {
        return appUrl;
    }

    public Login getLogin() {
        return login;
    }

    public String getRole() {
        return role;
    }
    
    
}

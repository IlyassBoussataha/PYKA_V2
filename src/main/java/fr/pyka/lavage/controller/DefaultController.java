/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.controller;

import com.fasterxml.jackson.databind.node.TextNode;
import fr.pyka.lavage.model.Login;
import fr.pyka.lavage.model.Profil;
import fr.pyka.lavage.model.Verificationtoken;
import fr.pyka.lavage.registration.OnRegistrationCompleteEvent;
import fr.pyka.lavage.service.LoginService;
import fr.pyka.lavage.service.ProfilService;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Publicab
 */
@Controller
public class DefaultController {
    
    @Autowired
    private LoginService loginService;
    @Autowired
    private ProfilService profilService;
    @Autowired
    private MessageSource messages;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    ApplicationEventPublisher eventPublisher;
    @Autowired
    private Environment env;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    static final String dictionnaire = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom srnd = new SecureRandom();
    
    @RequestMapping(value="/", method = RequestMethod.GET)
    public ModelAndView landing(){
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("index");
            return modelAndView;
    }
    
    @RequestMapping(value="/login", method = RequestMethod.GET)
    public ModelAndView login(){
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("login");
            return modelAndView;
    }
    
    @RequestMapping(value="/inscription", method = RequestMethod.GET)
    public ModelAndView registerForm(){
            ModelAndView modelAndView = new ModelAndView();
            Profil profil = new Profil();
            Login login = new Login();
            modelAndView.addObject("profil", profil);
            modelAndView.addObject("login", login);
            modelAndView.setViewName("inscriptionclient");
            return modelAndView;
    }
    
    @RequestMapping(value="/inscriptionLaveur", method = RequestMethod.GET)
    public ModelAndView registerLaveurForm(){
            ModelAndView modelAndView = new ModelAndView();
            Profil profil = new Profil();
            Login login = new Login();
            modelAndView.addObject("profil", profil);
            modelAndView.addObject("login", login);
            modelAndView.setViewName("inscriptionlaveur");
            return modelAndView;
    }
    
    @RequestMapping(value="/inscriptionEntreprise", method = RequestMethod.GET)
    public ModelAndView registerEntrepriseForm(){
            ModelAndView modelAndView = new ModelAndView();
            Profil profil = new Profil();
            Login login = new Login();
            modelAndView.addObject("profil", profil);
            modelAndView.addObject("login", login);
            modelAndView.setViewName("inscriptionentreprise");
            return modelAndView;
    }
    
    @RequestMapping(value="/home", method = RequestMethod.GET)
    public ModelAndView defaultAfterLogin(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasRole = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("SUPER_ADMIN"));
        boolean hasRole2 = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN"));
        boolean hasRole3 = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("CLIENT"));
        boolean hasRole4 = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("LAVEUR"));

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String email=null;
        if (principal instanceof UserDetails) {
        email = ((UserDetails)principal).getUsername();
        } else {
        email = principal.toString();
        }
        Login login = loginService.findLoginByEmail(email);
        if(hasRole || hasRole2){
            return new ModelAndView("redirect:/Admin/home");
            }
        if(hasRole3){
            modelAndView.setViewName("redirect:/Client/home");
            return modelAndView;
            }
        if(hasRole4){
            modelAndView.setViewName("redirect:/Laveur/home");
            return modelAndView;
            }

        return modelAndView;
    }
    
    /*
    @RequestMapping(value = "/inscription", method = RequestMethod.POST)
	public ModelAndView inscription(@Valid Profil profil, BindingResult bindingResult, Login login,@RequestParam("file") MultipartFile[] files, WebRequest request,@RequestParam("stringdatedenaissance") @DateTimeFormat(pattern="dd/MM/yyyy") Date datedenaissance,@RequestParam("optionsRadios") String typeCompte) throws IOException, ParseException {
		ModelAndView modelAndView = new ModelAndView();
                System.out.println("fr.pyka.lavage.controller.DefaultController.inscription() : " + typeCompte);
		Profil profilExists = profilService.findProfilByEmail(profil.getEmail());
		if (profilExists != null) {
                        modelAndView.addObject("emailErrorMessage", "Email dèja existant !");
                        modelAndView.addObject("profil", profil);
                        modelAndView.addObject("login", login);
                        modelAndView.setViewName("inscription");
                        return modelAndView;
		}
		if (bindingResult.hasErrors()) {
                        modelAndView.addObject("errorMessage", bindingResult.getFieldError());
			modelAndView.setViewName("inscription");
                        System.out.println("fr.pyka.lavage.controller.DefaultController.inscription()" + bindingResult.getFieldError());
		} else {
                        //String path = new File("src/main/resources/static/assets/img/").getAbsolutePath();
                        String base = System.getProperty("catalina.base");
                        String path = base.concat("/webapps/uploads/images/");
                        for (int i = 0; i < files.length; i++) {
                                MultipartFile file = files[i];
                                String ext = FilenameUtils.getExtension(file.getOriginalFilename());
                                String oldname = FilenameUtils.removeExtension(file.getOriginalFilename());
                                String newFilename = DigestUtils.md5Hex( oldname );
                                System.out.println( ext + "-" + oldname + " " + file.getOriginalFilename() + " -> ");
                                File tmp = new File(path,newFilename + "." + ext);
                                file.transferTo(tmp);
                                
                                if("Laveur".equals(typeCompte)){
                                    if (i==0) profil.setPhoto(newFilename + "." + ext);
                                    if (i==1) profil.setPieceid(newFilename + "." + ext);
                                    if (i==2) profil.setPermis(newFilename + "." + ext);
                                    if (i==3) profil.setCasierJudiciaire(newFilename + "." + ext);
                                    if (i==4) profil.setKbis(newFilename + "." + ext);
                                }
                                else{
                                    if (i==0) profil.setPhoto(newFilename + "." + ext);
                                    if (i==5) profil.setVtc(newFilename + "." + ext);
                                }
                        }
                        profil.setTelephone(profil.getTelephone().replaceAll("\\s+",""));
                        profil.setDateentree(new Date());
                        if("Laveur".equals(typeCompte)){ 
                            SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
                            Date date = formatter.parse(formatter.format(datedenaissance));
                            profil.setDatedenaissance(date);
                            profilService.AddProfil(profil,login,"LAVEUR");
                        }
			if("Client".equals(typeCompte)){ 
                            profil.setSiren(null);
                            profilService.AddProfil(profil,login,"CLIENT");
                        }
                        try {
                            String appUrl = request.getContextPath();
                            Login regLogin = loginService.findLoginByEmail(profil.getEmail());
                            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(regLogin, appUrl));
                        } catch (Exception me) {
                            return new ModelAndView("email", "error.profil", profil);
                        }
			modelAndView.addObject("successMessage", "Compte crée avec succèss");
			modelAndView.addObject("profil", new Profil());
			modelAndView.setViewName("login");
                        modelAndView.setViewName("InscriptionSuccess");
                        return InscriptionSuccess(true);//appelle fonction
			
		}
		return modelAndView;
	}*/
        
        
    @RequestMapping(value = "/inscription", method = RequestMethod.POST)
	public ModelAndView inscription(@Valid Profil profil, BindingResult bindingResult, Login login,@RequestParam("file") MultipartFile[] files, WebRequest request) throws IOException, ParseException {
		ModelAndView modelAndView = new ModelAndView();
                //System.out.println("fr.pyka.lavage.controller.DefaultController.inscription() : " + typeCompte);
		Profil profilExists = profilService.findProfilByEmail(profil.getEmail());
		if (profilExists != null) {
                        modelAndView.addObject("emailErrorMessage", "Email dèja existant !");
                        modelAndView.addObject("profil", profil);
                        modelAndView.addObject("login", login);
                        modelAndView.setViewName("inscription");
                        return modelAndView;
		}
		if (bindingResult.hasErrors()) {
                        modelAndView.addObject("errorMessage", bindingResult.getFieldError());
			modelAndView.setViewName("inscription");
                        System.out.println("fr.pyka.lavage.controller.DefaultController.inscription()" + bindingResult.getFieldError());
		} else {
                        //String path = new File("src/main/resources/static/assets/img/").getAbsolutePath();
                        String base = System.getProperty("catalina.base");
                        String path = base.concat("/webapps/uploads/images/");
                        for (int i = 0; i < files.length; i++) {
                                MultipartFile file = files[i];
                                String ext = FilenameUtils.getExtension(file.getOriginalFilename());
                                String oldname = FilenameUtils.removeExtension(file.getOriginalFilename());
                                String newFilename = DigestUtils.md5Hex( oldname );
                                System.out.println( ext + "-" + oldname + " " + file.getOriginalFilename() + " -> ");
                                File tmp = new File(path,newFilename + "." + ext);
                                file.transferTo(tmp);
                                
                                if (i==0) profil.setPhoto(newFilename + "." + ext);
                                //if (i==5) profil.setVtc(newFilename + "." + ext);
                        }
                        profil.setTelephone(profil.getTelephone().replaceAll("\\s+",""));
                        profil.setDateentree(new Date());
                        profil.setSiren(null);
                        //profil.setNomEntreprise(null);
                        profil.setRib(null);
                        profilService.AddProfil(profil,login,"CLIENT");
                        try {
                            String appUrl = request.getContextPath();
                            Login regLogin = loginService.findLoginByEmail(profil.getEmail());
                            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(regLogin, appUrl,"CLIENT"));
                        } catch (Exception me) {
                            return new ModelAndView("email", "error.profil", profil);
                        }
			modelAndView.addObject("successMessage", "Compte crée avec succèss");
			modelAndView.addObject("profil", new Profil());
			modelAndView.setViewName("login");
                        modelAndView.setViewName("InscriptionSuccess");
                        return InscriptionSuccess(true);//appelle fonction
			
		}
		return modelAndView;
	}
        
        
        @RequestMapping(value = "/inscriptionLaveur", method = RequestMethod.POST)
	public ModelAndView inscriptionLaveur(@Valid Profil profil, BindingResult bindingResult, Login login,@RequestParam("file") MultipartFile[] files, WebRequest request,@RequestParam("stringdatedenaissance") @DateTimeFormat(pattern="dd/MM/yyyy") Date datedenaissance) throws IOException, ParseException {
		ModelAndView modelAndView = new ModelAndView();
		Profil profilExists = profilService.findProfilByEmail(profil.getEmail());
		if (profilExists != null) {
                        modelAndView.addObject("emailErrorMessage", "Email dèja existant !");
                        modelAndView.addObject("profil", profil);
                        modelAndView.addObject("login", login);
                        modelAndView.setViewName("inscription");
                        return modelAndView;
		}
		if (bindingResult.hasErrors()) {
                        modelAndView.addObject("errorMessage", bindingResult.getFieldError());
			modelAndView.setViewName("inscription");
                        System.out.println("fr.pyka.lavage.controller.DefaultController.inscriptionLaveur()" + bindingResult.getFieldError());
		} else {
                        //String path = new File("src/main/resources/static/assets/img/").getAbsolutePath();
                        String base = System.getProperty("catalina.base");
                        String path = base.concat("/webapps/uploads/images/");
                        for (int i = 0; i < files.length; i++) {
                                MultipartFile file = files[i];
                                String ext = FilenameUtils.getExtension(file.getOriginalFilename());
                                String oldname = FilenameUtils.removeExtension(file.getOriginalFilename());
                                String newFilename = DigestUtils.md5Hex( oldname );
                                System.out.println( ext + "-" + oldname + " " + file.getOriginalFilename() + " -> ");
                                File tmp = new File(path,newFilename + "." + ext);
                                file.transferTo(tmp);
                                
                                if (i==0) profil.setPhoto(newFilename + "." + ext);
                                if (i==1) profil.setPieceid(newFilename + "." + ext);
                                if (i==2) profil.setPermis(newFilename + "." + ext);
                                if (i==3) profil.setKbis(newFilename + "." + ext);
                                if (i==4) profil.setCasierJudiciaire(newFilename + "." + ext);
                                if (i==5) profil.setPhotoRib(newFilename + "." + ext);
                        }
                        profil.setTelephone(profil.getTelephone().replaceAll("\\s+",""));
                        profil.setDateentree(new Date());
                        SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
                        Date date = formatter.parse(formatter.format(datedenaissance));
                        profil.setDatedenaissance(date);
                        profilService.AddProfil(profil,login,"LAVEUR");
                        try {
                            String appUrl = request.getContextPath();
                            Login regLogin = loginService.findLoginByEmail(profil.getEmail());
                            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(regLogin, appUrl,"LAVEUR"));
                        } catch (Exception me) {
                            return new ModelAndView("email", "error.profil", profil);
                        }
			modelAndView.addObject("successMessage", "Compte crée avec succèss");
			modelAndView.addObject("profil", new Profil());
			modelAndView.setViewName("login");
                        modelAndView.setViewName("InscriptionSuccess");
                        return InscriptionSuccess(true);//appelle fonction
			
		}
		return modelAndView;
	}
        
    @RequestMapping(value="/InscriptionSuccess", method = RequestMethod.GET)
    public ModelAndView InscriptionSuccess(boolean etat){
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("etat", etat);
            modelAndView.setViewName("InscriptionSuccess");
            return modelAndView;
    }
    
    @RequestMapping(value = "/ConfirmerInscription", method = RequestMethod.GET)
    public ModelAndView confirmInscription(WebRequest request, Model model, @RequestParam("token") String token,RedirectAttributes redirectAttributes) {

        Verificationtoken verificationToken = loginService.getVerificationToken(token);
        if (verificationToken == null) {
            model.addAttribute("expired", false);
            return InvalideCode("Code de validation Incorrect !");
        }

        Login login = verificationToken.getLogin();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            model.addAttribute("expired", true);
            model.addAttribute("token", token);
            return InvalideCode("Code de validation expiré !");
        } 
        login.setConfirme(true); 
        loginService.AddLogin(login);
        return ValideCode(true);
    }
    
    @RequestMapping(value="/ValideCode", method = RequestMethod.GET)
    public ModelAndView ValideCode(boolean etat){
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("etat", etat);
            modelAndView.setViewName("ValideCode");
            return modelAndView;
    }


    @RequestMapping(value="/InvalideCode", method = RequestMethod.GET)
    public ModelAndView InvalideCode(String message){
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("erreurmessage", message);
            modelAndView.setViewName("InvalideCode");
            return modelAndView;
    }
    
    @RequestMapping(value = "/RenvoyerCode", method = RequestMethod.GET)
    public String resendRegistrationToken(final HttpServletRequest request, final Model model, @RequestParam("token") final String oldToken) {
        final Verificationtoken newToken = loginService.generateNewVerificationToken(oldToken);
        final Login login = newToken.getLogin();
        try {
            final String appUrl = "http://" + request.getServerName() + request.getContextPath();
            final SimpleMailMessage email = ResendVerificationTokenEmail(appUrl, newToken, login);
            mailSender.send(email);
        } catch (final MailException e) {
            model.addAttribute("message", e.getLocalizedMessage());
            return "redirect:/login";
        }
        model.addAttribute("message", "Un nouveau lien de vérification vous sera envoyé");
        return "redirect:/login";
    }

    private SimpleMailMessage ResendVerificationTokenEmail(String contextPath, Verificationtoken newToken, Login login) {
      String confirmationUrl = contextPath + "/ConfirmerInscription?token=" + newToken.getToken();
      SimpleMailMessage email = new SimpleMailMessage();
      email.setSubject("PYKA - Confirmation du compte");
      email.setText("Bonjour " + login.getProfilId().getCivilite() + " " + login.getProfilId().getNom() + " " + login.getProfilId().getPrenom() + ", \r\n \r\n"
            + "Pour valider votre compte, il vous suffit de cliquer sur le lien suivant : \r\n \r\n"
            + confirmationUrl
            + "\r\n \r\nEquipe PYKA ");
      email.setFrom(env.getProperty("spring.mail.username"));
      email.setTo(login.getEmail());
      return email;
    }
    
    @RequestMapping(value="/Reinitialiser", method = RequestMethod.GET)
    public ModelAndView ReinitialiserMDPForm(){
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("Reinitialiser");
            return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value="/Reinitialiser", method = RequestMethod.POST)
    public String ReinitialiserMDP(@RequestBody TextNode email){//JSON STRING TO JAVA STRING
            ModelAndView modelAndView = new ModelAndView();
            Login login = loginService.findLoginByEmail(email.asText());
            if (login == null) {
                modelAndView.addObject("errormessage", "Email incorrect !");
                return "Email incorrect";
            }
            else{
                String mdp = genererMDPAlphaNum(8);
                final SimpleMailMessage sendmail = SendNewPwd(mdp,login);
                mailSender.send(sendmail);
                login.setPassword(bCryptPasswordEncoder.encode(mdp));
                loginService.AddLogin(login);
                return "Nous allons vous envoyez un mot de passe provisoire";
            }
    }

    private SimpleMailMessage SendNewPwd(String password, Login login) {
      SimpleMailMessage email = new SimpleMailMessage();
      email.setSubject("PYKA - Reinitialisation de votre Mot de passe");
      email.setText("Bonjour " + login.getProfilId().getCivilite() + " " + login.getProfilId().getNom() + " " + login.getProfilId().getPrenom() + ", \r\n \r\n"
            + "Vous pouvez dès a present vous connecter avec votre  : \r\n \r\n"
            + "Identifiant  : " + login.getEmail() +  "\r\n \r\n"
            + "Mot de passe  : " + password +  "\r\n \r\n"
            + "\r\n \r\nEquipe PYKA ");
      email.setFrom(env.getProperty("spring.mail.username"));
      email.setTo(login.getEmail());
      return email;
    }


    String genererMDPAlphaNum( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ ){
           sb.append( dictionnaire.charAt( srnd.nextInt(dictionnaire.length()) ) );
        }
        return sb.toString();
    }
        
        
        
        
    @ResponseBody
    @RequestMapping(value="/SirenExistante", method = RequestMethod.GET)
    public List<String> SirenList(){
            List<Profil> profilslist = profilService.findAllProfils();
            List<String> SirenExistante = new ArrayList<>();
            for (int i = 0; i < profilslist.size(); i++) {
               SirenExistante.add(profilslist.get(i).getSiren());
            }
            return SirenExistante;
    }
    
    @ResponseBody
    @RequestMapping(value="/TelephoneExistant", method = RequestMethod.GET)
    public List<String> TelephoneList(){
            List<Profil> profilslist = profilService.findAllProfils();
            List<String> TelephoneExistant = new ArrayList<>();
            for (int i = 0; i < profilslist.size(); i++) {
               TelephoneExistant.add(profilslist.get(i).getTelephone().replaceAll("..(?!$)", "$0 "));
            }
            return TelephoneExistant;
    }
    
    @ResponseBody
    @RequestMapping(value="/EmailExistant", method = RequestMethod.GET)
    public List<String> EmailList(){
            List<Profil> profilslist = profilService.findAllProfils();
            List<String> EmailExistant = new ArrayList<>();
            for (int i = 0; i < profilslist.size(); i++) {
               EmailExistant.add(profilslist.get(i).getEmail());
            }
            return EmailExistant;
    }
        
    
}

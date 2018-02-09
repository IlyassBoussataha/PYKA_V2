/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.controller;

import fr.pyka.lavage.model.Chat;
import fr.pyka.lavage.model.ChatLine;
import fr.pyka.lavage.model.Commande;
import fr.pyka.lavage.model.Creneaux;
import fr.pyka.lavage.model.Login;
import fr.pyka.lavage.model.Profil;
import fr.pyka.lavage.service.ChatLineService;
import fr.pyka.lavage.service.ChatService;
import fr.pyka.lavage.service.CommandeService;
import fr.pyka.lavage.service.CreneauxService;
import fr.pyka.lavage.service.LoginService;
import fr.pyka.lavage.service.ProfilService;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author Publicab
 */
@Controller
public class ClientController {
    
    @Autowired
    private LoginService loginService;
    @Autowired
    private CreneauxService CreneauxService;
    @Autowired
    private CommandeService CommandeService;
    @Autowired
    private ProfilService profilService;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private Environment env;
    @Autowired
    ApplicationEventPublisher eventPublisher;
    @Autowired
    private ChatLineService chatLineService;
    @Autowired
    private ChatService chatService;
    
    public String GetLoggedUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName(); //get logged in username
    }
    
    @RequestMapping(value="/Client/home", method = RequestMethod.GET)
    public ModelAndView landing(){
            ModelAndView modelAndView = new ModelAndView();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.setViewName("client/accueil");
            return modelAndView;
    }
    
    @RequestMapping(value="/Client/Tarifs", method = RequestMethod.GET)
    public ModelAndView tarifs(){
            ModelAndView modelAndView = new ModelAndView();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.setViewName("client/Tarifs");
            return modelAndView;
    }
    
    @RequestMapping(value="/Client/NouvelleCommande", method = RequestMethod.GET)
    public ModelAndView AddCommande(){
            ModelAndView modelAndView = new ModelAndView();
            List<Creneaux> ListCreneaux = CreneauxService.findAllCreneaux();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("ListCreneaux", ListCreneaux);
            modelAndView.setViewName("client/NouvelleCommande");
            return modelAndView;
    }
    
    @RequestMapping(value="/Client/NouvelleCommande", method = RequestMethod.POST)
    public ModelAndView AddCommandePOST(final HttpServletRequest request,@RequestParam("stringdate") @DateTimeFormat(pattern="dd/MM/yyyy") Date date,
            @RequestParam("selectcreneau") int creneauid,@RequestParam("selectvehicule") String typevehicule,@RequestParam("selectlavage") String typelavage,
            @RequestParam("adressemap") String adressemap,@RequestParam("selectpaiement") String modepaiement) throws ParseException{
        
            ModelAndView modelAndView = new ModelAndView();
            System.out.println("fr.pyka.lavage.controller.ClientController.AddCommandePOST() : " + GetLoggedUser());
            SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
            Date dateformated = formatter.parse(formatter.format(date));
            System.out.println("fr.pyka.lavage.controller.ClientController.AddCommandePOST() : " + dateformated);
            System.out.println("fr.pyka.lavage.controller.ClientController.AddCommandePOST() : " + creneauid);
            System.out.println("fr.pyka.lavage.controller.ClientController.AddCommandePOST() : " + typevehicule);
            System.out.println("fr.pyka.lavage.controller.ClientController.AddCommandePOST() : " + typelavage);
            System.out.println("fr.pyka.lavage.controller.ClientController.AddCommandePOST() : " + adressemap);
            System.out.println("fr.pyka.lavage.controller.ClientController.AddCommandePOST() : " + modepaiement);
            Commande Commande = new Commande();
            Commande.setClientId(profilService.findProfilByEmail(GetLoggedUser()));
            Commande.setDate(dateformated);
            Commande.setCreneau(CreneauxService.findCreneauxById(creneauid));
            Commande.setVehicule(typevehicule);
            Commande.setTypeLavage(typelavage);
            Commande.setAdresse(adressemap);
            Commande.setNouvelle(true);
            Commande.setModePaiement(modepaiement);
            
            CommandeService.AddCommande(Commande);
            
            final String appUrl = "http://" + request.getServerName() + request.getContextPath();
            final SimpleMailMessage email = CommandeNotifyClient(appUrl, Commande, Commande.getClientId().getLogin());
            mailSender.send(email);
            
            return new ModelAndView(new RedirectView("NouvelleCommande"));
    }
    
    @RequestMapping(value="/Client/ListeNouvellesCommandes", method = RequestMethod.GET)
    public ModelAndView ListNewCommande(){
            ModelAndView modelAndView = new ModelAndView();
            List<Commande> ListNouvellesCommandes = CommandeService.findAllNouvellesCommandesByClient(profilService.findProfilByEmail(GetLoggedUser()));
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("ListNouvellesCommandes", ListNouvellesCommandes);
            modelAndView.addObject("nbCommandes", ListNouvellesCommandes.size());
            modelAndView.setViewName("client/ListeNouvellesCommandes");
            return modelAndView;
    }
    
    @RequestMapping(value="/Client/ModifierCommande/{id}", method = RequestMethod.GET)
    public ModelAndView EditCommande(@PathVariable("id") int id){
            ModelAndView modelAndView = new ModelAndView();
            Commande commande = CommandeService.findCommandeById(id);
            List<Creneaux> ListCreneaux = CreneauxService.findAllCreneaux();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("Commande", commande);
            modelAndView.addObject("ListCreneaux", ListCreneaux);
            modelAndView.setViewName("client/ModifierCommande");
            return modelAndView;
    }
    
    @RequestMapping(value="/Client/ModifierCommande", method = RequestMethod.POST)
    public ModelAndView EditCommandePOST(final HttpServletRequest request,@Valid Commande commande, BindingResult bindingResult,@RequestParam("stringdate") @DateTimeFormat(pattern="dd/MM/yyyy") Date date,
            @RequestParam("selectcreneau") int creneauid) throws ParseException{
            ModelAndView modelAndView = new ModelAndView();
            System.out.println("fr.pyka.lavage.controller.ClientController.EditCommandePOST()" + commande);
            if (bindingResult.hasErrors()) {
                System.out.println("fr.pyka.lavage.controller.ClientController.EditCommandePOST()" + bindingResult.getFieldError());
            }else{
                Commande oldcommande = CommandeService.findCommandeById(commande.getId());
                SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd");
                Date dateformated = formatter.parse(formatter.format(date));
                System.out.println("fr.pyka.lavage.controller.ClientController.EditCommandePOST()dateformated :" + oldcommande);
                System.out.println("fr.pyka.lavage.controller.ClientController.EditCommandePOST()dateformated :" + dateformated);
                commande.setDate(dateformated);
                commande.setClientId(oldcommande.getClientId());
                commande.setCreneau(CreneauxService.findCreneauxById(creneauid));
                commande.setNouvelle(true);
                
                CommandeService.AddCommande(commande);
                final String appUrl = "http://" + request.getServerName() + request.getContextPath();
                final SimpleMailMessage email = ModifiyCommandeNotifyClient(appUrl, oldcommande, oldcommande.getClientId().getLogin());
                mailSender.send(email);
                final SimpleMailMessage email3 = CommandeNotifyClient(appUrl, commande, commande.getClientId().getLogin());
                mailSender.send(email3);
            }
            return new ModelAndView(new RedirectView("ListeNouvellesCommandes"));
    }
    
    @RequestMapping(value="/Client/ListeCommandesEnCours", method = RequestMethod.GET)
    public ModelAndView ListCommandesEnCours() throws ParseException{
            ModelAndView modelAndView = new ModelAndView();
            List<Commande> ListCommandesAssigner = CommandeService.findAllCommandesAssignerByClient(profilService.findProfilByEmail(GetLoggedUser()));
            List<Commande> ListCommandesEnCours = new ArrayList<>();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            Date today = dateFormat.parse(dateFormat.format(date));
            for (int i = 0; i < ListCommandesAssigner.size(); i++) {
                if(ListCommandesAssigner.get(i).getDate().compareTo(today) == 0){
                    ListCommandesEnCours.add(ListCommandesAssigner.get(i));
                }
            }
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("ListCommandesEnCours", ListCommandesEnCours);
            modelAndView.addObject("nbCommandes", ListCommandesEnCours.size());
            modelAndView.setViewName("client/ListeCommandesEnCours");
            return modelAndView;
    }
    
    @RequestMapping(value="/Client/ListeCommandesEffectuees", method = RequestMethod.GET)
    public ModelAndView ListCommandesEffectuees(){
            ModelAndView modelAndView = new ModelAndView();
            List<Commande> ListCommandesEffectuer = CommandeService.findAllCommandesEffectuerByClient(profilService.findProfilByEmail(GetLoggedUser()));
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("ListCommandesEffectuer", ListCommandesEffectuer);
            modelAndView.addObject("nbCommandes", ListCommandesEffectuer.size());
            modelAndView.setViewName("client/ListeCommandesEffectuees");
            return modelAndView;
    }
    
    @RequestMapping(value="/Client/ListeCommandesAnnulees", method = RequestMethod.GET)
    public ModelAndView ListCommandesAnnuler(){
            ModelAndView modelAndView = new ModelAndView();
            List<Commande> ListCommandesAnnuler = CommandeService.findAllCommandesAnnulerByClient(profilService.findProfilByEmail(GetLoggedUser()));
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("ListCommandesAnnuler", ListCommandesAnnuler);
            modelAndView.addObject("nbCommandes", ListCommandesAnnuler.size());
            modelAndView.setViewName("client/ListeCommandesAnnulees");
            return modelAndView;
    }
    
    @ResponseBody
    @RequestMapping(value="/Client/AnnulerCommande", method = RequestMethod.POST)
    public String RemoveCommande(@RequestBody int id) throws ParseException{
            ModelAndView modelAndView = new ModelAndView();
            Commande commande = CommandeService.findCommandeById(id);
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            //int hour = cal.get(Calendar.HOUR_OF_DAY);*/
            System.out.println("fr.pyka.lavage.controller.ClientController.RemoveCreneau() :" + commande.getCreneau().getHeureDebut());
            
            
            DateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
            Date date2 = new Date();
            Date mnt = dateFormat2.parse(dateFormat2.format(date2));
            Date mnt2 = dateFormat2.parse(dateFormat2.format(date2));
            /*mnt.setHours(cal.get(Calendar.HOUR_OF_DAY));
            mnt.setMinutes(cal.get(Calendar.MINUTE));
            mnt.setSeconds(cal.get(Calendar.SECOND));*/
            mnt.setHours(9);
            mnt.setMinutes(00);
            mnt.setSeconds(00);
            
            mnt2.setHours(7);
            mnt2.setMinutes(02);
            mnt2.setSeconds(00);
            
            /*System.out.println("fr.pyka.lavage.controller.ClientController.RemoveCommande() :" + mnt);
            System.out.println("fr.pyka.lavage.controller.ClientController.RemoveCommande() :" + commande.getCreneau().getHeureDebut().compareTo(mnt));
            System.out.println("fr.pyka.lavage.controller.ClientController.RemoveCommande() :" +Duration.between(toInstant, mnt.toInstant()).toMinutes());
            System.out.println("fr.pyka.lavage.controller.ClientController.RemoveCommande() :" +Duration.between(toInstant, mnt2.toInstant()).toMinutes());*/
            
            long diff = mnt.getTime() - commande.getCreneau().getHeureDebut().getTime();
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            System.out.print(diffHours + " hours, ");
            System.out.println(diffMinutes + " minutes, ");
            
            LocalDate dateCommande = Instant.ofEpochMilli(commande.getDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate today = Instant.ofEpochMilli(cal.getTime().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            //LocalDateTime d2 = LocalDateTime.from(today.toInstant());
            long days = DAYS.between(dateCommande, today);
            
            if(days <= -1){
                System.out.println("fr.pyka.lavage.controller.ClientController.RemoveCommande(days) : Commande annuler avec succès ! " + days);
                commande.setNouvelle(false);
                commande.setAssigner(false);
                commande.setEffectuer(false);
                commande.setLaveurId(null);
                commande.setAnnuler(true);
                CommandeService.AddCommande(commande);
                return "Commande annuler avec succès !";
            }
            else if(days == 0){
                if(diffHours < -1 || (diffHours == -1 && diffMinutes <= 0)){
                    System.out.println("fr.pyka.lavage.controller.ClientController.RemoveCommande() : Commande annuler avec succès ! " + days);
                    commande.setNouvelle(false);
                    commande.setAssigner(false);
                    commande.setEffectuer(false);
                    commande.setLaveurId(null);
                    commande.setAnnuler(true);
                    CommandeService.AddCommande(commande);
                    return "Commande annuler avec succès !";
                }
            }
            /*
            System.out.println("fr.pyka.lavage.controller.ClientController.RemoveCommande()) : " + days);
            System.out.println("fr.pyka.lavage.controller.ClientController.RemoveCommande()) : " + days2);
            if(diffHours < -1 || (diffHours == -1 && diffMinutes <= 0)){
                System.out.println("fr.pyka.lavage.controller.ClientController.RemoveCommande() : Commande annuler avec succès !");
            }
            
            long diff2 =  mnt2.getTime() - commande.getCreneau().getHeureDebut().getTime();
            long diffMinutes2 = diff2 / (60 * 1000) % 60;
            long diffHours2 = diff2 / (60 * 60 * 1000) % 24;
            System.out.print(diffHours2 + " hours, ");
            System.out.print(diffMinutes2 + " minutes, ");
            
            if(diffHours2 < -1 || (diffHours2 == -1 && diffMinutes2 <= 0)){
                System.out.println("fr.pyka.lavage.controller.ClientController.RemoveCommande2() : Commande annuler avec succès !");
            }*/
            /*commande.setNouvelle(false);
            commande.setAssigner(false);
            commande.setEffectuer(false);
            commande.setLaveurId(null);
            commande.setAnnuler(true);
            CommandeService.AddCommande(commande);*/
            return "Commande non annulé !";
    }
    
    @RequestMapping(value="/Client/ModifierProfil", method = RequestMethod.GET)
    public ModelAndView ModifyProfil(){
            ModelAndView modelAndView = new ModelAndView();
            Profil profil = profilService.findProfilByEmail(GetLoggedUser());
            Login login = profil.getLogin();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("profil", profil);
            modelAndView.addObject("login", login);
            modelAndView.setViewName("client/ModifierProfil");
            return modelAndView;
    }
    
    @RequestMapping(value="/Client/ModifierProfil", method = RequestMethod.POST)
    public ModelAndView ModifyProfilPOST(@Valid Profil profil, BindingResult bindingResult, Login login,@RequestParam("file") MultipartFile[] files, WebRequest request) throws IOException, ParseException {
            ModelAndView modelAndView = new ModelAndView();
            Profil oldProfil = profilService.findProfilById(profil.getId());
            Login oldlogin = loginService.findLoginByEmail(oldProfil.getEmail());
            if (bindingResult.hasErrors()) {
                    modelAndView.addObject("errorMessage", bindingResult.getFieldError());
                    modelAndView.setViewName("client/ModifierProfil");
                    System.out.println("fr.pyka.lavage.controller.ClientController.ModifyProfilPOST() : " + bindingResult.getFieldError());
            } else {
                    //String path = new File("src/main/resources/static/assets/img/").getAbsolutePath();
                    String base = System.getProperty("catalina.base");
                    String path = base.concat("/webapps/uploads/images/");
                    if(files[0].isEmpty()){
                        profil.setPhoto(oldProfil.getPhoto());
                    }else{
                        String ext = FilenameUtils.getExtension(files[0].getOriginalFilename());
                        String oldname = FilenameUtils.removeExtension(files[0].getOriginalFilename());
                        String newFilename = DigestUtils.md5Hex( oldname );
                        System.out.println( ext + "-" + oldname + " " + files[0].getOriginalFilename() + " -> ");
                        File tmp = new File(path,newFilename + "." + ext);
                        files[0].transferTo(tmp);
                        profil.setPhoto(newFilename + "." + ext);
                    }
                    /*
                    if(files[1].isEmpty()){
                        profil.setVtc(oldProfil.getVtc());
                    }else{
                        String ext = FilenameUtils.getExtension(files[1].getOriginalFilename());
                        String oldname = FilenameUtils.removeExtension(files[1].getOriginalFilename());
                        String newFilename = DigestUtils.md5Hex( oldname );
                        System.out.println( ext + "-" + oldname + " " + files[1].getOriginalFilename() + " -> ");
                        File tmp = new File(path,newFilename + "." + ext);
                        files[1].transferTo(tmp);
                        profil.setVtc(newFilename + "." + ext);
                    }
                    */
                    profil.setTelephone(profil.getTelephone().replaceAll("\\s+",""));
                    boolean changepwd = true;
                    if(login.getPassword().equals(oldlogin.getPassword())){ 
                        changepwd = false;
                    }
                    profilService.UpdateProfil(oldProfil.getId(), profil, login, false, changepwd);
                    modelAndView.addObject("successMessage", "Compte modifié avec succèss");
                    modelAndView.addObject("profil", profil);
                    modelAndView.addObject("login", login);
                    return new ModelAndView(new RedirectView("ModifierProfil"));

            }
            return modelAndView;
    }
    
    
    @RequestMapping(value="/Client/MessagesEnvoyee", method = RequestMethod.GET)
	public ModelAndView MailOutBox(){
		ModelAndView modelAndView = new ModelAndView();
                Profil sender = profilService.findProfilByEmail(GetLoggedUser());
                List<ChatLine> chatLineList = chatLineService.findChatLineBySenderId(sender);
                List<Chat> chatList = new ArrayList<>();
                
                List<ChatLine> chatLineList2 = new ArrayList<>();
                if(!chatLineList.isEmpty()){
                    chatLineList2.add(chatLineList.get(0));
                    chatList.add(chatLineList.get(0).getChatId());
                    for (int i = 1; i < chatLineList.size(); i++) {
                        if(!chatList.contains(chatLineList.get(i).getChatId())){
                            chatLineList2.add(chatLineList.get(i));
                            chatList.add(chatLineList.get(i).getChatId());
                        }
                    }
                }
                
                Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
                modelAndView.addObject("user", loggedUser);
                modelAndView.addObject("chatLineList", chatLineList2);
		modelAndView.setViewName("client/Messages");
		return modelAndView;
	}
        
        @RequestMapping(value="/Client/MessagesRecu", method = RequestMethod.GET)
	public ModelAndView MailInBox(){
		ModelAndView modelAndView = new ModelAndView();
                Profil receiver = profilService.findProfilByEmail(GetLoggedUser());
                List<ChatLine> chatLineList = chatLineService.findChatLineByReceiverId(receiver);
                List<Chat> chatList = new ArrayList<>();
                
                List<ChatLine> chatLineList2 = new ArrayList<>();
                if(!chatLineList.isEmpty()){
                    chatLineList2.add(chatLineList.get(0));
                    chatList.add(chatLineList.get(0).getChatId());
                    for (int i = 1; i < chatLineList.size(); i++) {
                        if(!chatList.contains(chatLineList.get(i).getChatId())){
                            chatLineList2.add(chatLineList.get(i));
                            chatList.add(chatLineList.get(i).getChatId());
                        }
                    }
                }
                
                Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
                modelAndView.addObject("user", loggedUser);
                modelAndView.addObject("chatLineList", chatLineList2);
		modelAndView.setViewName("client/Messages");
		return modelAndView;
	}
        
        @RequestMapping(value="/Client/NouveauMessage", method = RequestMethod.GET)
	public ModelAndView ComposeMail(){
		ModelAndView modelAndView = new ModelAndView();
                ChatLine chatLine = new ChatLine();
                List<Profil> profilList = profilService.findAllProfilsValider();
                Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
                modelAndView.addObject("user", loggedUser);
                modelAndView.addObject("chatLine", chatLine);
                modelAndView.addObject("profilList", profilList);
		modelAndView.setViewName("client/NouveauMessage");
		return modelAndView;
	}
        
        @RequestMapping(value="/Client/NouveauMessage", method = RequestMethod.POST)
	public ModelAndView ComposeMailPost(@Valid @ModelAttribute("chatLine") ChatLine chatLine, BindingResult bindingResult){
		ModelAndView modelAndView = new ModelAndView();
                Profil sender = profilService.findProfilByEmail(GetLoggedUser());
                Profil receiver = profilService.findProfilByEmail("ilyass.bou@gmail.com");
                Chat chat = new Chat();
                chatLine.setSenderId(sender);
                chatLine.setReceiverId(receiver);
                chatLine.setDate(new Date());
                chatLine.setChatId(chat);
                chatLine.setVu(false);
                chatLineService.AddChatLine(chat, chatLine);
                List<Profil> profilList = profilService.findAllProfilsValider();
                Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
                modelAndView.addObject("user", loggedUser);
                modelAndView.addObject("chatLine", chatLine);
                modelAndView.addObject("profilList", profilList);
		modelAndView.setViewName("client/NouveauMessage");
		return modelAndView;
	}
        
        @RequestMapping(value="/Client/ConsulterMessage/{id}", method = RequestMethod.GET)
	public ModelAndView ViewMailGET(@PathVariable("id") Long id){
		ModelAndView modelAndView = new ModelAndView();
                ChatLine chatLine = chatLineService.findChatLineById(id);
                List<ChatLine> chatLineList = chatLineService.findChatLineByChatId(chatLine.getChatId());
                chatLine.setVu(true);
                chatLineService.AddChatLine(chatLine.getChatId(), chatLine);
                Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
                modelAndView.addObject("user", loggedUser);
                modelAndView.addObject("chatLineList", chatLineList);
		modelAndView.setViewName("client/ConsulterMessage");
		return modelAndView;
	}
        
        @RequestMapping(value="/Client/RepondreMessage", method = RequestMethod.POST)
	public ModelAndView ReplyMailPost(@Valid @ModelAttribute("chatLine2") ChatLine chatLine2, BindingResult bindingResult, @RequestParam("messageprofilId") int messageprofilId,@RequestParam("messageChatLineId") Long messageChatLineId){
		ModelAndView modelAndView = new ModelAndView();
                Profil sender = profilService.findProfilByEmail(GetLoggedUser());
                Profil receiver = profilService.findProfilById(messageprofilId);
                chatLine2.setSenderId(sender);
                chatLine2.setReceiverId(receiver);
                chatLine2.setDate(new Date());
                ChatLine chatLinetmp = chatLineService.findChatLineById(messageChatLineId);
                chatLine2.setChatId(chatLinetmp.getChatId());
                chatLine2.setVu(false);
                chatLineService.AddChatLine(chatLine2.getChatId(), chatLine2);
		return new ModelAndView("redirect:/Client/MessagesEnvoyee");
	}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private SimpleMailMessage CommandeNotifyClient(String contextPath, Commande commande, Login login) {
      SimpleMailMessage email = new SimpleMailMessage();
      email.setSubject("PYKA - Confirmation d'une nouvelle commande");
      email.setText("Bonjour " + login.getProfilId().getCivilite() + " " + login.getProfilId().getNom() + " " + login.getProfilId().getPrenom() + ", \r\n \r\n"
            + "Votre commande pour le "+ commande.getDate() +", Du "+ commande.getCreneau().getHeureDebut() +" à "+ commande.getCreneau().getHeureFin() +"a bien été pris en charge par l'un de nos laveurs. \r\n \r\n"
            + "\r\n \r\nEquipe PYKA ");
      email.setFrom(env.getProperty("spring.mail.username"));
      email.setTo(login.getEmail());
      return email;
    }
    
    private SimpleMailMessage ModifiyCommandeNotifyClient(String contextPath, Commande commande, Login login) {
      SimpleMailMessage email = new SimpleMailMessage();
      email.setSubject("PYKA - Modification de votre commande");
      email.setText("Bonjour " + login.getProfilId().getCivilite() + " " + login.getProfilId().getNom() + " " + login.getProfilId().getPrenom() + ", \r\n \r\n"
            + "Votre commande pour le "+ commande.getDate() +", Du "+ commande.getCreneau().getHeureDebut() +" à "+ commande.getCreneau().getHeureFin() +"a bien été modifier. \r\n \r\n"
            + "Pour plus de détails veuillez consulter votre espace client. \r\n \r\n"
            + "\r\n \r\nEquipe PYKA ");
      email.setFrom(env.getProperty("spring.mail.username"));
      email.setTo(login.getEmail());
      return email;
    }
    
} 

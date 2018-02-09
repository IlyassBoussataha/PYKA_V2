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
import fr.pyka.lavage.model.Verificationtoken;
import fr.pyka.lavage.registration.OnRegistrationCompleteEvent;
import fr.pyka.lavage.service.ChatLineService;
import fr.pyka.lavage.service.ChatService;
import fr.pyka.lavage.service.CommandeService;
import fr.pyka.lavage.service.CreneauxService;
import fr.pyka.lavage.service.LoginService;
import fr.pyka.lavage.service.ProfilService;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.toIntExact;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
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
public class AdminController {
    
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
    
    @RequestMapping(value="/Admin/home", method = RequestMethod.GET)
    public ModelAndView landing(){
            ModelAndView modelAndView = new ModelAndView();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.setViewName("admin/dashboard");
            return modelAndView;
    }
    
    
    @RequestMapping(value="/Admin/AjouterCreneau", method = RequestMethod.GET)
    public ModelAndView AddCreneau(){
            ModelAndView modelAndView = new ModelAndView();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.setViewName("admin/AjouterCreneau");
            return modelAndView;
    }
    
    @RequestMapping(value="/Admin/AjouterCreneau", method = RequestMethod.POST)
    public ModelAndView AddCreneauPOST(@RequestParam("heuredebut") String HeureDebut,@RequestParam("heurefin") String HeureFin) throws ParseException{
            ModelAndView modelAndView = new ModelAndView();
            LocalTime HeureDebutlt = LocalTime.parse(HeureDebut);
            LocalTime HeureFinlt = LocalTime.parse(HeureFin);
            /*DateFormat formatter = new SimpleDateFormat("HH:mm");
            Date HeureDebutlt = (Date)formatter.parse(HeureDebut);*/
            System.out.println("fr.pyka.lavage.controller.AdminController.AddCreneauPOST() : " + HeureDebutlt);
            System.out.println("fr.pyka.lavage.controller.AdminController.AddCreneauPOST() : " + HeureFinlt);
            Creneaux creneau = new Creneaux();
            creneau.setHeureDebut(Time.valueOf(HeureDebutlt));
            creneau.setHeureFin(Time.valueOf(HeureFinlt));
            CreneauxService.AddCreneaux(creneau);
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.setViewName("admin/AjouterCreneau");
            return modelAndView;
    }
    
    @RequestMapping(value="/Admin/ListeCreneaux", method = RequestMethod.GET)
    public ModelAndView ListCreneaux(){
            ModelAndView modelAndView = new ModelAndView();
            List<Creneaux> ListCreneaux = CreneauxService.findAllCreneaux();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("ListCreneaux", ListCreneaux);
            modelAndView.setViewName("admin/ListeCreneaux");
            return modelAndView;
    }
    
    @ResponseBody
    @RequestMapping(value="/Admin/SupprimerCreneau", method = RequestMethod.POST)
    public String RemoveCreneau(@RequestBody int id){
            ModelAndView modelAndView = new ModelAndView();
            Creneaux Creneau = CreneauxService.findCreneauxById(id);
            CreneauxService.RemoveCreneau(Creneau);
            return "Créneau supprimer avec succès !";
    }
    
    @RequestMapping(value="/Admin/NouvelleCommande", method = RequestMethod.GET)
    public ModelAndView AddCommande(){
            ModelAndView modelAndView = new ModelAndView();
            List<Profil> ListClients = profilService.findAllClientsValider();
            List<Profil> ListLaveurs = profilService.findAllLaveursValiderAndNotExclu();
            List<Creneaux> ListCreneaux = CreneauxService.findAllCreneaux();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("ListClients", ListClients);
            modelAndView.addObject("ListLaveurs", ListLaveurs);
            modelAndView.addObject("ListCreneaux", ListCreneaux);
            modelAndView.setViewName("admin/NouvelleCommande");
            return modelAndView;
    }
    
    @RequestMapping(value="/Admin/NouvelleCommande", method = RequestMethod.POST)
    public ModelAndView AddCommandePOST(final HttpServletRequest request,@RequestParam("selectclient") int clientid,
            @RequestParam("stringdate") @DateTimeFormat(pattern="dd/MM/yyyy") Date date,
            @RequestParam("selectcreneau") int creneauid,@RequestParam("selectvehicule") String typevehicule,@RequestParam("selectlavage") String typelavage,
            @RequestParam("adressemap") String adressemap,@RequestParam("affecterlaveur") String affecterlaveur,
            @RequestParam(required = false,name="selectlaveur") int laveurid,@RequestParam("selectpaiement") String modepaiement) throws ParseException{
        
            ModelAndView modelAndView = new ModelAndView();
            System.out.println("fr.pyka.lavage.controller.AdminController.AddCommandePOST() : " + clientid);
            SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
            Date dateformated = formatter.parse(formatter.format(date));
            System.out.println("fr.pyka.lavage.controller.AdminController.AddCommandePOST() : " + dateformated);
            System.out.println("fr.pyka.lavage.controller.AdminController.AddCommandePOST() : " + creneauid);
            System.out.println("fr.pyka.lavage.controller.AdminController.AddCommandePOST() : " + typevehicule);
            System.out.println("fr.pyka.lavage.controller.AdminController.AddCommandePOST() : " + typelavage);
            System.out.println("fr.pyka.lavage.controller.AdminController.AddCommandePOST() : " + adressemap);
            System.out.println("fr.pyka.lavage.controller.AdminController.AddCommandePOST() : " + affecterlaveur);
            System.out.println("fr.pyka.lavage.controller.AdminController.AddCommandePOST() : " + laveurid);
            System.out.println("fr.pyka.lavage.controller.AdminController.AddCommandePOST() : " + modepaiement);
            Commande Commande = new Commande();
            Commande.setClientId(profilService.findProfilById(clientid));
            Commande.setDate(dateformated);
            Commande.setCreneau(CreneauxService.findCreneauxById(creneauid));
            Commande.setVehicule(typevehicule);
            Commande.setTypeLavage(typelavage);
            Commande.setAdresse(adressemap);
            if("Oui".equals(affecterlaveur)){
                Commande.setLaveurId(profilService.findProfilById(laveurid));
                Commande.setAssigner(true);
            }
            else Commande.setNouvelle(true);
            Commande.setModePaiement(modepaiement);
            
            CommandeService.AddCommande(Commande);
            
            final String appUrl = "http://" + request.getServerName() + request.getContextPath();
            final SimpleMailMessage email = CommandeNotifyClient(appUrl, Commande, Commande.getClientId().getLogin());
            mailSender.send(email);
            if("Oui".equals(affecterlaveur)){
                final SimpleMailMessage email2 = CommandeNotifyLaveur(appUrl, Commande, Commande.getLaveurId().getLogin());
                mailSender.send(email2);
            }
            
            modelAndView.setViewName("admin/NouvelleCommande");
            return modelAndView;
    }
    
    @RequestMapping(value="/Admin/ListeNouvellesCommandes", method = RequestMethod.GET)
    public ModelAndView ListNewCommande(){
            ModelAndView modelAndView = new ModelAndView();
            List<Commande> ListNouvellesCommandes = CommandeService.findAllNouvellesCommandes();
            List<Profil> ListLaveurs = profilService.findAllLaveursValiderAndNotExclu();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("ListNouvellesCommandes", ListNouvellesCommandes);
            modelAndView.addObject("ListLaveurs", ListLaveurs);
            modelAndView.addObject("nbCommandes", ListNouvellesCommandes.size());
            modelAndView.setViewName("admin/ListeNouvellesCommandes");
            return modelAndView;
    }
    
    @RequestMapping(value="/Admin/AssignerCommande", method = RequestMethod.POST)
    public ModelAndView AssignCommandePOST(final HttpServletRequest request,@RequestParam("commandeid") int commandeid,@RequestParam("selectlaveur") int laveurid){
            ModelAndView modelAndView = new ModelAndView();
            Commande Commande = CommandeService.findCommandeById(commandeid);
            Profil Laveur = profilService.findProfilById(laveurid);
            
            Commande.setLaveurId(Laveur);
            Commande.setAssigner(true);
            Commande.setNouvelle(false);
            CommandeService.AddCommande(Commande);
            final String appUrl = "http://" + request.getServerName() + request.getContextPath();
            final SimpleMailMessage email = CommandeNotifyClient(appUrl, Commande, Commande.getClientId().getLogin());
            mailSender.send(email);
            final SimpleMailMessage email2 = CommandeNotifyLaveur(appUrl, Commande, Commande.getLaveurId().getLogin());
            mailSender.send(email2);
            
            List<Commande> ListNouvellesCommandes = CommandeService.findAllNouvellesCommandes();
            List<Profil> ListLaveurs = profilService.findAllLaveursValiderAndNotExclu();
            modelAndView.addObject("ListNouvellesCommandes", ListNouvellesCommandes);
            modelAndView.addObject("ListLaveurs", ListLaveurs);
            return new ModelAndView(new RedirectView("ListeNouvellesCommandes"));
    }
    
    
    @RequestMapping(value="/Admin/ListeCommandesAssignee", method = RequestMethod.GET)
    public ModelAndView ListCommandeAssigned(){
            ModelAndView modelAndView = new ModelAndView();
            List<Commande> ListCommandesAssigner = CommandeService.findAllCommandesAssigner();
            List<Profil> ListLaveurs = profilService.findAllLaveursValiderAndNotExclu();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("ListCommandesAssigner", ListCommandesAssigner);
            modelAndView.addObject("ListLaveurs", ListLaveurs);
            modelAndView.addObject("nbCommandes", ListCommandesAssigner.size());
            modelAndView.setViewName("admin/ListeCommandesAssignee");
            return modelAndView;
    }
    
    @RequestMapping(value="/Admin/ReAssignerCommande", method = RequestMethod.POST)
    public ModelAndView ReAssignCommandePOST(final HttpServletRequest request,@RequestParam("commandeid") int commandeid,@RequestParam("laveurid") int ancienlaveurid
            ,@RequestParam("selectlaveur") int laveurid){
            ModelAndView modelAndView = new ModelAndView();
            Commande Commande = CommandeService.findCommandeById(commandeid);
            Profil Laveur = profilService.findProfilById(laveurid);
            Profil ancienLaveur = profilService.findProfilById(ancienlaveurid);
            
            Commande.setLaveurId(Laveur);
            CommandeService.AddCommande(Commande);
            final String appUrl = "http://" + request.getServerName() + request.getContextPath();
            final SimpleMailMessage email = CommandeNotifyLaveur(appUrl, Commande, Commande.getLaveurId().getLogin());
            mailSender.send(email);
            final SimpleMailMessage email2 = CommandeNotifyAncienLaveur(appUrl, Commande, ancienLaveur.getLogin());
            mailSender.send(email2);
            
            List<Commande> ListNouvellesCommandes = CommandeService.findAllNouvellesCommandes();
            List<Profil> ListLaveurs = profilService.findAllLaveursValiderAndNotExclu();
            modelAndView.addObject("ListNouvellesCommandes", ListNouvellesCommandes);
            modelAndView.addObject("ListLaveurs", ListLaveurs);
            return new ModelAndView(new RedirectView("ListeNouvellesCommandes"));
    }
    
    @ResponseBody
    @RequestMapping(value="/Admin/AnnulerCommande", method = RequestMethod.POST)
    public String RemoveCommande(@RequestBody int id) throws ParseException{
            ModelAndView modelAndView = new ModelAndView();
            Commande commande = CommandeService.findCommandeById(id);
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            //int hour = cal.get(Calendar.HOUR_OF_DAY);*/
            System.out.println("fr.pyka.lavage.controller.AdminController.RemoveCreneau() :" + commande.getCreneau().getHeureDebut());
            
            
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
                System.out.println("fr.pyka.lavage.controller.AdminController.RemoveCommande(days) : Commande annuler avec succès ! " + days);
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
                    System.out.println("fr.pyka.lavage.controller.AdminController.RemoveCommande() : Commande annuler avec succès ! " + days);
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
    
    @RequestMapping(value="/Admin/ModifierCommande/{id}", method = RequestMethod.GET)
    public ModelAndView EditCommande(@PathVariable("id") int id){
            ModelAndView modelAndView = new ModelAndView();
            Commande commande = CommandeService.findCommandeById(id);
            List<Creneaux> ListCreneaux = CreneauxService.findAllCreneaux();
            List<Profil> ListLaveurs = profilService.findAllLaveursValiderAndNotExclu();
            List<Profil> ListClients = profilService.findAllClientsValider();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("Commande", commande);
            modelAndView.addObject("ListCreneaux", ListCreneaux);
            modelAndView.addObject("ListLaveurs", ListLaveurs);
            modelAndView.addObject("ListClients", ListClients);
            modelAndView.setViewName("admin/ModifierCommande");
            return modelAndView;
    }
    
    @RequestMapping(value="/Admin/ModifierCommande", method = RequestMethod.POST)
    public ModelAndView EditCommandePOST(final HttpServletRequest request,@Valid Commande commande, BindingResult bindingResult,@RequestParam("selectclient") int clientid,
            @RequestParam("stringdate") @DateTimeFormat(pattern="dd/MM/yyyy") Date date,@RequestParam("selectcreneau") int creneauid,@RequestParam("affecterlaveur") String affecterlaveur,@RequestParam("selectlaveur") int laveurid) throws ParseException{
            ModelAndView modelAndView = new ModelAndView();
            System.out.println("fr.pyka.lavage.controller.AdminController.EditCommandePOST()" + commande);
            if (bindingResult.hasErrors()) {
                System.out.println("fr.pyka.lavage.controller.AdminController.EditCommandePOST()" + bindingResult.getFieldError());
            }else{
                Commande oldcommande = CommandeService.findCommandeById(commande.getId());
                SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
                Date dateformated = formatter.parse(formatter.format(date));
                commande.setClientId(profilService.findProfilById(clientid));
                commande.setDate(dateformated);
                commande.setCreneau(CreneauxService.findCreneauxById(creneauid));
                if("Oui".equals(affecterlaveur)){
                    commande.setLaveurId(profilService.findProfilById(laveurid));
                    commande.setAssigner(true);
                }
                else{ 
                    commande.setLaveurId(null);
                    commande.setNouvelle(true);
                }
                CommandeService.AddCommande(commande);
                final String appUrl = "http://" + request.getServerName() + request.getContextPath();
                final SimpleMailMessage email = ModifiyCommandeNotifyClient(appUrl, oldcommande, oldcommande.getClientId().getLogin());
                mailSender.send(email);
                final SimpleMailMessage email2 = ModifiyCommandeNotifyLaveur(appUrl, oldcommande, oldcommande.getLaveurId().getLogin());
                mailSender.send(email2);
                final SimpleMailMessage email3 = CommandeNotifyClient(appUrl, commande, commande.getClientId().getLogin());
                mailSender.send(email3);
                final SimpleMailMessage email4 = CommandeNotifyLaveur(appUrl, commande, commande.getLaveurId().getLogin());
                mailSender.send(email4);
                /*System.out.println("fr.pyka.lavage.controller.AdminController.EditCommandePOST() : " + commande.getClientId());
                System.out.println("fr.pyka.lavage.controller.AdminController.EditCommandePOST() : " + commande.getDate());
                System.out.println("fr.pyka.lavage.controller.AdminController.EditCommandePOST() : " + commande.getCreneau());
                System.out.println("fr.pyka.lavage.controller.AdminController.EditCommandePOST() : " + commande.getVehicule());
                System.out.println("fr.pyka.lavage.controller.AdminController.EditCommandePOST() : " + commande.getTypeLavage());
                System.out.println("fr.pyka.lavage.controller.AdminController.EditCommandePOST() : " + commande.getAdresse());
                System.out.println("fr.pyka.lavage.controller.AdminController.EditCommandePOST() : " + commande.getAssigner());
                System.out.println("fr.pyka.lavage.controller.AdminController.EditCommandePOST() : " + commande.getLaveurId());
                System.out.println("fr.pyka.lavage.controller.AdminController.EditCommandePOST() : " + commande.getModePaiement());*/
            }
            return new ModelAndView(new RedirectView("ModifierCommande/"+commande.getId()));
    }
    
    @RequestMapping(value="/Admin/ListeCommandesEnCours", method = RequestMethod.GET)
    public ModelAndView ListCommandesEnCours() throws ParseException{
            ModelAndView modelAndView = new ModelAndView();
            List<Commande> ListCommandesAssigner = CommandeService.findAllCommandesAssigner();
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
            modelAndView.setViewName("admin/ListeCommandesEnCours");
            return modelAndView;
    }
    
    @RequestMapping(value="/Admin/ListeCommandesEffectuees", method = RequestMethod.GET)
    public ModelAndView ListCommandesEffectuees(){
            ModelAndView modelAndView = new ModelAndView();
            List<Commande> ListCommandesEffectuer = CommandeService.findAllCommandesEffectuer();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("ListCommandesEffectuer", ListCommandesEffectuer);
            modelAndView.addObject("nbCommandes", ListCommandesEffectuer.size());
            modelAndView.setViewName("admin/ListeCommandesEffectuees");
            return modelAndView;
    }
    
    @RequestMapping(value="/Admin/ListeCommandesAnnulees", method = RequestMethod.GET)
    public ModelAndView ListCommandesAnnuler(){
            ModelAndView modelAndView = new ModelAndView();
            List<Commande> ListCommandesAnnuler = CommandeService.findAllCommandesAnnuler();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("ListCommandesAnnuler", ListCommandesAnnuler);
            modelAndView.addObject("nbCommandes", ListCommandesAnnuler.size());
            modelAndView.setViewName("admin/ListeCommandesAnnulees");
            return modelAndView;
    }
    
    @RequestMapping(value="/Admin/AjouterLaveur", method = RequestMethod.GET)
    public ModelAndView AddLaveur(){
            ModelAndView modelAndView = new ModelAndView();
            Profil profil = new Profil();
            Login login = new Login();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("profil", profil);
            modelAndView.addObject("login", login);
            modelAndView.setViewName("admin/AjouterLaveur");
            return modelAndView;
    }
    
    @RequestMapping(value="/Admin/AjouterLaveur", method = RequestMethod.POST)
    public ModelAndView AddLaveurPOST(@Valid Profil profil, BindingResult bindingResult, Login login,@RequestParam("file") MultipartFile[] files, WebRequest request,@RequestParam("stringdatedenaissance") @DateTimeFormat(pattern="dd/MM/yyyy") Date datedenaissance) throws IOException, ParseException {
            ModelAndView modelAndView = new ModelAndView();
            Profil profilExists = profilService.findProfilByEmail(profil.getEmail());
            if (profilExists != null) {
                    modelAndView.addObject("emailErrorMessage", "Email dèja existant !");
                    modelAndView.addObject("profil", profil);
                    modelAndView.addObject("login", login);
                    modelAndView.setViewName("admin/AjouterLaveur");
                    return modelAndView;
            }
            if (bindingResult.hasErrors()) {
                    modelAndView.addObject("errorMessage", bindingResult.getFieldError());
                    modelAndView.setViewName("inscription");
                    System.out.println("fr.pyka.lavage.controller.AdminController.AddLaveurPOST() : " + bindingResult.getFieldError());
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
                            if (i==3) profil.setCasierJudiciaire(newFilename + "." + ext);
                            if (i==4) profil.setKbis(newFilename + "." + ext);
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
                    modelAndView.setViewName("/Admin/InscriptionSuccess");
                    return InscriptionSuccess(true);//appelle fonction

            }
            return modelAndView;
    }
    
    @RequestMapping(value="/Admin/InscriptionSuccess", method = RequestMethod.GET)
    public ModelAndView InscriptionSuccess(boolean etat){
            ModelAndView modelAndView = new ModelAndView();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("etat", etat);
            modelAndView.setViewName("admin/InscriptionSuccess");
            return modelAndView;
    }
    
    @RequestMapping(value="/Admin/ListeLaveursActifs", method = RequestMethod.GET)
    public ModelAndView ListLaveursActifs(){
            ModelAndView modelAndView = new ModelAndView();
            List<Profil> ListLaveursValider = profilService.findAllLaveursValiderAndNotExclu();
            //List<Commande> ListCommandesEffectuer = CommandeService.findAllCommandesEffectuer();
            LocalDate today = LocalDate.now();
            System.out.println("Current date: " + today);
            //minus 2 week to the current date
            LocalDate next2Week = today.minus(2, ChronoUnit.WEEKS);
            Date date = java.sql.Date.valueOf(next2Week);
            System.out.println("Previous week: " + date);
            
            List<Commande> ListCommandesEffectuerAfterDate = CommandeService.findAllCommandesEffectuerAfterDate(date);
            System.out.println("ListCommandesEffectuerPour deux semaines précedentes : " + ListCommandesEffectuerAfterDate.size());
            
            List<Profil> ListLaveursActifs = new ArrayList<>();
            
            //Mise à jour du nombre des commandes réalisées sur les 2 dernières semaines
            for (int i = 0; i < ListLaveursValider.size(); i++) {
                ListLaveursValider.get(i).setLastCommandes(0);
                profilService.UpdateProfil(ListLaveursValider.get(i).getId(), ListLaveursValider.get(i), ListLaveursValider.get(i).getLogin(), false, false);
            }
            for (int i = 0; i < ListLaveursValider.size(); i++) {
                for (int j = 0; j < ListCommandesEffectuerAfterDate.size(); j++) {
                    if(ListCommandesEffectuerAfterDate.get(j).getLaveurId().getId() ==  ListLaveursValider.get(i).getId()){
                        ListLaveursValider.get(i).setLastCommandes(ListLaveursValider.get(i).getLastCommandes()+1);
                        profilService.UpdateProfil(ListLaveursValider.get(i).getId(), ListLaveursValider.get(i), ListLaveursValider.get(i).getLogin(), false, false);
                    }
                }
            }
            
            for (int i = 0; i < ListLaveursValider.size(); i++) {
                for (int j = 0; j < ListCommandesEffectuerAfterDate.size(); j++) {
                    if(ListCommandesEffectuerAfterDate.get(j).getLaveurId().getId() ==  ListLaveursValider.get(i).getId()){
                        if(!ListLaveursActifs.contains(ListLaveursValider.get(i))){
                            ListLaveursActifs.add(ListLaveursValider.get(i));
                        }
                    }
                }
            }
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("ListLaveursActifs", ListLaveursActifs);
            modelAndView.addObject("nbLaveurs", ListLaveursActifs.size());
            modelAndView.setViewName("admin/ListeLaveursActifs");
            return modelAndView;
    }
    
    
    @RequestMapping(value="/Admin/ListeLaveursInactifs", method = RequestMethod.GET)
    public ModelAndView ListLaveursInactifs(){
            ModelAndView modelAndView = new ModelAndView();
            List<Profil> ListLaveursValider = profilService.findAllLaveursValiderAndNotExclu();
            //List<Commande> ListCommandesEffectuer = CommandeService.findAllCommandesEffectuer();
            LocalDate today = LocalDate.now();
            System.out.println("Current date: " + today);
            //minus 2 week to the current date
            LocalDate next2Week = today.minus(2, ChronoUnit.WEEKS);
            Date date = java.sql.Date.valueOf(next2Week);
            System.out.println("Previous week: " + date);
            
            List<Commande> ListCommandesEffectuerAfterDate = CommandeService.findAllCommandesEffectuerAfterDate(date);
            System.out.println("ListCommandesEffectuerPour deux semaines précedentes : " + ListCommandesEffectuerAfterDate.size());
            
            List<Profil> ListLaveursInactifs = new ArrayList<>();
            boolean pasdecommande = true;
            
            for (int i = 0; i < ListLaveursValider.size(); i++) {
                for (int j = 0; j < ListCommandesEffectuerAfterDate.size(); j++) {
                    if(ListCommandesEffectuerAfterDate.get(j).getLaveurId().getId() ==  ListLaveursValider.get(i).getId()){
                        pasdecommande = false;
                    }
                }
                if(pasdecommande && !ListLaveursInactifs.contains(ListLaveursValider.get(i))){
                    ListLaveursInactifs.add(ListLaveursValider.get(i));
                }
                pasdecommande = true;
            }
            
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("ListLaveursInactifs", ListLaveursInactifs);
            modelAndView.addObject("nbLaveurs", ListLaveursInactifs.size());
            modelAndView.setViewName("admin/ListeLaveursInactifs");
            return modelAndView;
    }
    
    @RequestMapping(value="/Admin/ListeLaveursExclus", method = RequestMethod.GET)
    public ModelAndView ListLaveursExlus(){
            ModelAndView modelAndView = new ModelAndView();
            List<Profil> ListLaveursExclu = profilService.findAllLaveursValiderAndExclu();
            
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("ListLaveursExclu", ListLaveursExclu);
            modelAndView.addObject("nbLaveurs", ListLaveursExclu.size());
            modelAndView.setViewName("admin/ListeLaveursExclus");
            return modelAndView;
    }
    
    @RequestMapping(value="/Admin/ListeProfilsAValider", method = RequestMethod.GET)
    public ModelAndView ListProfilAValider(){
            ModelAndView modelAndView = new ModelAndView();
            List<Profil> ListProfilsAValider= profilService.findAllProfilsAValider();
            
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("ListProfilsAValider", ListProfilsAValider);
            modelAndView.addObject("nbLaveurs", ListProfilsAValider.size());
            modelAndView.setViewName("admin/ListeProfilsAValider");
            return modelAndView;
    }
    
    @RequestMapping(value="/Admin/ModifierLaveur/{id}", method = RequestMethod.GET)
    public ModelAndView ModifyLaveur(@PathVariable("id") int id){
            ModelAndView modelAndView = new ModelAndView();
            Profil profil = profilService.findProfilById(id);
            Login login = profil.getLogin();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("profil", profil);
            modelAndView.addObject("login", login);
            modelAndView.setViewName("admin/ModifierLaveur");
            return modelAndView;
    }
    
    @RequestMapping(value="/Admin/ModifierLaveur", method = RequestMethod.POST)
    public ModelAndView ModifyLaveurPOST(@Valid Profil profil, BindingResult bindingResult, Login login,@RequestParam("file") MultipartFile[] files, WebRequest request,@RequestParam("stringdatedenaissance") @DateTimeFormat(pattern="dd/MM/yyyy") Date datedenaissance) throws IOException, ParseException {
            ModelAndView modelAndView = new ModelAndView();
            Profil oldProfil = profilService.findProfilById(profil.getId());
            Login oldlogin = loginService.findLoginByEmail(oldProfil.getEmail());
            if (bindingResult.hasErrors()) {
                    modelAndView.addObject("errorMessage", bindingResult.getFieldError());
                    modelAndView.setViewName("admin/ModifierLaveur");
                    System.out.println("fr.pyka.lavage.controller.AdminController.AddLaveurPOST() : " + bindingResult.getFieldError());
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
                    if(files[1].isEmpty()){
                        profil.setPieceid(oldProfil.getPieceid());
                    }else{
                        String ext = FilenameUtils.getExtension(files[1].getOriginalFilename());
                        String oldname = FilenameUtils.removeExtension(files[1].getOriginalFilename());
                        String newFilename = DigestUtils.md5Hex( oldname );
                        System.out.println( ext + "-" + oldname + " " + files[1].getOriginalFilename() + " -> ");
                        File tmp = new File(path,newFilename + "." + ext);
                        files[1].transferTo(tmp);
                        profil.setPieceid(newFilename + "." + ext);
                    }
                    if(files[2].isEmpty()){
                        profil.setPermis(oldProfil.getPermis());
                    }else{
                        String ext = FilenameUtils.getExtension(files[2].getOriginalFilename());
                        String oldname = FilenameUtils.removeExtension(files[2].getOriginalFilename());
                        String newFilename = DigestUtils.md5Hex( oldname );
                        System.out.println( ext + "-" + oldname + " " + files[2].getOriginalFilename() + " -> ");
                        File tmp = new File(path,newFilename + "." + ext);
                        files[2].transferTo(tmp);
                        profil.setPermis(newFilename + "." + ext);
                    }
                    if(files[3].isEmpty()){
                        profil.setCasierJudiciaire(oldProfil.getCasierJudiciaire());
                    }else{
                        String ext = FilenameUtils.getExtension(files[3].getOriginalFilename());
                        String oldname = FilenameUtils.removeExtension(files[3].getOriginalFilename());
                        String newFilename = DigestUtils.md5Hex( oldname );
                        System.out.println( ext + "-" + oldname + " " + files[3].getOriginalFilename() + " -> ");
                        File tmp = new File(path,newFilename + "." + ext);
                        files[3].transferTo(tmp);
                        profil.setCasierJudiciaire(newFilename + "." + ext);
                    }
                    
                    if(files[4].isEmpty()){
                        profil.setKbis(oldProfil.getKbis());
                    }else{
                        String ext = FilenameUtils.getExtension(files[4].getOriginalFilename());
                        String oldname = FilenameUtils.removeExtension(files[4].getOriginalFilename());
                        String newFilename = DigestUtils.md5Hex( oldname );
                        System.out.println( ext + "-" + oldname + " " + files[4].getOriginalFilename() + " -> ");
                        File tmp = new File(path,newFilename + "." + ext);
                        files[4].transferTo(tmp);
                        profil.setKbis(newFilename + "." + ext);
                    }
                    
                    profil.setTelephone(profil.getTelephone().replaceAll("\\s+",""));
                    SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
                    Date date = formatter.parse(formatter.format(datedenaissance));
                    profil.setDatedenaissance(date);
                    boolean changepwd = true;
                    if(login.getPassword().equals(oldlogin.getPassword())){ 
                        changepwd = false;
                    }
                    profilService.UpdateProfil(oldProfil.getId(), profil, login, false, changepwd);
                    modelAndView.addObject("successMessage", "Compte modifié avec succèss");
                    modelAndView.addObject("profil", profil);
                    modelAndView.addObject("login", login);
                    return new ModelAndView(new RedirectView("ModifierLaveur/"+profil.getId()));

            }
            return modelAndView;
    }
    
    @RequestMapping(value="/Admin/ExclureLaveur/{id}", method = RequestMethod.GET)
    public ModelAndView ExclureLaveur(@PathVariable("id") int id){
            ModelAndView modelAndView = new ModelAndView();
            Profil Laveur = profilService.findProfilById(id);
            Login login = Laveur.getLogin();
            login.setExclu(true);
            Laveur.setDateExclusion(new Date());
            
            profilService.UpdateProfil(Laveur.getId(), Laveur, login, false, false);
            
            return new ModelAndView(new RedirectView("../ListeLaveursActifs"));
    }
    
    @RequestMapping(value="/Admin/ReIntegrerLaveur/{id}", method = RequestMethod.GET)
    public ModelAndView ReIntegrerLaveur(@PathVariable("id") int id){
            ModelAndView modelAndView = new ModelAndView();
            Profil Laveur = profilService.findProfilById(id);
            Login login = Laveur.getLogin();
            login.setExclu(false);
            Laveur.setDateExclusion(null);
            
            profilService.UpdateProfil(Laveur.getId(), Laveur, login, false, false);
            
            return new ModelAndView(new RedirectView("../ListeLaveursExclus"));
    }
    
     @RequestMapping(value="/Admin/ConfirmerProfil/{id}", method = RequestMethod.GET)
    public ModelAndView ValiderProfil(@PathVariable("id") int id){
            ModelAndView modelAndView = new ModelAndView();
            Profil profil = profilService.findProfilById(id);
            Login login = profil.getLogin();
            login.setConfirme(true);
            login.setValide(true);
            profil.setActive(true);
            
            profilService.UpdateProfil(profil.getId(), profil, login, false, false);
            
            return new ModelAndView(new RedirectView("../ListeProfilsAValider"));
    }
    
    
    /************************************************************ Messages **************************************************************************/
        
    @RequestMapping(value="/Admin/MessagesEnvoyee", method = RequestMethod.GET)
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
            modelAndView.setViewName("admin/Messages");
            return modelAndView;
    }

    @RequestMapping(value="/Admin/MessagesRecu", method = RequestMethod.GET)
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
            modelAndView.setViewName("admin/Messages");
            return modelAndView;
    }

    @RequestMapping(value="/Admin/EnvoyerMessage/{id}", method = RequestMethod.GET)
    public ModelAndView EnvoyerMessage(@PathVariable("id") int id){
            ModelAndView modelAndView = new ModelAndView();
            ChatLine chatLine = new ChatLine();
            Profil profil = profilService.findProfilById(id);
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("chatLine", chatLine);
            modelAndView.addObject("profil", profil);
            modelAndView.setViewName("admin/EnvoyerMessage");
            return modelAndView;
    }

    @RequestMapping(value="/Admin/EnvoyerMessage", method = RequestMethod.POST)
    public ModelAndView EnvoyerMessagePost(@Valid @ModelAttribute("chatLine") ChatLine chatLine, BindingResult bindingResult,@RequestParam("messageprofilId") int messageprofilId){

            Profil sender = profilService.findProfilByEmail(GetLoggedUser());
            Profil receiver = profilService.findProfilById(messageprofilId);
            Chat chat = new Chat();
            chatLine.setSenderId(sender);
            chatLine.setReceiverId(receiver);
            chatLine.setDate(new Date());
            chatLine.setChatId(chat);
            chatLine.setVu(false);
            chatLineService.AddChatLine(chat, chatLine);

            return new ModelAndView(new RedirectView("EnvoyerMessage/"+messageprofilId));
    }

    @RequestMapping(value="/Admin/NouveauMessage", method = RequestMethod.GET)
    public ModelAndView ComposeMail(){
            ModelAndView modelAndView = new ModelAndView();
            ChatLine chatLine = new ChatLine();
            List<Profil> profilList = profilService.findAllProfilsValider();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("chatLine", chatLine);
            modelAndView.addObject("profilList", profilList);
            modelAndView.setViewName("admin/NouveauMessage");
            return modelAndView;
    }

    @RequestMapping(value="/Admin/NouveauMessage", method = RequestMethod.POST)
    public ModelAndView ComposeMailPost(@Valid @ModelAttribute("chatLine") ChatLine chatLine, BindingResult bindingResult,@RequestParam("messageprofilId") int messageprofilId){
            ModelAndView modelAndView = new ModelAndView();
            Profil sender = profilService.findProfilByEmail(GetLoggedUser());
            Profil receiver = profilService.findProfilById(messageprofilId);
            Chat chat = new Chat();
            chatLine.setSenderId(sender);
            chatLine.setReceiverId(receiver);
            chatLine.setDate(new Date());
            chatLine.setChatId(chat);
            chatLine.setVu(false);
            chatLineService.AddChatLine(chat, chatLine);

            /*final SimpleMailMessage email = SendMSGByEmail(sender,receiver,chatLine.getMessage());
            mailSender.send(email);*/

            List<Profil> profilList = profilService.findAllProfilsValider();
            modelAndView.addObject("chatLine", chatLine);
            modelAndView.addObject("profilList", profilList);
            modelAndView.setViewName("admin/NouveauMessage");
            return modelAndView;
    }

    @RequestMapping(value="/Admin/ConsulterMessage/{id}", method = RequestMethod.GET)
    public ModelAndView ViewMailGET(@PathVariable("id") Long id){
            ModelAndView modelAndView = new ModelAndView();
            ChatLine chatLine = chatLineService.findChatLineById(id);
            List<ChatLine> chatLineList = chatLineService.findChatLineByChatId(chatLine.getChatId());
            chatLine.setVu(true);
            chatLineService.AddChatLine(chatLine.getChatId(), chatLine);
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("chatLineList", chatLineList);
            modelAndView.setViewName("admin/ConsulterMessage");
            return modelAndView;
    }

    @RequestMapping(value="/Admin/RepondreMessage", method = RequestMethod.POST)
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

            /*final SimpleMailMessage email = SendMSGByEmail(sender,receiver,chatLine2.getMessage());
            mailSender.send(email);*/

            return new ModelAndView("redirect:/Admin/MessagesEnvoyee");
    }
        
        
    @RequestMapping(value="/Admin/ModifierProfil", method = RequestMethod.GET)
    public ModelAndView ModifyProfil(){
            ModelAndView modelAndView = new ModelAndView();
            Profil profil = profilService.findProfilByEmail(GetLoggedUser());
            Login login = profil.getLogin();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("profil", profil);
            modelAndView.addObject("login", login);
            modelAndView.setViewName("admin/ModifierProfil");
            return modelAndView;
    }
    
    @RequestMapping(value="/Admin/ModifierProfil", method = RequestMethod.POST)
    public ModelAndView ModifyProfilPOST(@Valid Profil profil, BindingResult bindingResult, Login login,@RequestParam("file") MultipartFile[] files, WebRequest request,@RequestParam("stringdatedenaissance") @DateTimeFormat(pattern="dd/MM/yyyy") Date datedenaissance) throws IOException, ParseException {
            ModelAndView modelAndView = new ModelAndView();
            Profil oldProfil = profilService.findProfilById(profil.getId());
            Login oldlogin = loginService.findLoginByEmail(oldProfil.getEmail());
            if (bindingResult.hasErrors()) {
                    modelAndView.addObject("errorMessage", bindingResult.getFieldError());
                    modelAndView.setViewName("admin/ModifierProfil");
                    System.out.println("fr.pyka.lavage.controller.AdminController.ModifyProfilPOST() : " + bindingResult.getFieldError());
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
                    if(files[1].isEmpty()){
                        profil.setPieceid(oldProfil.getPieceid());
                    }else{
                        String ext = FilenameUtils.getExtension(files[1].getOriginalFilename());
                        String oldname = FilenameUtils.removeExtension(files[1].getOriginalFilename());
                        String newFilename = DigestUtils.md5Hex( oldname );
                        System.out.println( ext + "-" + oldname + " " + files[1].getOriginalFilename() + " -> ");
                        File tmp = new File(path,newFilename + "." + ext);
                        files[1].transferTo(tmp);
                        profil.setPieceid(newFilename + "." + ext);
                    }
                    
                    profil.setTelephone(profil.getTelephone().replaceAll("\\s+",""));
                    SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
                    Date date = formatter.parse(formatter.format(datedenaissance));
                    profil.setDatedenaissance(date);
                    boolean changepwd = true;
                    if(login.getPassword().equals(oldlogin.getPassword())){ 
                        changepwd = false;
                    }
                    profilService.UpdateProfil(oldProfil.getId(), profil, login, true, changepwd);
                    modelAndView.addObject("successMessage", "Compte modifié avec succèss");
                    modelAndView.addObject("profil", profil);
                    modelAndView.addObject("login", login);
                    return new ModelAndView(new RedirectView("ModifierProfil"));

            }
            return modelAndView;
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
    
    private SimpleMailMessage CommandeNotifyLaveur(String contextPath, Commande commande, Login login) {
      SimpleMailMessage email = new SimpleMailMessage();
      email.setSubject("PYKA - Confirmation d'une nouvelle commande");
      email.setText("Bonjour " + login.getProfilId().getCivilite() + " " + login.getProfilId().getNom() + " " + login.getProfilId().getPrenom() + ", \r\n \r\n"
            + "Une nouvelle commande vient de vous être assigné pour le "+ commande.getDate() +", Du "+ commande.getCreneau().getHeureDebut() +" à "+ commande.getCreneau().getHeureFin() +". \r\n \r\n"
            + "\r\n \r\nEquipe PYKA ");
      email.setFrom(env.getProperty("spring.mail.username"));
      email.setTo(login.getEmail());
      return email;
    }
    
    private SimpleMailMessage CommandeNotifyAncienLaveur(String contextPath, Commande commande, Login login) {
      SimpleMailMessage email = new SimpleMailMessage();
      email.setSubject("PYKA - Confirmation d'une nouvelle commande");
      email.setText("Bonjour " + login.getProfilId().getCivilite() + " " + login.getProfilId().getNom() + " " + login.getProfilId().getPrenom() + ", \r\n \r\n"
            + "Nous vous informons que la commande prévu pour le "+ commande.getDate() +", Du "+ commande.getCreneau().getHeureDebut() +" à "+ commande.getCreneau().getHeureFin() +". \r\n \r\n"
            + "à été assigner à un autre laveur \r\n"
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
    
    private SimpleMailMessage ModifiyCommandeNotifyLaveur(String contextPath, Commande commande, Login login) {
      SimpleMailMessage email = new SimpleMailMessage();
      email.setSubject("PYKA - Modification d'une commande");
      email.setText("Bonjour " + login.getProfilId().getCivilite() + " " + login.getProfilId().getNom() + " " + login.getProfilId().getPrenom() + ", \r\n \r\n"
            + "Votre commande pour le "+ commande.getDate() +", Du "+ commande.getCreneau().getHeureDebut() +" à "+ commande.getCreneau().getHeureFin() +"a bien été modifier. \r\n \r\n"
            + "Pour plus de détails veuillez consulter votre espace personnel. \r\n \r\n"
            + "\r\n \r\nEquipe PYKA ");
      email.setFrom(env.getProperty("spring.mail.username"));
      email.setTo(login.getEmail());
      return email;
    }
    
} 

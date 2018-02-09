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
import fr.pyka.lavage.model.Disponibilite;
import fr.pyka.lavage.model.Login;
import fr.pyka.lavage.model.Profil;
import fr.pyka.lavage.service.ChatLineService;
import fr.pyka.lavage.service.ChatService;
import fr.pyka.lavage.service.CommandeService;
import fr.pyka.lavage.service.CreneauxService;
import fr.pyka.lavage.service.DisponibiliteService;
import fr.pyka.lavage.service.LoginService;
import fr.pyka.lavage.service.ProfilService;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.Valid;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author Publicab
 */
@Controller
public class LaveurController {
    
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
    @Autowired
    private DisponibiliteService DisponibiliteService;
    
    public String GetLoggedUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName(); //get logged in username
    }
    
    @RequestMapping(value="/Laveur/home", method = RequestMethod.GET)
    public ModelAndView landing(){
            ModelAndView modelAndView = new ModelAndView();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.setViewName("laveur/dashboard");
            return modelAndView;
    }
    
    @RequestMapping(value="/Laveur/Planning", method = RequestMethod.GET)
    public ModelAndView Planning() throws ParseException{
            ModelAndView modelAndView = new ModelAndView();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            Date today = dateFormat.parse(dateFormat.format(date));
            Date[] date_range = cal(today);     
            List<Date> calendrier = getListOfDaysBetweenTwoDates(date_range[0], date_range[1]);
            LinkedHashMap<Creneaux, List<Disponibilite>> ListDisponibilite = DisponibiliteService.findDisponibiliteByLaveurAndWeek(loggedUser,calendrier);
            System.out.println("fr.pyka.lavage.controller.LaveurController.Planning() :" + ListDisponibilite.size());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("today", today);
            modelAndView.addObject("ListDisponibilite", ListDisponibilite);
            modelAndView.addObject("calendrier", calendrier);
            modelAndView.setViewName("laveur/Planning");
            return modelAndView;
    }
    
    @RequestMapping(value="/Laveur/Planning", method = RequestMethod.POST)
    public ModelAndView PlanningPost(@RequestParam("dateduplanning") @DateTimeFormat(pattern="dd/MM/yyyy") Date dateduplanning) throws ParseException{
            ModelAndView modelAndView = new ModelAndView();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
            Date today = formatter.parse(formatter.format(dateduplanning));
            Date[] date_range = cal(today);     
            List<Date> calendrier = getListOfDaysBetweenTwoDates(date_range[0], date_range[1]);
            LinkedHashMap<Creneaux, List<Disponibilite>> ListDisponibilite = DisponibiliteService.findDisponibiliteByLaveurAndWeek(loggedUser,calendrier);
            System.out.println("fr.pyka.lavage.controller.LaveurController.Planning() :" + ListDisponibilite.size());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("today", today);
            modelAndView.addObject("ListDisponibilite", ListDisponibilite);
            modelAndView.addObject("calendrier", calendrier);
            modelAndView.setViewName("laveur/Planning");
            return modelAndView;
    }
    
    @RequestMapping(value="/Laveur/EnregistrerPlanning/{datesaveplanning}", method = RequestMethod.POST)
    public ModelAndView SavePlanningPost(@RequestBody List<String> dispo,@PathVariable("datesaveplanning") @DateTimeFormat(pattern="yyyy-MM-dd") Date datesaveplanning) throws ParseException{
            ModelAndView modelAndView = new ModelAndView();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
            Date today = formatter.parse(formatter.format(datesaveplanning));
            Date[] date_range = cal(today);  
            List<Date> calendrier = getListOfDaysBetweenTwoDates(date_range[0], date_range[1]);
            List<Creneaux> ListCreneaux = CreneauxService.findAllCreneauxOrderedByHeureDebut();
            List<Disponibilite> ListDisponibilite = DisponibiliteService.findDisponibiliteByLaveurAndWeek2(loggedUser,calendrier);
            List<Disponibilite> planningdispo = new ArrayList<>();
            System.out.println("fr.pyka.lavage.controller.LaveurController.SavePlanningPost()" + dispo.size());
            for (int i = 0; i < dispo.size(); i++) {
                    System.out.println("fr.pyka.lavage.controller.LaveurController.SavePlanningPost() : " + dispo.get(i));
                    if(dispo.get(i).startsWith("dispo")){
                        List<String> allMatches = new ArrayList<>();
                        Pattern p = Pattern.compile("\\[(.*?)\\]");
                        Matcher m = p.matcher(dispo.get(i));
                        while (m.find()) {
                            allMatches.add(m.group(1));
                        }
                        Disponibilite disponibilite = new Disponibilite();
                        disponibilite.setAttribue(true);
                        disponibilite.setLaveurId(loggedUser);
                        disponibilite.setDate(calendrier.get(Integer.parseInt(allMatches.get(1))));
                        disponibilite.setCreneau(ListCreneaux.get(Integer.parseInt(allMatches.get(0))));
                        //DisponibiliteService.AddDisponibilite(disponibilite);
                        System.out.println("fr.pyka.lavage.controller.LaveurController.SavePlanningPost() : " + disponibilite.getDate());
                        System.out.println("fr.pyka.lavage.controller.LaveurController.SavePlanningPost() : " + disponibilite.getCreneau());
                    }
                    else{ 
                        Disponibilite disponibiliteexistante = DisponibiliteService.findDisponibiliteById(Integer.parseInt(dispo.get(i)));
                        planningdispo.add(disponibiliteexistante);
                        }
            }
            System.out.println("fr.pyka.lavage.controller.LaveurController.SavePlanningPost(Remove) : " + ListDisponibilite);
            for (int i = 0; i < ListDisponibilite.size(); i++) {
                if(ListDisponibilite.get(i).getId() != null && !planningdispo.contains(ListDisponibilite.get(i))){
                    //DisponibiliteService.RemoveDisponibilite(ListDisponibilite.get(i));
                    System.out.println("fr.pyka.lavage.controller.LaveurController.SavePlanningPost(Remove) : " + ListDisponibilite.get(i));
                }
            }
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("today", today);
            //modelAndView.addObject("ListDisponibilite", ListDisponibilite);
            modelAndView.addObject("calendrier", calendrier);
            modelAndView.setViewName("laveur/Planning");
            return modelAndView;
    }
    
    @RequestMapping(value="/Laveur/ModifierProfil", method = RequestMethod.GET)
    public ModelAndView ModifyProfil(){
            ModelAndView modelAndView = new ModelAndView();
            Profil profil = profilService.findProfilByEmail(GetLoggedUser());
            Login login = profil.getLogin();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("profil", profil);
            modelAndView.addObject("login", login);
            modelAndView.setViewName("laveur/ModifierProfil");
            return modelAndView;
    }
    
    @RequestMapping(value="/Laveur/ModifierProfil", method = RequestMethod.POST)
    public ModelAndView ModifyProfilPOST(@Valid Profil profil, BindingResult bindingResult, Login login,@RequestParam("file") MultipartFile[] files, WebRequest request,@RequestParam("stringdatedenaissance") @DateTimeFormat(pattern="dd/MM/yyyy") Date datedenaissance) throws IOException, ParseException {
            ModelAndView modelAndView = new ModelAndView();
            Profil oldProfil = profilService.findProfilById(profil.getId());
            Login oldlogin = loginService.findLoginByEmail(oldProfil.getEmail());
            if (bindingResult.hasErrors()) {
                    modelAndView.addObject("errorMessage", bindingResult.getFieldError());
                    modelAndView.setViewName("laveur/ModifierProfil");
                    System.out.println("fr.pyka.lavage.controller.LaveurController.ModifyProfilPOST() : " + bindingResult.getFieldError());
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
                    return new ModelAndView(new RedirectView("ModifierProfil"));

            }
            return modelAndView;
    }
    
    
    
    @RequestMapping(value="/Laveur/ListeCommandesAssignee", method = RequestMethod.GET)
    public ModelAndView ListCommandeAssigned(){
            ModelAndView modelAndView = new ModelAndView();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            List<Commande> ListCommandesAssigner = CommandeService.findAllCommandesAssignerByLaveur(loggedUser);
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("ListCommandesAssigner", ListCommandesAssigner);
            modelAndView.addObject("nbCommandes", ListCommandesAssigner.size());
            modelAndView.setViewName("laveur/ListeCommandesAssignee");
            return modelAndView;
    }
    
    @RequestMapping(value="/Laveur/ListeCommandesEnCours", method = RequestMethod.GET)
    public ModelAndView ListCommandesEnCours() throws ParseException{
            ModelAndView modelAndView = new ModelAndView();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            List<Commande> ListCommandesAssigner = CommandeService.findAllCommandesAssignerByLaveur(loggedUser);
            List<Commande> ListCommandesEnCours = new ArrayList<>();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            Date today = dateFormat.parse(dateFormat.format(date));
            for (int i = 0; i < ListCommandesAssigner.size(); i++) {
                if(ListCommandesAssigner.get(i).getDate().compareTo(today) == 0){
                    ListCommandesEnCours.add(ListCommandesAssigner.get(i));
                }
            }
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("ListCommandesEnCours", ListCommandesEnCours);
            modelAndView.addObject("nbCommandes", ListCommandesEnCours.size());
            modelAndView.setViewName("laveur/ListeCommandesEnCours");
            return modelAndView;
    }
    
    @RequestMapping(value="/Laveur/ListeCommandesEffectuees", method = RequestMethod.GET)
    public ModelAndView ListCommandesEffectuees(){
            ModelAndView modelAndView = new ModelAndView();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            List<Commande> ListCommandesEffectuer = CommandeService.findAllCommandesEffectuerByLaveur(loggedUser);
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("ListCommandesEffectuer", ListCommandesEffectuer);
            modelAndView.addObject("nbCommandes", ListCommandesEffectuer.size());
            modelAndView.setViewName("laveur/ListeCommandesEffectuees");
            return modelAndView;
    }
    
    
    @RequestMapping(value="/Laveur/MessagesEnvoyee", method = RequestMethod.GET)
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
            modelAndView.setViewName("laveur/Messages");
            return modelAndView;
    }
        
    @RequestMapping(value="/Laveur/MessagesRecu", method = RequestMethod.GET)
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
            modelAndView.setViewName("laveur/Messages");
            return modelAndView;
    }
        
    @RequestMapping(value="/Laveur/NouveauMessage", method = RequestMethod.GET)
    public ModelAndView ComposeMail(){
            ModelAndView modelAndView = new ModelAndView();
            ChatLine chatLine = new ChatLine();
            List<Profil> profilList = profilService.findAllProfilsValider();
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("chatLine", chatLine);
            modelAndView.addObject("profilList", profilList);
            modelAndView.setViewName("laveur/NouveauMessage");
            return modelAndView;
    }
        
    @RequestMapping(value="/Laveur/NouveauMessage", method = RequestMethod.POST)
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
            modelAndView.setViewName("laveur/NouveauMessage");
            return modelAndView;
    }
        
    @RequestMapping(value="/Laveur/ConsulterMessage/{id}", method = RequestMethod.GET)
    public ModelAndView ViewMailGET(@PathVariable("id") Long id){
            ModelAndView modelAndView = new ModelAndView();
            ChatLine chatLine = chatLineService.findChatLineById(id);
            List<ChatLine> chatLineList = chatLineService.findChatLineByChatId(chatLine.getChatId());
            chatLine.setVu(true);
            chatLineService.AddChatLine(chatLine.getChatId(), chatLine);
            Profil loggedUser = profilService.findProfilByEmail(GetLoggedUser());
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("chatLineList", chatLineList);
            modelAndView.setViewName("laveur/ConsulterMessage");
            return modelAndView;
    }
        
    @RequestMapping(value="/Laveur/RepondreMessage", method = RequestMethod.POST)
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
            return new ModelAndView("redirect:/Laveur/MessagesEnvoyee");
    }
    
    
    
    private Date[] cal(Date date) {
      
            if(date == null) return null;
            // Go backward to get Monday
            LocalDate monday = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            while (monday.getDayOfWeek() != DayOfWeek.MONDAY)
            {
              monday = monday.minusDays(1);
            }

            // Go forward to get Sunday
            LocalDate sunday = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY)
            {
              sunday = sunday.plusDays(1);
            }

            Date[] dates = new Date[2];
            dates[0] = Date.from(monday.atStartOfDay(ZoneId.systemDefault()).toInstant());
            dates[1] = Date.from(sunday.atStartOfDay(ZoneId.systemDefault()).toInstant());
            return dates;
        }
    
    
    private List<Date> getListOfDaysBetweenTwoDates(Date startDate, Date endDate) {
            List<Date> result = new ArrayList<>();
            Calendar start = Calendar.getInstance();
            start.setTime(startDate);
            Calendar end = Calendar.getInstance();
            end.setTime(endDate);
            end.add(Calendar.DAY_OF_YEAR, 1); //Add 1 day to endDate to make sure endDate is included into the final list
            while (start.before(end)) {
                result.add(start.getTime());
                start.add(Calendar.DAY_OF_YEAR, 1);
            }
            return result;
        }
    
}

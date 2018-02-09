/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Publicab
 */
@Entity
@Table(name = "profil")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Profil.findAll", query = "SELECT p FROM Profil p")})
public class Profil implements Serializable {

    @OneToMany(mappedBy = "laveurId")
    private Collection<Disponibilite> disponibiliteCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "senderId")
    private Collection<ChatLine> chatLineCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receiverId")
    private Collection<ChatLine> chatLineCollection1;

    @Column(name = "date_exclusion")
    @Temporal(TemporalType.DATE)
    private Date dateExclusion;

    @Column(name = "last_commandes")
    private Integer lastCommandes;

    @Column(name = "nb_commandes")
    private Integer nbCommandes;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profilId")
    private Collection<Login> loginCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "civilite")
    private String civilite;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nom")
    private String nom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "prenom")
    private String prenom;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "datedenaissance")
    @Temporal(TemporalType.DATE)
    private Date datedenaissance;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "adresse")
    private String adresse;
    @Basic(optional = false)
    @NotNull
    @Column(name = "codepostale")
    private int codepostale;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "ville")
    private String ville;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "telephone")
    private String telephone;
    @Size(max = 20)
    @Column(name = "siren")
    private String siren;
    @Size(max = 50)
    @Column(name = "rib")
    private String rib;
    @Size(max = 50)
    @Column(name = "nom_entreprise")
    private String nomEntreprise;
    @Size(max = 300)
    @Column(name = "parcours")
    private String parcours;
    @Column(name = "dateentree")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dateentree;
    @Column(name = "datesortie")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date datesortie;
    @Size(max = 300)
    @Column(name = "pieceid")
    private String pieceid;
    @Size(max = 300)
    @Column(name = "pieceidverso")
    private String pieceidverso;
    @Size(max = 300)
    @Column(name = "kbis")
    private String kbis;
    @Size(max = 300)
    @Column(name = "permis")
    private String permis;
    @Size(max = 300)
    @Column(name = "photo")
    private String photo;
    @Size(max = 300)
    @Column(name = "casier_judiciaire")
    private String casierJudiciaire;
    @Size(max = 300)
    @Column(name = "vtc")
    private String vtc;
    @Size(max = 300)
    @Column(name = "photoRib")
    private String photoRib;
    @Basic(optional = false)
    @NotNull
    @Column(name = "active")
    private boolean active;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clientId")
    private Collection<Commande> commandeCollection;
    @OneToMany(mappedBy = "laveurId")
    private Collection<Commande> commandeCollection1;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "profilId")
    private Login login;

    public Profil() {
    }

    public Profil(Integer id) {
        this.id = id;
    }

    public Profil(Integer id, String civilite, String nom, String prenom, Date datedenaissance, String adresse, int codepostale, String ville, String email, String telephone, boolean active) {
        this.id = id;
        this.civilite = civilite;
        this.nom = nom;
        this.prenom = prenom;
        this.datedenaissance = datedenaissance;
        this.adresse = adresse;
        this.codepostale = codepostale;
        this.ville = ville;
        this.email = email;
        this.telephone = telephone;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Date getDatedenaissance() {
        return datedenaissance;
    }

    public void setDatedenaissance(Date datedenaissance) {
        this.datedenaissance = datedenaissance;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getCodepostale() {
        return codepostale;
    }

    public void setCodepostale(int codepostale) {
        this.codepostale = codepostale;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSiren() {
        return siren;
    }

    public void setSiren(String siren) {
        this.siren = siren;
    }

    public String getRib() {
        return rib;
    }

    public void setRib(String rib) {
        this.rib = rib;
    }

    public String getNomEntreprise() {
        return nomEntreprise;
    }

    public void setNomEntreprise(String nomEntreprise) {
        this.nomEntreprise = nomEntreprise;
    }

    public String getParcours() {
        return parcours;
    }

    public void setParcours(String parcours) {
        this.parcours = parcours;
    }

    public Date getDateentree() {
        return dateentree;
    }

    public void setDateentree(Date dateentree) {
        this.dateentree = dateentree;
    }

    public Date getDatesortie() {
        return datesortie;
    }

    public void setDatesortie(Date datesortie) {
        this.datesortie = datesortie;
    }

    public String getPieceid() {
        return pieceid;
    }

    public void setPieceid(String pieceid) {
        this.pieceid = pieceid;
    }

    public String getPieceidverso() {
        return pieceidverso;
    }

    public void setPieceidverso(String pieceidverso) {
        this.pieceidverso = pieceidverso;
    }

    public String getKbis() {
        return kbis;
    }

    public void setKbis(String kbis) {
        this.kbis = kbis;
    }

    public String getPermis() {
        return permis;
    }

    public void setPermis(String permis) {
        this.permis = permis;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCasierJudiciaire() {
        return casierJudiciaire;
    }

    public void setCasierJudiciaire(String casierJudiciaire) {
        this.casierJudiciaire = casierJudiciaire;
    }

    public String getVtc() {
        return vtc;
    }

    public void setVtc(String vtc) {
        this.vtc = vtc;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @XmlTransient
    public Collection<Commande> getCommandeCollection() {
        return commandeCollection;
    }

    public void setCommandeCollection(Collection<Commande> commandeCollection) {
        this.commandeCollection = commandeCollection;
    }

    @XmlTransient
    public Collection<Commande> getCommandeCollection1() {
        return commandeCollection1;
    }

    public void setCommandeCollection1(Collection<Commande> commandeCollection1) {
        this.commandeCollection1 = commandeCollection1;
    }

    @XmlTransient
    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Profil)) {
            return false;
        }
        Profil other = (Profil) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fr.pyka.model.Profil[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<Login> getLoginCollection() {
        return loginCollection;
    }

    public void setLoginCollection(Collection<Login> loginCollection) {
        this.loginCollection = loginCollection;
    }

    public Integer getNbCommandes() {
        return nbCommandes;
    }

    public void setNbCommandes(Integer nbCommandes) {
        this.nbCommandes = nbCommandes;
    }

    public Integer getLastCommandes() {
        return lastCommandes;
    }

    public void setLastCommandes(Integer lastCommandes) {
        this.lastCommandes = lastCommandes;
    }

    public Date getDateExclusion() {
        return dateExclusion;
    }

    public void setDateExclusion(Date dateExclusion) {
        this.dateExclusion = dateExclusion;
    }

    @XmlTransient
    public Collection<ChatLine> getChatLineCollection() {
        return chatLineCollection;
    }

    public void setChatLineCollection(Collection<ChatLine> chatLineCollection) {
        this.chatLineCollection = chatLineCollection;
    }

    @XmlTransient
    public Collection<ChatLine> getChatLineCollection1() {
        return chatLineCollection1;
    }

    public void setChatLineCollection1(Collection<ChatLine> chatLineCollection1) {
        this.chatLineCollection1 = chatLineCollection1;
    }

    @XmlTransient
    public Collection<Disponibilite> getDisponibiliteCollection() {
        return disponibiliteCollection;
    }

    public void setDisponibiliteCollection(Collection<Disponibilite> disponibiliteCollection) {
        this.disponibiliteCollection = disponibiliteCollection;
    }

    public String getPhotoRib() {
        return photoRib;
    }

    public void setPhotoRib(String photoRib) {
        this.photoRib = photoRib;
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Publicab
 */
@Entity
@Table(name = "commande")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Commande.findAll", query = "SELECT c FROM Commande c")})
public class Commande implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "vehicule")
    private String vehicule;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "type_lavage")
    private String typeLavage;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "adresse")
    private String adresse;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "mode_paiement")
    private String modePaiement;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Float montant;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nouvelle")
    private boolean nouvelle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "assigner")
    private boolean assigner;
    @Basic(optional = false)
    @NotNull
    @Column(name = "effectuer")
    private boolean effectuer;
    @Basic(optional = false)
    @NotNull
    @Column(name = "annuler")
    private boolean annuler;
    @Size(max = 100)
    @Column(name = "motif_annuler")
    private String motifAnnuler;
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Profil clientId;
    @JoinColumn(name = "laveur_id", referencedColumnName = "id")
    @ManyToOne
    private Profil laveurId;
    @JoinColumn(name = "creneau", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Creneaux creneau;

    public Commande() {
    }

    public Commande(Integer id) {
        this.id = id;
    }

    public Commande(Integer id, String vehicule, String typeLavage, String adresse, String modePaiement, boolean nouvelle, boolean assigner, boolean effectuer, boolean annuler) {
        this.id = id;
        this.vehicule = vehicule;
        this.typeLavage = typeLavage;
        this.adresse = adresse;
        this.modePaiement = modePaiement;
        this.nouvelle = nouvelle;
        this.assigner = assigner;
        this.effectuer = effectuer;
        this.annuler = annuler;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getVehicule() {
        return vehicule;
    }

    public void setVehicule(String vehicule) {
        this.vehicule = vehicule;
    }

    public String getTypeLavage() {
        return typeLavage;
    }

    public void setTypeLavage(String typeLavage) {
        this.typeLavage = typeLavage;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    public Float getMontant() {
        return montant;
    }

    public void setMontant(Float montant) {
        this.montant = montant;
    }

    public boolean getNouvelle() {
        return nouvelle;
    }

    public void setNouvelle(boolean nouvelle) {
        this.nouvelle = nouvelle;
    }

    public boolean getAssigner() {
        return assigner;
    }

    public void setAssigner(boolean assigner) {
        this.assigner = assigner;
    }

    public boolean getEffectuer() {
        return effectuer;
    }

    public void setEffectuer(boolean effectuer) {
        this.effectuer = effectuer;
    }

    public boolean getAnnuler() {
        return annuler;
    }

    public void setAnnuler(boolean annuler) {
        this.annuler = annuler;
    }

    public String getMotifAnnuler() {
        return motifAnnuler;
    }

    public void setMotifAnnuler(String motifAnnuler) {
        this.motifAnnuler = motifAnnuler;
    }

    public Profil getClientId() {
        return clientId;
    }

    public void setClientId(Profil clientId) {
        this.clientId = clientId;
    }

    public Profil getLaveurId() {
        return laveurId;
    }

    public void setLaveurId(Profil laveurId) {
        this.laveurId = laveurId;
    }

    public Creneaux getCreneau() {
        return creneau;
    }

    public void setCreneau(Creneaux creneau) {
        this.creneau = creneau;
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
        if (!(object instanceof Commande)) {
            return false;
        }
        Commande other = (Commande) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fr.pyka.model.Commande[ id=" + id + " ]";
    }
    
}

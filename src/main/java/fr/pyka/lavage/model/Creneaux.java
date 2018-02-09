/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.model;

import java.io.Serializable;
import java.sql.Time;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Publicab
 */
@Entity
@Table(name = "creneaux")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Creneaux.findAll", query = "SELECT c FROM Creneaux c")})
public class Creneaux implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "heure_debut")
    @Temporal(TemporalType.TIME)
    private Date heureDebut;
    @Basic(optional = false)
    @NotNull
    @Column(name = "heure_fin")
    @Temporal(TemporalType.TIME)
    private Date heureFin;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creneau")
    private Collection<Disponibilite> disponibiliteCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creneau")
    private Collection<Commande> commandeCollection;

    public Creneaux() {
    }

    public Creneaux(Integer id) {
        this.id = id;
    }

    public Creneaux(Integer id, Time heureDebut, Time heureFin) {
        this.id = id;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(Time heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Date getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(Time heureFin) {
        this.heureFin = heureFin;
    }

    @XmlTransient
    public Collection<Disponibilite> getDisponibiliteCollection() {
        return disponibiliteCollection;
    }

    public void setDisponibiliteCollection(Collection<Disponibilite> disponibiliteCollection) {
        this.disponibiliteCollection = disponibiliteCollection;
    }

    @XmlTransient
    public Collection<Commande> getCommandeCollection() {
        return commandeCollection;
    }

    public void setCommandeCollection(Collection<Commande> commandeCollection) {
        this.commandeCollection = commandeCollection;
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
        if (!(object instanceof Creneaux)) {
            return false;
        }
        Creneaux other = (Creneaux) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fr.pyka.model.Creneaux[ id=" + id + " ]";
    }


    public void setHeureDebut(Date heureDebut) {
        this.heureDebut = heureDebut;
    }

    public void setHeureFin(Date heureFin) {
        this.heureFin = heureFin;
    }
    
}

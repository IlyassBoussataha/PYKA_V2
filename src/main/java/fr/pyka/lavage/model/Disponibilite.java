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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Publicab
 */
@Entity
@Table(name = "disponibilite")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Disponibilite.findAll", query = "SELECT d FROM Disponibilite d")})
public class Disponibilite implements Serializable {

    @JoinColumn(name = "laveur_id", referencedColumnName = "id")
    @ManyToOne
    private Profil laveurId;

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
    @Column(name = "attribue")
    private boolean attribue;
    @JoinColumn(name = "creneau", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Creneaux creneau;

    public Disponibilite() {
    }

    public Disponibilite(Integer id) {
        this.id = id;
    }

    public Disponibilite(Integer id, boolean attribue) {
        this.id = id;
        this.attribue = attribue;
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

    public boolean getAttribue() {
        return attribue;
    }

    public void setAttribue(boolean attribue) {
        this.attribue = attribue;
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
        if (!(object instanceof Disponibilite)) {
            return false;
        }
        Disponibilite other = (Disponibilite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fr.pyka.model.Disponibilite[ id=" + id + " ]";
    }

    public Profil getLaveurId() {
        return laveurId;
    }

    public void setLaveurId(Profil laveurId) {
        this.laveurId = laveurId;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Publicab
 */
@Entity
@Table(name = "login")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Login.findAll", query = "SELECT l FROM Login l")})
public class Login implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "loginId")
    private Collection<Verificationtoken> verificationtokenCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Column(name = "admin")
    private boolean admin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "limited")
    private boolean limited;
    @Basic(optional = false)
    @NotNull
    @Column(name = "confirme")
    private boolean confirme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valide")
    private boolean valide;
    @Basic(optional = false)
    @NotNull
    @Column(name = "exclu")
    private boolean exclu;
    @JoinColumn(name = "profil_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    private Profil profilId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "login")
    private Collection<LoginRoles> loginRolesCollection;

    public Login() {
    }

    public Login(Integer id) {
        this.id = id;
    }

    public Login(Integer id, String email, String password, boolean admin, boolean limited, boolean confirme, boolean valide, boolean exclu) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.admin = admin;
        this.limited = limited;
        this.confirme = confirme;
        this.valide = valide;
        this.exclu = exclu;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean getLimited() {
        return limited;
    }

    public void setLimited(boolean limited) {
        this.limited = limited;
    }

    public boolean getConfirme() {
        return confirme;
    }

    public void setConfirme(boolean confirme) {
        this.confirme = confirme;
    }

    public boolean getValide() {
        return valide;
    }

    public void setValide(boolean valide) {
        this.valide = valide;
    }

    public boolean getExclu() {
        return exclu;
    }

    public void setExclu(boolean exclu) {
        this.exclu = exclu;
    }

    public Profil getProfilId() {
        return profilId;
    }

    public void setProfilId(Profil profilId) {
        this.profilId = profilId;
    }

    @XmlTransient
    public Collection<LoginRoles> getLoginRolesCollection() {
        return loginRolesCollection;
    }

    public void setLoginRolesCollection(Collection<LoginRoles> loginRolesCollection) {
        this.loginRolesCollection = loginRolesCollection;
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
        if (!(object instanceof Login)) {
            return false;
        }
        Login other = (Login) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fr.pyka.model.Login[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<Verificationtoken> getVerificationtokenCollection() {
        return verificationtokenCollection;
    }

    public void setVerificationtokenCollection(Collection<Verificationtoken> verificationtokenCollection) {
        this.verificationtokenCollection = verificationtokenCollection;
    }
    
}

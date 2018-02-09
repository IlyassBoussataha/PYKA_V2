/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.model;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Publicab
 */
@Entity
@Table(name = "login_roles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LoginRoles.findAll", query = "SELECT l FROM LoginRoles l")})
public class LoginRoles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "roles")
    private String roles;
    @JoinColumn(name = "login_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Login login;

    public LoginRoles() {
    }

    public LoginRoles(Integer id) {
        this.id = id;
    }

    public LoginRoles(Integer id, String roles) {
        this.id = id;
        this.roles = roles;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Login getLoginId() {
        return login;
    }

    public void setLoginId(Login loginId) {
        this.login = loginId;
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
        if (!(object instanceof LoginRoles)) {
            return false;
        }
        LoginRoles other = (LoginRoles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fr.pyka.model.LoginRoles[ id=" + id + " ]";
    }
    
}

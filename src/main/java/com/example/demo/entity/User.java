package com.example.demo.entity;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@ToString(exclude = "invoice")
@EqualsAndHashCode(exclude = "invoice")
@Entity
@Table(name="users")
public class User implements UserDetails, Serializable {

    @Id
    private String userName;
    @Column(name="password")
    private String password;
    @Column(name="email")
    private String email;
    @ElementCollection
    private List<String> images;
    @Column(name = "enabled")
    private boolean enabled;

    public User() {
        super();
        this.enabled=false;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getImages() {
        return images;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

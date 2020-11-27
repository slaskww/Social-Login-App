package com.example.oauth2sociallogin.domain;


import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Collection;

@Entity
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue
     private Long id;

     private String username;

     @Size(min = 8, message = "Hasło powinno zawierać minimum 8 znaków w tym snaki specjalne")
     private String password;
     private String oAuth2ProviderId;

     @NotBlank(message = "Pole 'Imię i nazwisko' nie może być puste")
     @Size(min = 5, message = "Pole 'Imię i nazwisko' ma mniej niz 5 znakow")
     private String fullName;
     private String email;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
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

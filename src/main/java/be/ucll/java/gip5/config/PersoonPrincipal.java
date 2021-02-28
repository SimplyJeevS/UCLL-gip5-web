package be.ucll.java.gip5.config;

import be.ucll.java.gip5.model.Persoon;
import be.ucll.java.gip5.model.Rol;
import be.ucll.java.gip5.model.Toewijzing;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PersoonPrincipal implements UserDetails {

    private final Persoon persoon;

    public PersoonPrincipal(Persoon persoon){
        this.persoon = persoon;
    }

    public Persoon getPersoon(){
        return persoon;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authList = new ArrayList<SimpleGrantedAuthority>();
        authList.add(new SimpleGrantedAuthority("ROLE_"+Rol.GUEST));
        if(persoon.getDefault_rol() == Rol.JOURNALIST){
            authList.add(new SimpleGrantedAuthority("ROLE_"+Rol.JOURNALIST));
        }
        if(persoon.getDefault_rol() == Rol.SPELER){
            authList.add(new SimpleGrantedAuthority("ROLE_"+Rol.SPELER));
        }
        if(persoon.getDefault_rol() == Rol.COACH){
            authList.add(new SimpleGrantedAuthority("ROLE_"+Rol.COACH));
        }
        if(persoon.getDefault_rol() == Rol.SECRETARIS){
            authList.add(new SimpleGrantedAuthority("ROLE_"+Rol.SECRETARIS));
        }
        return authList;
    }

    @Override
    public String getPassword() {
        return persoon.getWachtwoord();
    }

    @Override
    public String getUsername() {
        return persoon.getEmail();
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

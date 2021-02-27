package be.ucll.java.gip5.config;

<<<<<<< HEAD
=======
import be.ucll.java.gip5.model.AdminRol;
>>>>>>> 54b8ad2ddadb12a7382e566d3745e82e54e3b7bb
import be.ucll.java.gip5.model.Persoon;
import be.ucll.java.gip5.model.Toewijzing;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ToewijzingPrincipal implements UserDetails {

    private final Toewijzing toewijzing;
    private final AdminRol adminRol;
    private final Persoon persoon;

    public ToewijzingPrincipal(Toewijzing toewijzing,AdminRol adminRol,Persoon persoon){
        this.toewijzing = toewijzing;
        this.adminRol = adminRol;
        this.persoon = persoon;
    }

    public Toewijzing getToewijzing(){
        return toewijzing;
    }

    public AdminRol getAdminRol(){
        return adminRol;
    }

    public Persoon getPersoon(){
        return persoon;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authList = new ArrayList<SimpleGrantedAuthority>();
        authList.add(new SimpleGrantedAuthority("ROLE_USER"));
        if(toewijzing.getRolId() == adminRol.getAdminRolId()){
            authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
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

package com.flockit.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.flockit.enums.User_Role;

public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public CustomUserDetails(final UserModel user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = getAuthorities(user.getRole());
    }
    
    private List<GrantedAuthority> getAuthorities(final User_Role role) {
    	
    	List<GrantedAuthority> authorities = new ArrayList<>();
    	
        authorities.add(new SimpleGrantedAuthority(role.getValue()));
        
        //role.getPrivileges().stream().map(p -> new SimpleGrantedAuthority(p.getName())).forEach(authorities::add);
    
    	return authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

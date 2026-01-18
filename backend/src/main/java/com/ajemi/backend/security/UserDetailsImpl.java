package com.ajemi.backend.security;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {

    private final com.ajemi.backend.entity.User user;

    public UserDetailsImpl(com.ajemi.backend.entity.User user) {
        this.user = user;
    }

    // ⭐ المهم
    public Long getId() {
        return user.getId();
    }

    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
            new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())
        );
    }

    @Override public String getPassword() { return user.getPassword(); }
    @Override public boolean isAccountNonExpired() { return true; }
    // @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    // @Override public boolean isEnabled() { return true; }
    @Override 
public boolean isAccountNonLocked() { 
    // Ila kān banned == true, khass accountNonLocked t-koun false
    return !user.isBanned(); 
}

@Override 
public boolean isEnabled() { 
    // Ila kān banned, n-qdro n-7sbouh b-elli l-compte m-sedd (Disabled)
    return !user.isBanned(); 
}
}


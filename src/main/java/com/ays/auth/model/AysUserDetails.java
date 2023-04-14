package com.ays.auth.model;

import com.ays.auth.model.enums.AysUserType;
import com.ays.user.model.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class AysUserDetails implements UserDetails {

    private final AysUser aysUser;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        switch (aysUser.getType()) {
            case ADMIN -> {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(aysUser.getType().name()));
                return authorities;
            }
            case USER -> {
                List<GrantedAuthority> authorities = new ArrayList<>();

                Set<UserRole> roles = aysUser.getRoles();
                for (UserRole role : roles) {
                    authorities.add(new SimpleGrantedAuthority(role.name()));
                }
                authorities.add(new SimpleGrantedAuthority(aysUser.getType().name()));

                return authorities;
            }
            default -> throw new IllegalArgumentException("Unknown user type: " + aysUser.getType());
        }
    }

    @Override
    public String getPassword() {
        return aysUser.getPassword();
    }

    @Override
    public String getUsername() {
        return aysUser.getUsername();
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

    public Long getId() {
        return aysUser.getId();
    }

    public String getEmail() {
        return aysUser.getEmail();
    }

    public AysUserType getType() {
        return aysUser.getType();
    }
}

package com.server.demeter.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


import com.server.demeter.domain.Role;
import com.server.demeter.domain.User;
import com.server.demeter.repository.UserRepository;
import com.server.demeter.services.exception.ObjectNotEnabledException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userepository;

    @Override
    public UserDetails loadUserByUsername(final String email)  {

        final Optional<User> user = userepository.findByEmail(email);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException(String.format("UserNotExist"));
        } else if (!user.get().isEnabled()) {
            throw new ObjectNotEnabledException(String.format("UserNotEnabled"));
        }

        return new UserRepositoryUserDetails(user.get());
    }

    private final List<GrantedAuthority> getGrantedAuthorities(final Collection<Role> roles) {
        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        
        for (final Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return authorities;
    }

    public final Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
        return getGrantedAuthorities(roles);
    }

    // internal class
    private final static class UserRepositoryUserDetails extends User implements UserDetails {
        private static final long serialVersionUID = 1L;

        public UserRepositoryUserDetails(final User user) {
            super(user);
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return getRoles();
        }

        @Override
        public String getUsername() {
            return getEmail();
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
            return isEnabled();
        }
    }

}
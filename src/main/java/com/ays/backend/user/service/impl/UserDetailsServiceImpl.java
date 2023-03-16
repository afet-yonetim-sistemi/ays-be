package com.ays.backend.user.service.impl;

import com.ays.backend.user.exception.UserNotFoundException;
import com.ays.backend.user.model.entities.User;
import com.ays.backend.user.repository.UserRepository;
import com.ays.backend.user.security.JwtUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User Name " + username + " not found"));
        return new JwtUserDetails(user);
    }
}

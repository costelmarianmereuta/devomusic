package com.devoteam.devomusic.security;

import com.devoteam.devomusic.exception.ResourceNotFoundException;
import com.devoteam.devomusic.model.GoogleUser;
import com.devoteam.devomusic.model.PasswordUser;
import com.devoteam.devomusic.model.User;
import com.devoteam.devomusic.repository.GoogleUserRepository;
import com.devoteam.devomusic.repository.PasswordUserRepository;
import com.devoteam.devomusic.repository.UserBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordUserRepository passwordUserRepository;

    @Autowired
    private GoogleUserRepository googleUserRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (passwordUserRepository.findByUsernameOrEmail(email,email).isPresent()) {
            PasswordUser user = passwordUserRepository.findByUsernameOrEmail(email, email).get();
            return UserPrincipal.create(user);
        } else if (googleUserRepository.findByEmail(email).isPresent()) {
            GoogleUser user = googleUserRepository.findByEmail(email).get();
            return UserPrincipal.create(user);
        } else {
            throw new UsernameNotFoundException("User with username " + email + " not found.");
        }
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        if (passwordUserRepository.findById(id).isPresent()) {
            PasswordUser user = passwordUserRepository.findById(id).get();
            return UserPrincipal.create(user);
        } else if (googleUserRepository.findById(id).isPresent()) {
            GoogleUser user = googleUserRepository.findById(id).get();
            return UserPrincipal.create(user);
        } else {
            throw new UsernameNotFoundException("User with id " + id.toString() + " not found.");
        }
    }
}

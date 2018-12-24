package com.devoteam.devomusic.controller;

import com.devoteam.devomusic.exception.AppException;
import com.devoteam.devomusic.model.GoogleUser;
import com.devoteam.devomusic.model.PasswordUser;
import com.devoteam.devomusic.model.Role;
import com.devoteam.devomusic.model.RoleName;
import com.devoteam.devomusic.payload.ApiResponse;
import com.devoteam.devomusic.payload.GoogleSignupRequest;
import com.devoteam.devomusic.payload.SignUpRequest;
import com.devoteam.devomusic.repository.GoogleUserRepository;
import com.devoteam.devomusic.repository.PasswordUserRepository;
import com.devoteam.devomusic.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

/**
 * A REST Controller for mapping the /register endpoint.
 */
@RestController
@RequestMapping("/api/auth")
public class RegisterController {

    @Autowired
    private PasswordUserRepository passwordUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private GoogleUserRepository googleUserRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(passwordUserRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(passwordUserRepository.existsByEmail(signUpRequest.getEmail()) || googleUserRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating passwordUser's account
        PasswordUser passwordUser = new PasswordUser(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        passwordUser.setPassword(passwordEncoder.encode(passwordUser.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("PasswordUser Role not set."));

        passwordUser.setRoles(Collections.singleton(userRole));

        PasswordUser result = passwordUserRepository.save(passwordUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "PasswordUser registered successfully"));
    }

    /**
     * Endpoint for registering Google users, which aren't already registered.
     * @param googleSignupRequest: Class for encapsulating the request.
     * @return: Returns a ResponseEntity, indicating whether the response was successfull or not.
     */
    @PostMapping("/oauth_google")
    public ResponseEntity<?> registerGoogleUser(@RequestBody GoogleSignupRequest googleSignupRequest) {
        // Check whether the users exists or not
        if (passwordUserRepository.existsByEmail(googleSignupRequest.getEmail()) || googleUserRepository.existsByEmail(googleSignupRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email is already registered."),
                    HttpStatus.BAD_REQUEST);
        }

        // Make a user and set their role
        GoogleUser googleUser = new GoogleUser(googleSignupRequest.getEmail(), googleSignupRequest.getName(),
                googleSignupRequest.getProviderPic());
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User role not set."));
        googleUser.setRoles(Collections.singleton(userRole));

        GoogleUser result = googleUserRepository.save(googleUser);

        return ResponseEntity.ok().body(new ApiResponse(true, "User with email " + googleSignupRequest.getEmail() + " registered successfully"));
    }
}

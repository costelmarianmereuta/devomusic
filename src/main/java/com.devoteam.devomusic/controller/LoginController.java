package com.devoteam.devomusic.controller;

import com.devoteam.devomusic.payload.GoogleJsonRequest;
import com.devoteam.devomusic.payload.ApiResponse;
import com.devoteam.devomusic.payload.JwtAuthenticationResponse;
import com.devoteam.devomusic.payload.LoginRequest;
import com.devoteam.devomusic.repository.GoogleUserRepository;
import com.devoteam.devomusic.repository.PasswordUserRepository;
import com.devoteam.devomusic.repository.RoleRepository;
import com.devoteam.devomusic.security.CustomUserDetailsService;
import com.devoteam.devomusic.security.JwtTokenProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

/**
 * A REST Controller for varios login endpoints.
 */
@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Value("${google.client.client-id}")
    private String googleClientId;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordUserRepository passwordUserRepository;

    @Autowired
    private GoogleUserRepository googleUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    /**
     * Method for performing an oauth login after a google token has been received.
     * @param oauthToken: The oauthToken from Google
     * @return Returns a ResponseEntity with a JWT if the user has registered successfully, an http error otherwise.
     */
    @PostMapping("/oauth2")
    public ResponseEntity<?> oAuth2Login(@RequestBody GoogleJsonRequest oauthToken) {
        try {
            // Verify the Google login token
            JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();
            GoogleIdToken idToken = verifier.verify(oauthToken.getTokenId());

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                String userId = payload.getSubject();
                String email = payload.getEmail();
                String providerPic = oauthToken.getImageUrl();

                // Check whether the user already exists in the database.
                if (!(passwordUserRepository.existsByEmail(email) || googleUserRepository.existsByEmail(email))) {
                    return new ResponseEntity<>(new ApiResponse(false, "There is no user registered" +
                            "with that email"), HttpStatus.BAD_REQUEST);
                }

                // Authenticate the user
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null
                        )
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Generate and return a JWT
                String jwt = tokenProvider.generateToken(authentication);
                return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));

            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ApiResponse(false, "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);

    }

}

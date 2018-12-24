package com.devoteam.devomusic.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * A class for return information about the logged in user.
 */
@RestController
public class HomeController {

    @RequestMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }

}

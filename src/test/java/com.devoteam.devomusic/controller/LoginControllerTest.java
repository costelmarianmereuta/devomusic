package com.devoteam.devomusic.controller;

import com.devoteam.devomusic.payload.LoginRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Test
    public void testLoginFail() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("blabloe");
        loginRequest.setPassword("biliboeli");

        this.mockMvc.perform(post(("/api/auth/login"), loginRequest)).andExpect(status().is4xxClientError());
    }

    @Test
    public void testLoginSuccess() throws Exception {
        // TODO: implement given

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("testtest");
        loginRequest.setPassword("test");

        this.mockMvc.perform(post(("/api/auth/register"), loginRequest)).andExpect(status().isOk());
    }

}

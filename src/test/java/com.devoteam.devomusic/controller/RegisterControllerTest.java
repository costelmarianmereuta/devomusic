package com.devoteam.devomusic.controller;

import com.devoteam.devomusic.payload.SignUpRequest;
import com.netflix.discovery.converters.Auto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
public class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRegisterUserSuccess() throws Exception {
        SignUpRequest registerRequest = new SignUpRequest();
        registerRequest.setName("Devomusic Devomusic");
        registerRequest.setUsername("devomusic");
        registerRequest.setEmail("devomusic@devomusic.com");
        registerRequest.setPassword("bloebla");

        this.mockMvc.perform(post(("/api/auth/register"), registerRequest)).andExpect(status().isOk());
    }

    @Test
    public void testRegisterUserFail() throws Exception {
        // TODO: given(...)

        SignUpRequest registerRequest = new SignUpRequest();
        registerRequest.setName("Devomusic Devomusic");
        registerRequest.setUsername("devomusic");
        registerRequest.setEmail("devomusic@devomusic.com");
        registerRequest.setPassword("bloebla");

        this.mockMvc.perform(post(("/api/auth/register"), registerRequest)).andExpect(status().isOk());
    }

}

package com.devoteam.devomusic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
public class AuthenticationServerApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void contextLoads() {

    }

    @Test
    public void configureSuccess() throws Exception {
        this.mockMvc.perform(get(("/"))).andExpect(status().isOk());
    }

    @Test
    public void configureFail() throws Exception {
        this.mockMvc.perform(get(("/test"))).andExpect(status().is3xxRedirection());
    }
}

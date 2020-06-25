package com.test.purchasing.controller;

import com.test.purchasing.model.dto.CreateOrderDTO;
import com.test.purchasing.model.exception.GoodNotFoundException;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.support.SessionStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CreateOrderDTO order;

    @MockBean
    Model model;

    @MockBean
    SessionStatus sessionStatus;

    @Autowired
    OrderController orderController;

    @Rule
    public ExpectedException expectedException;

    @Test
    void getListPageUnauthorized() throws Exception {
        mockMvc.perform(get("/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails("test@gmail.com")
    void getListPageOK() throws Exception {
        mockMvc.perform(get("/list"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("test@gmail.com")
    public void internationalizationTest() throws Exception {
        this.mockMvc.perform(get("/list?lang=ua_UA"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Вийти")));
    }

    @Test
    @WithUserDetails("test@gmail.com")
    void addGood() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/list")
                .with(csrf().asHeader())
                .param("goodId", "0")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection()).andReturn();
        assertThat(result.getResolvedException(), instanceOf(GoodNotFoundException.class));
    }

    @Test
    @WithUserDetails("test@gmail.com")
    void changeAmount() throws Exception {
        this.mockMvc.perform(post("/pay/5")
                .with(csrf().asHeader())
                .param("amount", "3")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("test@gmail.com")
    void deleteGood() throws Exception {
        this.mockMvc.perform(post("/delete/5")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());
    }
}
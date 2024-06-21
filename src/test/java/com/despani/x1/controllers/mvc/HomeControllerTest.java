//package com.despani.core.controllers.mvc;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import static org.hamcrest.Matchers.containsString;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import static org.junit.Assert.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//@RunWith(MockitoJUnitRunner.class)
//public class HomeControllerTest {
//
//    @Before
//    public void setUp() throws Exception {
//    }
//
//    @Before
//    public void setup() {
//
//        SimpMessagingTemplate simpMessagingTemplate = Mockito.mock(SimpMessagingTemplate.class);
//        HomeController hc = new HomeController(simpMessagingTemplate);
//        mockMvc = MockMvcBuilders.standaloneSetup(hc).build();
//    }
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void shouldReturnDefaultMessage() throws Exception {
//        this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().is3xxRedirection())
//                .andExpect(content().string(containsString("")));
//        System.out.printf("oh dulsinia!");
//    }
//
//    @Test
//    public void home2() {
//    }
//
//    @Test
//    public void xout() {
//    }
//
//    @Test
//    public void greet() {
//    }
//}
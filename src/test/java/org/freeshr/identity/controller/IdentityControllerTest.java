package org.freeshr.identity.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.freeshr.identity.launch.ApplicationConfiguration;
import org.freeshr.identity.model.UserCredentials;
import org.freeshr.identity.service.UserCredentialsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationConfiguration.class})
public class IdentityControllerTest {

    private MockMvc mockMvc;
    private String user = "foo";
    private String password = "bar";

    @Mock
    UserCredentialsService service;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mockMvc= MockMvcBuilders.standaloneSetup(new IdentityController(service)).build();
    }

    @Test
    public void shouldCreateTokenOnSuccessfulLogin() throws Exception {
        UserCredentials userCredentials = new UserCredentials(user, password);
        UUID uuid = UUID.randomUUID();
        Mockito.when(service.verify(userCredentials)).thenReturn(uuid);

        String content = objectMapper.writeValueAsString(userCredentials);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(service).verify(userCredentials);
    }

    @Test
    public void shouldRespond403OnBadCredentials() throws Exception {
        UserCredentials userCredentials = new UserCredentials(user, password);
        Mockito.when(service.verify(userCredentials)).thenReturn(null);

        String content = objectMapper.writeValueAsString(userCredentials);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(403));

        Mockito.verify(service).verify(userCredentials);
    }
//
//    @Test
//    public void shouldGiveErrorIfUserNotVerify() throws Exception {
//
//        UserCredentials userCredentials = new UserCredentials();
//        userCredentials.setName(user);
//        userCredentials.setPassword(password);
//        Mockito.when(service.verify(userCredentials)).thenReturn(null);
//        String json = objectMapper.writeValueAsString(userCredentials);
//        mockMvc.perform(MockMvcRequestBuilders.post("/login").content(json).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().is(403));
//        Mockito.verify(service).verify(userCredentials);
//    }
//
//    @Test
//    public void shouldGetTokenDetail() throws Exception {
//
//        UserCredentials userCredentials = new UserCredentials();
//        String token = "token";
//        userCredentials.setName(user);
//        userCredentials.setPassword(password);
//        ResponseObject responseObject = new ResponseObject();
//        responseObject.setToken(token);
//        responseObject.setLogin(user);
//        Mockito.when(service.getUserCredential(token)).thenReturn(user);
//        mockMvc.perform(MockMvcRequestBuilders.get("/userInfo/" + token).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.login", Is.is(user)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.token", Is.is(token)));
//        Mockito.verify(service).getUserCredential(token);
//    }


}

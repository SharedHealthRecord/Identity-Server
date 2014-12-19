package org.freeshr.identity.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.freeshr.identity.launch.ApplicationConfiguration;
import org.freeshr.identity.model.UserCredentials;
import org.freeshr.identity.model.UserInfo;
import org.freeshr.identity.service.IdentityService;
import org.hamcrest.core.Is;
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

import java.util.HashSet;
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
    IdentityService service;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new IdentityController(service)).build();
    }

    @Test
    public void shouldCreateTokenOnSuccessfulLogin() throws Exception {
        UserCredentials userCredentials = new UserCredentials(user, password);
        UUID uuid = UUID.randomUUID();
        Mockito.when(service.login(userCredentials)).thenReturn(uuid);

        String content = objectMapper.writeValueAsString(userCredentials);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(service).login(userCredentials);
    }

    @Test
    public void shouldRespond403OnBadCredentials() throws Exception {
        UserCredentials userCredentials = new UserCredentials(user, password);
        Mockito.when(service.login(userCredentials)).thenReturn(null);

        String content = objectMapper.writeValueAsString(userCredentials);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(403));

        Mockito.verify(service).login(userCredentials);
    }

    @Test
    public void shouldGetUserInfoGivenToken() throws Exception {
        UserInfo userInfo = new UserInfo(user, getRoles(), "123");
        UUID token = UUID.randomUUID();
        Mockito.when(service.userInfo(token)).thenReturn(userInfo);
        mockMvc.perform(MockMvcRequestBuilders.get("/userInfo/" + token).
                contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user", Is.is(user)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.locationCode", Is.is("123")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles[0]", Is.is("shr.user")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles[1]", Is.is("mci.admin")));
        Mockito.verify(service).userInfo(token);
    }

    private HashSet<String> getRoles() {
        HashSet<String> strings = new HashSet<>();
        strings.add("mci.admin");
        strings.add("shr.user");
        return strings;
    }

}

package org.freeshr.identity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.freeshr.identity.model.UserCredentials;
import org.freeshr.identity.service.IdentityService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class IdentityControllerTest {
    private MockMvc mockMvc;

    @Mock
    private IdentityService service;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new IdentityController(service)).build();
    }

    @Test
    public void shouldCreateTokenOnSuccessfulLogin() throws Exception {
        String email = "foo";
        String password = "bar";

        String clientId = "12345";
        String authToken = "xyz";

        UUID uuid = UUID.randomUUID();
        when(service.signin(any(UserCredentials.class))).thenReturn(uuid);
        UserCredentials userCredentials = new UserCredentials(email, password);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/signin");
        requestBuilder.param("email", email);
        requestBuilder.param("password", password);
        requestBuilder.contentType(MediaType.MULTIPART_FORM_DATA);
        requestBuilder.header("client_id", clientId);
        requestBuilder.header("X-Auth-Token", authToken);
        HashMap<String, String> responseMap = new HashMap<>();
        responseMap.put(IdentityController.ACCESS_TOKEN, uuid.toString());
        String result = new ObjectMapper().writeValueAsString(responseMap);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(result));

        Mockito.verify(service).signin(userCredentials);
    }

}
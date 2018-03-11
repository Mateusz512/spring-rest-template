package mateuszhinc.springRestTemplate.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import mateuszhinc.springRestTemplate.Application;
import mateuszhinc.springRestTemplate.dto.AuthorityDTO;
import mateuszhinc.springRestTemplate.dto.UserDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    //TODO: move method elsewhere
    public static String convertObjectToJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return mapper.writeValueAsString(object);
    }

    //TODO: move method elsewhere
    public static String convertObjectToJsonIgnoringAnnotations(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return mapper.writeValueAsString(object);
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(springSecurityFilterChain).build();
    }

    @Test
    public void givenNoToken_whenGetUsers_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenInvalidRole_whenGetUsers_thenForbidden() throws Exception {
        final String accessToken = obtainAccessToken("user1", "password");

        mockMvc.perform(get("/user")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void givenToken_whenGetUsers_thenOk() throws Exception {
        final String accessToken = obtainAccessToken("admin", "password");

        mockMvc.perform(get("/user")
                .header("Authorization", "Bearer " + accessToken)
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].username", is("admin")))
                .andExpect(jsonPath("$[0].authorities", hasSize(2)))
                .andExpect(jsonPath("$[0].authorities[*].name", containsInAnyOrder("ADMIN", "USER")));
    }

    @Test
    public void givenTokenAndValidUser_whenPutUser_thenCreated() throws Exception {
        final String accessToken = obtainAccessToken("admin", "password");
        final UserDTO userDTO = new UserDTO("user3", "password3",
                Arrays.asList(new AuthorityDTO("ADMIN"), new AuthorityDTO("USER")));

        mockMvc.perform(put("/user")
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json;charset=UTF-8")
                .content(convertObjectToJsonIgnoringAnnotations(userDTO)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/user")
                .header("Authorization", "Bearer " + accessToken)
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[*].username", hasItem("user3")));
    }

    @Test
    public void givenTokenAndInvalidUser_whenPutUser_thenCreated() throws Exception {
        final String accessToken = obtainAccessToken("admin", "password");
        final UserDTO userDTO = new UserDTO("", "",
                Arrays.asList(new AuthorityDTO("AsaIN"), new AuthorityDTO("fsaa")));

        mockMvc.perform(put("/user")
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json;charset=UTF-8")
                .content(convertObjectToJsonIgnoringAnnotations(userDTO)))
                .andExpect(status().isBadRequest());
    }

    //TODO: test Update
    //TODO: test Delete

    //TODO: move method elsewhere
    private String obtainAccessToken(String username, String password) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "project-api");
        params.add("secret", "top-sikret");
        params.add("username", username);
        params.add("password", password);

        ResultActions result
                = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic("project-api", "top-sikret"))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }
}
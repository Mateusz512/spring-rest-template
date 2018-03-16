package mateuszhinc.springRestTemplate.controller;

import mateuszhinc.springRestTemplate.dto.AuthorityDTO;
import mateuszhinc.springRestTemplate.dto.UserDTO;
import mateuszhinc.springRestTemplate.helpers.AppConst;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends BaseControllerTest {

    @Test
    public void givenNoToken_whenGetUsers_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenInvalidRole_whenGetUsers_thenForbidden() throws Exception {
        final String accessToken = obtainAccessToken("user2", "password");

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
                .andExpect(jsonPath("$[0].username", is("admin")))
                .andExpect(jsonPath("$[0].authorities[*].name", containsInAnyOrder(AppConst.ROLE_ADMIN, AppConst.ROLE_USER)));
    }

    @Test
    public void givenTokenAndValidUser_whenPutUser_thenCreated() throws Exception {
        final String accessToken = obtainAccessToken("admin", "password");
        final UserDTO userDTO = new UserDTO(null, "brandNewUser", "passjfds",
                Arrays.asList(new AuthorityDTO(AppConst.ROLE_ADMIN), new AuthorityDTO(AppConst.ROLE_USER)));

        mockMvc.perform(put("/user")
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json;charset=UTF-8")
                .content(convertObjectToJsonIgnoringAnnotations(userDTO)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/user")
                .header("Authorization", "Bearer " + accessToken)
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.username=='brandNewUser')]").exists())
                .andExpect(jsonPath("$[?(@.username=='brandNewUser')].authorities[*].name",
                        containsInAnyOrder(AppConst.ROLE_ADMIN, AppConst.ROLE_USER)));
    }

    @Test
    public void givenTokenAndInvalidUser_whenPutUser_thenBadRequest() throws Exception {
        final String accessToken = obtainAccessToken("admin", "password");
        final UserDTO userDTO = new UserDTO(null, "", "",
                Arrays.asList(new AuthorityDTO("AsaIN"), new AuthorityDTO("fsaa")));

        mockMvc.perform(put("/user")
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json;charset=UTF-8")
                .content(convertObjectToJsonIgnoringAnnotations(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenTokenAndValidId_whenDeleteUser_thenOk() throws Exception {
        final String accessToken = obtainAccessToken("admin", "password");

        mockMvc.perform(delete("/user/{id}",2)
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/user")
                .header("Authorization", "Bearer " + accessToken)
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id==2)]").doesNotExist());
    }

    @Test
    public void givenTokenAndExistingUser_whenPutUser_thenUpdated() throws Exception {
        final String accessToken = obtainAccessToken("admin", "password");
        final UserDTO userDTO = new UserDTO(null, "notadmin", "password2",
                Collections.singletonList(new AuthorityDTO(AppConst.ROLE_USER)));

        mockMvc.perform(put("/user/{id}",3)
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json;charset=UTF-8")
                .content(convertObjectToJsonIgnoringAnnotations(userDTO)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/user")
                .header("Authorization", "Bearer " + accessToken)
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id==3)].username", contains("notadmin")))
                .andExpect(jsonPath("$[?(@.id==3)].authorities.[?(@.name=='ADMIN')]").doesNotExist());
    }


}
package com.example.experiment7;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class RbacSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void publicEndpointShouldBeAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/public/hello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("This is a public endpoint"));
    }

    @Test
    void userEndpointShouldReturnUnauthorizedWhenNoCredentials() throws Exception {
        mockMvc.perform(get("/api/user/profile"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void userShouldAccessUserEndpoint() throws Exception {
        mockMvc.perform(get("/api/user/profile").with(httpBasic("user1", "user123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
    }

    @Test
    void userShouldBeForbiddenFromAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/admin/dashboard").with(httpBasic("user1", "user123")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Forbidden"));
    }

    @Test
    void adminShouldAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/admin/dashboard").with(httpBasic("admin1", "admin123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin1"));
    }

    @Test
    void loginShouldSucceedWithValidCredentials() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user1\",\"password\":\"user123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.username").value("user1"));
    }

    @Test
    void loginShouldFailWithInvalidCredentials() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user1\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Unauthorized"));
    }
}

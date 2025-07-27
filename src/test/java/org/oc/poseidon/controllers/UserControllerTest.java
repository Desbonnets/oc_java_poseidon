package org.oc.poseidon.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.oc.poseidon.domain.User;
import org.oc.poseidon.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import java.util.Optional;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "user", roles = {"ADMIN"})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setFullname("Test User");
        user.setRole("USER");
    }

    @Test
    void home_shouldReturnUserListView() throws Exception {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void addUser_shouldReturnAddView() throws Exception {
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));
    }

    @Test
    void validate_shouldRedirectToListWhenValid() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .param("username", "testuser")
                        .param("password", "Password123!")
                        .param("fullname", "Test User")
                        .param("role", "ROLE_USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        then(userRepository).should().save(any(User.class));
    }

    @Test
    void validate_shouldReturnAddViewWhenInvalid() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .param("username", "")  // invalid, empty
                        .param("password", "password123")
                        .param("fullname", "")
                        .param("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));
    }

    @Test
    void showUpdateForm_shouldReturnUpdateView() throws Exception {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/user/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void updateUser_shouldRedirectWhenValid() throws Exception {
        mockMvc.perform(post("/user/update/1")
                        .with(csrf())
                        .param("username", "updateduser")
                        .param("password", "Newpassword1!")
                        .param("fullname", "Updated User")
                        .param("role", "ROLE_ADMIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        then(userRepository).should().save(any(User.class));
    }

    @Test
    void updateUser_shouldReturnUpdateViewWhenInvalid() throws Exception {
        mockMvc.perform(post("/user/update/1")
                        .with(csrf())
                        .param("username", "")
                        .param("password", "pass")
                        .param("fullname", "")
                        .param("role", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"));
    }

    @Test
    void deleteUser_shouldRedirectToList() throws Exception {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/user/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        then(userRepository).should().delete(user);
    }
}

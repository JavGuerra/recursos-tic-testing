package com.grupo1.recursos_tic.integration.controller;

import com.grupo1.recursos_tic.config.WithMockUserSecurityContextFactory;
import com.grupo1.recursos_tic.model.Resource;
import com.grupo1.recursos_tic.model.User;
import com.grupo1.recursos_tic.model.User;
import com.grupo1.recursos_tic.model.UserRole;
import com.grupo1.recursos_tic.repository.UserRepo;
import com.grupo1.recursos_tic.service.RatingService;
import com.grupo1.recursos_tic.service.UserService;
import com.grupo1.recursos_tic.service.UserService;
import com.grupo1.recursos_tic.util.ErrMsg;
import org.hamcrest.Matchers;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerIntegrationTest {

    @Autowired
    private UserService userService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        userService.deleteAllUsers();

        User admin = User.builder()
                .email("admin@admin.es")
                .role(UserRole.ADMIN)
                .username("admin")
                .password(passwordEncoder.encode("Admin1234"))
                .build();
        userService.save(admin);

        var auth = new UsernamePasswordAuthenticationToken(
                admin,
                admin.getPassword(),
                admin.getAuthorities()
        );

        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
    }


    @Test
    //@WithMockUser(username = "admin", roles = "ADMIN")
    void test() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("users", hasSize(1)))
        ;
    }

    @Test
    @DisplayName("Buscar todos los usuarios")
    void findAll() throws Exception {
        userService.saveAll(List.of(
                User.builder().username("usuario1").build(),
                User.builder().username("usuario2").build()
        ));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", hasSize(3)))
                .andExpect(model().attribute("users",
                        allOf(
                                hasItem(hasProperty("username", Matchers.is("usuario1"))),
                                hasItem(hasProperty("username", Matchers.is("usuario2")))
                        )
                ));
    }

    @Test
    @DisplayName("Buscar todos los usuarios y no obtener usuarios")
    void findAll_NoUsers() throws Exception {
        userService.saveAll(List.of());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"))
                // Admin user is always initialized by default. Without this it's impossible being able to run user entity tests
                .andExpect(model().attribute("users", hasSize(1)));
    }

    @Test
    @DisplayName("Buscar recurso con ID válido y usuario autenticado")
    void findById_WithAuthenticated() throws Exception {

        User user = User.builder()
                .username("usuario1")
                .name("Usuario1")
                .build();
        userService.save(user);

        mockMvc.perform(get("/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/detail"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user",
                        hasProperty("name", Matchers.is("Usuario1"))
                ));
    }

    @Test
    void getFormToCreateNewUser() {
    }

    @Test
    void getFormToEditUser() {
    }

    @Test
    void save() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void deleteAllUsers() {
    }
}
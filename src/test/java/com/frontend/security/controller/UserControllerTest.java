package com.frontend.security.controller;

import com.frontend.security.user.User;
import com.frontend.security.user.UserController;
import com.frontend.security.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frontend.security.user.Role;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(UserController.class)// place the @AutoConfigureMockMvc annotation on this class under test
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SecurityConfig.class)
@WebAppConfiguration
class UserControllerTest {

//    @Autowired
//    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired

    private MockMvc mockMvc;


    @MockBean   // annotates that UserService is what we want to set up as a mock service
    private UserService userService;

    private User user1, user2;
    private User updatedUser1;

    @Autowired
    private ObjectMapper objectMapper;  // used for sending data in string format when we run our tests

    @BeforeEach
        // set up some initialisations before we use run the test
    void init(){

        user1 = User.builder()
                .firstname("Jurgen")
                .lastname("Klopp")
                .contact("94387592")
                .dob(new Date(693878400000L))
                .email("jurgen@gmail.com")
                .password("password")
                .role(Role.USER)
                .plan(null)
                .build();
        user2 = User.builder()
                .firstname("Mikel")
                .lastname("Arteta")
                .contact("94387591")
                .dob(new Date(693878400000L))
                .email("arteta@gmail.com")
                .password("password")
                .role(Role.USER)
                .plan(null)
                .build();
        updatedUser1 = User.builder()
                .firstname("Jurgen")
                .lastname("Klopp")
                .contact("94387593")
                .dob(new Date(693878400000L))
                .email("jurgenklopp@gmail.com")
                .password("password")
                .role(Role.USER)
                .plan(null)
                .build();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser
        // testing all products to be returned for GET request for all products
    void getProducts() throws Exception{
        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);

        when(userService.getUsers()).thenReturn(list);

        this.mockMvc.perform(get("/api/v1/users/user/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(list.size())));
    }

    @Test
    @WithMockUser
    void getSingleProduct() throws Exception {
        //CHALLENGE TO WRITE THE UNIT TEST TO GET A SINGLE PRODUCT
        when(userService.getUser(any(Integer.class)))
                .thenReturn(Optional.ofNullable(user1));

        this.mockMvc.perform(get("/api/v1/users/user/{id}","1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user1.getId())))
                .andExpect(jsonPath("$.firstname", is(user1.getFirstname())))
                .andExpect(jsonPath("$.lastname", is(user1.getLastname())))
                .andExpect(jsonPath("$.contact", is(user1.getContact())))
                .andExpect(jsonPath("$.dob", is(user1.getDob().toString())))
                .andExpect(jsonPath("$.email", is(user1.getEmail())))
                .andExpect(jsonPath("$.password", is(user1.getPassword())));

    }

    @Test
    @WithMockUser
    void updateProduct() throws Exception{
        // this is unit test, therefore the expected output stated here
        when(userService.updateUser(any(Integer.class), any(User.class)))
                .thenReturn(Optional.ofNullable(updatedUser1));

        // this is what the unit test will perform and the expected output should
        // be the same as the above unit test case
        this.mockMvc.perform(put("/api/v1/users/user/{id}", "1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser1)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user1.getId())))
                .andExpect(jsonPath("$.firstname", is(user1.getFirstname())))
                .andExpect(jsonPath("$.lastname", is(user1.getLastname())))
                .andExpect(jsonPath("$.contact", is(user1.getContact())))
                .andExpect(jsonPath("$.dob", is(user1.getDob().toString())))
                .andExpect(jsonPath("$.email", is(user1.getEmail())))
                .andExpect(jsonPath("$.password", is(user1.getPassword())));
    }

    @Test
    @WithMockUser
    void deleteProduct() throws Exception{
        // doNothing().when(productService).deleteProduct(anyString());
        // this.mockMvc.perform(delete("/products/{id}", "1")).andExpect(status().isNoContent());

        when(userService.deleteUser(any(Integer.class)))
                .thenReturn(Optional.ofNullable((user1)));

        this.mockMvc.perform(delete("/api/v1/users/user/{id}", "1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(user1.getId())))
                .andExpect(jsonPath("$.firstname", is(user1.getFirstname())))
                .andExpect(jsonPath("$.lastname", is(user1.getLastname())))
                .andExpect(jsonPath("$.contact", is(user1.getContact())))
                .andExpect(jsonPath("$.dob", is(user1.getDob().toString())))
                .andExpect(jsonPath("$.email", is(user1.getEmail())))
                .andExpect(jsonPath("$.password", is(user1.getPassword())));
    }

//    @Test
//    @WithMockUser
//    void createProduct() throws Exception {
//        when(userService.createUser(any(Plan.class))).thenReturn(user1);
//
//        this.mockMvc.perform(post("/api/v1/plans/plans").with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(user1)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", is(user1.getId())))
//                .andExpect(jsonPath("$.firstname", is(user1.getFirstname())))
//                .andExpect(jsonPath("$.lastname", is(user1.getLastname())))
//                .andExpect(jsonPath("$.contact", is(user1.getContact())))
//                .andExpect(jsonPath("$.dob", is(user1.getDob())))
//                .andExpect(jsonPath("$.email", is(user1.getEmail())))
//                .andExpect(jsonPath("$.password", is(user1.getPassword())));
//    }
}

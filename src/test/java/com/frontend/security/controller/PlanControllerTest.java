package com.frontend.security.controller;

import com.frontend.security.plan.Plan;
import com.frontend.security.plan.PlanController;
import com.frontend.security.plan.PlanService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(PlanController.class)
@Import(PlanController.class)// place the @AutoConfigureMockMvc annotation on this class under test
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SecurityConfig.class)
@WebAppConfiguration
class PlanControllerTest {

//    @Autowired
//    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired

    private MockMvc mockMvc;


    @MockBean   // annotates that UserService is what we want to set up as a mock service
    private PlanService planService;

    private Plan plan1, plan2;
    private Plan updatedPlan1;

    @Autowired
    private ObjectMapper objectMapper;  // used for sending data in string format when we run our tests

    @BeforeEach
        // set up some initialisations before we use run the test
    void init(){
        plan1 = new Plan("Gold", "This is the Gold Plan", "test", 100, false);
        plan2 = new Plan("Silver", "This is the Silver Plan", "test", 50, false);
        updatedPlan1 = new Plan("Gold", "This is the Gold Plan", "test", 100, true);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser
        // testing all products to be returned for GET request for all products
    void getProducts() throws Exception{
        List<Plan> list = new ArrayList<>();
        list.add(plan1);
        list.add(plan2);

        when(planService.getPlans()).thenReturn(list);

        this.mockMvc.perform(get("/api/v1/plans")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(list.size())));
    }

    @Test
    @WithMockUser
    void getSingleProduct() throws Exception {
        //CHALLENGE TO WRITE THE UNIT TEST TO GET A SINGLE PRODUCT
        when(planService.getPlan(any(Integer.class)))
                .thenReturn(Optional.ofNullable(plan1));

        this.mockMvc.perform(get("/api/v1/plans/{id}","1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(plan1.getId())))
                .andExpect(jsonPath("$.name", is(plan1.getName())))
                .andExpect(jsonPath("$.description", is(plan1.getDescription())))
                .andExpect(jsonPath("$.imageURL", is(plan1.getImageURL())))
                .andExpect(jsonPath("$.price", is(plan1.getPrice())))
                .andExpect(jsonPath("$.promo", is(plan1.getPromo())));
    }

    @Test
    @WithMockUser
    void updateProduct() throws Exception{
        // this is unit test, therefore the expected output stated here
        when(planService.updatePlan(any(Integer.class), any(Plan.class)))
                .thenReturn(Optional.ofNullable(updatedPlan1));

        // this is what the unit test will perform and the expected output should
        // be the same as the above unit test case
        this.mockMvc.perform(put("/api/v1/plans/{id}", "1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPlan1)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedPlan1.getId())))
                .andExpect(jsonPath("$.name", is(updatedPlan1.getName())))
                .andExpect(jsonPath("$.description", is(updatedPlan1.getDescription())))
                .andExpect(jsonPath("$.imageURL", is(updatedPlan1.getImageURL())))
                .andExpect(jsonPath("$.price", is(updatedPlan1.getPrice())))
                .andExpect(jsonPath("$.promo", is(updatedPlan1.getPromo())));
    }

    @Test
    @WithMockUser
    void deleteProduct() throws Exception{
        // doNothing().when(productService).deleteProduct(anyString());
        // this.mockMvc.perform(delete("/products/{id}", "1")).andExpect(status().isNoContent());

        when(planService.deletePlan(any(Integer.class)))
                .thenReturn(Optional.ofNullable((plan1)));

        this.mockMvc.perform(delete("/api/v1/plans/{id}", "1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(plan1.getId())))
                .andExpect(jsonPath("$.name", is(plan1.getName())))
                .andExpect(jsonPath("$.description", is(plan1.getDescription())))
                .andExpect(jsonPath("$.imageURL", is(plan1.getImageURL())))
                .andExpect(jsonPath("$.price", is(plan1.getPrice())))
                .andExpect(jsonPath("$.promo", is(plan1.getPromo())));
    }

    @Test
    @WithMockUser
    void createProduct() throws Exception {
        when(planService.createPlan(any(Plan.class))).thenReturn(plan1);

        this.mockMvc.perform(post("/api/v1/plans").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(plan1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(plan1.getId())))
                .andExpect(jsonPath("$.name", is(plan1.getName())))
                .andExpect(jsonPath("$.description", is(plan1.getDescription())))
                .andExpect(jsonPath("$.imageURL", is(plan1.getImageURL())))
                .andExpect(jsonPath("$.price", is(plan1.getPrice())))
                .andExpect(jsonPath("$.promo", is(plan1.getPromo())));
        }
    }

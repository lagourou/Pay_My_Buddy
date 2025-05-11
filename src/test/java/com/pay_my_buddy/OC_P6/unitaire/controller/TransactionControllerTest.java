package com.pay_my_buddy.OC_P6.unitaire.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.math.BigDecimal;
import com.pay_my_buddy.OC_P6.dto.TransactionRequestDTO;
import com.pay_my_buddy.OC_P6.model.User;
import com.pay_my_buddy.OC_P6.service.TransactionService;
import com.pay_my_buddy.OC_P6.controller.TransactionController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();

        BigDecimal.valueOf(100);
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setUsername("Laurent");
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "USER")
    void testPay_SuccessfulTransaction() throws Exception {
        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setEmail("friend@example.com");
        dto.setAmount(BigDecimal.valueOf(10));
        dto.setDescription("Déjeuner");

        when(transactionService.addNewTransaction(anyLong(), anyString(), any(BigDecimal.class), anyString()))
                .thenReturn(null);

        mockMvc.perform(post("/pay")
                .param("email", dto.getEmail())
                .param("amount", dto.getAmount().toString())
                .param("description", dto.getDescription()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transaction"))
                .andExpect(flash().attribute("success", "Paiement réussi"));
    }

}

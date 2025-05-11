package com.pay_my_buddy.OC_P6.unitaire.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pay_my_buddy.OC_P6.controller.UserConnectionsController;
import com.pay_my_buddy.OC_P6.model.User;
import com.pay_my_buddy.OC_P6.model.UserConnections;
import com.pay_my_buddy.OC_P6.service.UserConnectionsService;
import com.pay_my_buddy.OC_P6.service.UserService;
import com.pay_my_buddy.OC_P6.configuration.UserDetailsImplements;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.lang.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

class UserConnectionsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserConnectionsService userConnectionsService;

    @Mock
    private UserService userService;

    private UserConnectionsController userConnectionsController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        userConnectionsController = new UserConnectionsController(userConnectionsService, userService);

        HandlerMethodArgumentResolver mockPrincipalResolver = new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.getParameterAnnotation(AuthenticationPrincipal.class) != null;
            }

            @Override
            public Object resolveArgument(MethodParameter parameter,
                    @Nullable ModelAndViewContainer mavContainer,
                    NativeWebRequest webRequest,
                    @Nullable WebDataBinderFactory binderFactory) {
                UserDetailsImplements userDetails = mock(UserDetailsImplements.class);
                when(userDetails.getId()).thenReturn(1L);
                return userDetails;
            }
        };

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(userConnectionsController)
                .setViewResolvers(viewResolver)
                .setCustomArgumentResolvers(mockPrincipalResolver)
                .build();
    }

    @Test
    void testAddConnection_Success() throws Exception {
        User friend = new User();
        friend.setId(2L);
        friend.setEmail("friend@test.com");

        when(userService.getUserByEmail("friend@test.com")).thenReturn(friend);
        when(userConnectionsService.addConnections(1L, "friend@test.com"))
                .thenReturn(new UserConnections());

        mockMvc.perform(post("/add").param("email", "friend@test.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/userConnections?add"));

        verify(userConnectionsService).addConnections(1L, "friend@test.com");
    }

    @Test
    void testAddConnection_Error() throws Exception {
        when(userService.getUserByEmail("invalid@test.com")).thenReturn(null);

        mockMvc.perform(post("/add").param("email", "invalid@test.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/userConnections?add"));

        verify(userConnectionsService, never()).addConnections(anyLong(), anyString());
    }

    @Test
    void testShowConnections() throws Exception {
        mockMvc.perform(get("/userConnections")
                .param("success", "Opération réussie")
                .param("error", "Une erreur s'est produite"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("success", "Opération réussie"))
                .andExpect(model().attribute("error", "Une erreur s'est produite"))
                .andExpect(view().name("userConnections"));
    }
}

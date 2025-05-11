package com.pay_my_buddy.OC_P6.unitaire.configuration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import com.pay_my_buddy.OC_P6.configuration.UserDetailsImplements;
import com.pay_my_buddy.OC_P6.model.User;

class UserDetailsImplementsTest {

    private User user;
    private UserDetailsImplements userDetails;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("Laurent");
        user.setEmail("laurent@example.com");
        user.setPassword("securepassword");

        userDetails = new UserDetailsImplements(user);
    }

    @Test
    void testGetUsername() {
        assertEquals("Laurent", userDetails.getUsername());
    }

    @Test
    void testGetEmail() {
        assertEquals("laurent@example.com", userDetails.getEmail());
    }

    @Test
    void testGetId() {
        assertEquals(1L, userDetails.getId());
    }

    @Test
    void testGetPassword() {
        assertEquals("securepassword", userDetails.getPassword());
    }

    @Test
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty()); // ✅ Vérifie que la liste est vide
    }

    @Test
    void testIsAccountNonExpired() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void testIsCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        assertTrue(userDetails.isEnabled());
    }
}

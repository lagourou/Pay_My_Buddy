package com.pay_my_buddy.OC_P6.unitaire.mapper;

import com.pay_my_buddy.OC_P6.dto.RegisterRequestDTO;
import com.pay_my_buddy.OC_P6.mapper.UserMapper;
import com.pay_my_buddy.OC_P6.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testToRegisterRequestDTO() {
        User user = new User();
        user.setUsername("Laurent");
        user.setEmail("laurent@example.com");
        user.setPassword("securePass");

        RegisterRequestDTO dto = userMapper.toRegisterRequestDTO(user);

        assertNotNull(dto);
        assertEquals("Laurent", dto.getUsername());
        assertEquals("laurent@example.com", dto.getEmail());
        assertEquals("securePass", dto.getPassword());
    }

    @Test
    void testToRegisterRequestDTO_Null() {
        RegisterRequestDTO dto = userMapper.toRegisterRequestDTO(null);
        assertNull(dto);
    }

    @Test
    void testToUser() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setUsername("Jean");
        dto.setEmail("jean@example.com");
        dto.setPassword("strongPass");

        User user = userMapper.toUser(dto);

        assertNotNull(user);
        assertEquals("Jean", user.getUsername());
        assertEquals("jean@example.com", user.getEmail());
        assertEquals("strongPass", user.getPassword());

        assertNull(user.getId());
        assertNull(user.getFriends());
        assertNull(user.getReceivedTransactions());
        assertNull(user.getSentTransactions());

        assertEquals(BigDecimal.ZERO.stripTrailingZeros(), user.getAccountBalance().stripTrailingZeros());
    }

    @Test
    void testToUser_Null() {
        User user = userMapper.toUser(null);
        assertNull(user);
    }
}

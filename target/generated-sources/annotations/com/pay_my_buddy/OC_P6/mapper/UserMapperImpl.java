package com.pay_my_buddy.OC_P6.mapper;

import com.pay_my_buddy.OC_P6.dto.RegisterRequestDTO;
import com.pay_my_buddy.OC_P6.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(value = "org.mapstruct.ap.MappingProcessor", date = "2025-05-11T06:49:03+0200", comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.15 (Eclipse Adoptium)")
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public RegisterRequestDTO toRegisterRequestDTO(User user) {
        if (user == null) {
            return null;
        }

        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();

        registerRequestDTO.setUsername(user.getUsername());
        registerRequestDTO.setEmail(user.getEmail());
        registerRequestDTO.setPassword(user.getPassword());

        return registerRequestDTO;
    }

    @Override
    public User toUser(RegisterRequestDTO registerRequestDTO) {
        if (registerRequestDTO == null) {
            return null;
        }

        User user = new User();

        user.setUsername(registerRequestDTO.getUsername());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(registerRequestDTO.getPassword());

        return user;
    }
}

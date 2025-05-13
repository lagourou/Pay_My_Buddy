package com.pay_my_buddy.OC_P6.mapper;

import com.pay_my_buddy.OC_P6.dto.RegisterRequestDTO;
import com.pay_my_buddy.OC_P6.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-13T16:03:58+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.42.0.v20250508-0718, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public RegisterRequestDTO toRegisterRequestDTO(User user) {
        if ( user == null ) {
            return null;
        }

        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();

        registerRequestDTO.setEmail( user.getEmail() );
        registerRequestDTO.setPassword( user.getPassword() );
        registerRequestDTO.setUsername( user.getUsername() );

        return registerRequestDTO;
    }

    @Override
    public User toUser(RegisterRequestDTO registerRequestDTO) {
        if ( registerRequestDTO == null ) {
            return null;
        }

        User user = new User();

        user.setEmail( registerRequestDTO.getEmail() );
        user.setPassword( registerRequestDTO.getPassword() );
        user.setUsername( registerRequestDTO.getUsername() );

        return user;
    }
}

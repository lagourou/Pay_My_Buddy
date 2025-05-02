package com.pay_my_buddy.OC_P6.mapper;

import com.pay_my_buddy.OC_P6.dto.RegisterRequestDTO;
import com.pay_my_buddy.OC_P6.model.User;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-01T17:26:18+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.42.0.z20250331-1358, environment: Java 21.0.6 (Eclipse Adoptium)"
)
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

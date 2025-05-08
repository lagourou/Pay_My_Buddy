package com.pay_my_buddy.OC_P6.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.pay_my_buddy.OC_P6.dto.RegisterRequestDTO;
import com.pay_my_buddy.OC_P6.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    public RegisterRequestDTO toRegisterRequestDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "friends", ignore = true)
    @Mapping(target = "receivedTransactions", ignore = true)
    @Mapping(target = "sentTransactions", ignore = true)
    @Mapping(target = "accountBalance", ignore = true)

    public User toUser(RegisterRequestDTO registerRequestDTO);

}
package com.pay_my_buddy.OC_P6.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.pay_my_buddy.OC_P6.dto.TransactionResponseDTO;
import com.pay_my_buddy.OC_P6.model.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "senderId", source = "transaction.sender.id")
    @Mapping(target = "senderUsername", source = "transaction.sender.username")
    @Mapping(target = "senderEmail", source = "transaction.sender.email")
    @Mapping(target = "senderBalance", source = "transaction.sender.accountBalance")
    @Mapping(target = "receiverId", source = "transaction.receiver.id")
    @Mapping(target = "receiverUsername", source = "transaction.receiver.username")
    @Mapping(target = "receiverEmail", source = "transaction.receiver.email")
    TransactionResponseDTO toTransactionResponseDTO(Transaction transaction);

    @Mapping(target = "sender.id", source = "senderId")
    @Mapping(target = "receiver.id", source = "receiverId")
    Transaction toTransaction(TransactionResponseDTO transactionDTO);

}

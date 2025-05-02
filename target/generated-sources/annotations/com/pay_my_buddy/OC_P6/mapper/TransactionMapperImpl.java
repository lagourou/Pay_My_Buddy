package com.pay_my_buddy.OC_P6.mapper;

import com.pay_my_buddy.OC_P6.dto.TransactionResponseDTO;
import com.pay_my_buddy.OC_P6.model.Transaction;
import com.pay_my_buddy.OC_P6.model.User;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-01T17:26:15+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.42.0.z20250331-1358, environment: Java 21.0.6 (Eclipse Adoptium)"
)
@Component
public class TransactionMapperImpl implements TransactionMapper {

    @Override
    public TransactionResponseDTO toTransactionResponseDTO(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }

        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();

        transactionResponseDTO.setSenderId( transactionSenderId( transaction ) );
        transactionResponseDTO.setSenderUsername( transactionSenderUsername( transaction ) );
        transactionResponseDTO.setSenderEmail( transactionSenderEmail( transaction ) );
        transactionResponseDTO.setSenderBalance( transactionSenderAccountBalance( transaction ) );
        transactionResponseDTO.setReceiverId( transactionReceiverId( transaction ) );
        transactionResponseDTO.setReceiverUsername( transactionReceiverUsername( transaction ) );
        transactionResponseDTO.setReceiverEmail( transactionReceiverEmail( transaction ) );
        transactionResponseDTO.setAmount( transaction.getAmount() );
        transactionResponseDTO.setDescription( transaction.getDescription() );
        transactionResponseDTO.setId( transaction.getId() );
        transactionResponseDTO.setTransactionDate( transaction.getTransactionDate() );

        return transactionResponseDTO;
    }

    @Override
    public Transaction toTransaction(TransactionResponseDTO transactionDTO) {
        if ( transactionDTO == null ) {
            return null;
        }

        Transaction transaction = new Transaction();

        transaction.setSender( transactionResponseDTOToUser( transactionDTO ) );
        transaction.setReceiver( transactionResponseDTOToUser1( transactionDTO ) );
        transaction.setAmount( transactionDTO.getAmount() );
        transaction.setDescription( transactionDTO.getDescription() );
        transaction.setId( transactionDTO.getId() );
        transaction.setTransactionDate( transactionDTO.getTransactionDate() );

        return transaction;
    }

    private Long transactionSenderId(Transaction transaction) {
        User sender = transaction.getSender();
        if ( sender == null ) {
            return null;
        }
        return sender.getId();
    }

    private String transactionSenderUsername(Transaction transaction) {
        User sender = transaction.getSender();
        if ( sender == null ) {
            return null;
        }
        return sender.getUsername();
    }

    private String transactionSenderEmail(Transaction transaction) {
        User sender = transaction.getSender();
        if ( sender == null ) {
            return null;
        }
        return sender.getEmail();
    }

    private BigDecimal transactionSenderAccountBalance(Transaction transaction) {
        User sender = transaction.getSender();
        if ( sender == null ) {
            return null;
        }
        return sender.getAccountBalance();
    }

    private Long transactionReceiverId(Transaction transaction) {
        User receiver = transaction.getReceiver();
        if ( receiver == null ) {
            return null;
        }
        return receiver.getId();
    }

    private String transactionReceiverUsername(Transaction transaction) {
        User receiver = transaction.getReceiver();
        if ( receiver == null ) {
            return null;
        }
        return receiver.getUsername();
    }

    private String transactionReceiverEmail(Transaction transaction) {
        User receiver = transaction.getReceiver();
        if ( receiver == null ) {
            return null;
        }
        return receiver.getEmail();
    }

    protected User transactionResponseDTOToUser(TransactionResponseDTO transactionResponseDTO) {
        if ( transactionResponseDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( transactionResponseDTO.getSenderId() );

        return user;
    }

    protected User transactionResponseDTOToUser1(TransactionResponseDTO transactionResponseDTO) {
        if ( transactionResponseDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( transactionResponseDTO.getReceiverId() );

        return user;
    }
}

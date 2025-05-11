package com.pay_my_buddy.OC_P6.unitaire.mapper;

import com.pay_my_buddy.OC_P6.dto.TransactionResponseDTO;
import com.pay_my_buddy.OC_P6.mapper.TransactionMapper;
import com.pay_my_buddy.OC_P6.model.Transaction;
import com.pay_my_buddy.OC_P6.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransactionMapperTest {

    private final TransactionMapper transactionMapper = Mappers.getMapper(TransactionMapper.class);

    @Test
    void testToTransactionResponseDTO() {
        User sender = new User();
        sender.setId(1L);
        sender.setUsername("Laurent");
        sender.setEmail("laurent@example.com");
        sender.setAccountBalance(BigDecimal.valueOf(200));

        User receiver = new User();
        receiver.setId(2L);
        receiver.setUsername("Jean");
        receiver.setEmail("jean@example.com");
        receiver.setAccountBalance(BigDecimal.valueOf(150));

        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);

        TransactionResponseDTO dto = transactionMapper.toTransactionResponseDTO(transaction);

        assertNotNull(dto);
        assertEquals(1L, dto.getSenderId());
        assertEquals("Laurent", dto.getSenderUsername());
        assertEquals("laurent@example.com", dto.getSenderEmail());
        assertEquals(BigDecimal.valueOf(200), dto.getSenderBalance());

        assertEquals(2L, dto.getReceiverId());
        assertEquals("Jean", dto.getReceiverUsername());
        assertEquals("jean@example.com", dto.getReceiverEmail());
    }

    @Test
    void testToTransactionResponseDTO_Null() {
        TransactionResponseDTO dto = transactionMapper.toTransactionResponseDTO(null);
        assertNull(dto);
    }

    @Test
    void testToTransaction() {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setSenderId(1L);
        dto.setReceiverId(2L);

        Transaction transaction = transactionMapper.toTransaction(dto);

        assertNotNull(transaction);
        assertEquals(1L, transaction.getSender().getId());
        assertEquals(2L, transaction.getReceiver().getId());
    }

    @Test
    void testToTransaction_Null() {
        Transaction transaction = transactionMapper.toTransaction(null);
        assertNull(transaction);
    }
}

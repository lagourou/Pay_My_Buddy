package com.pay_my_buddy.OC_P6.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserConnectionsResponseDTO {

    private Long id;
    private String username;
    private String email;

}

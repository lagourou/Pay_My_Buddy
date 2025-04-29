package com.pay_my_buddy.OC_P6.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserConnectionId implements Serializable {

    private Long userId;
    private Long connectionId;

}

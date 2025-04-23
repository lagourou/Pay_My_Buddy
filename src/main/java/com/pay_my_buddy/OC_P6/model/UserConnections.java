package com.pay_my_buddy.OC_P6.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_connections")
@Data
public class UserConnections {

    @EmbeddedId
    private UserConnectionId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("connectionId")
    @JoinColumn(name = "connection_id", nullable = false)
    private User connection;
}

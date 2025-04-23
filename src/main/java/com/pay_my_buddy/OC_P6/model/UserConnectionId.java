package com.pay_my_buddy.OC_P6.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Embeddable
@RequiredArgsConstructor
@Data
public class UserConnectionId implements Serializable {

    private final int userId;
    private final int connectionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true; // On compare l'objet actuel à l'objet o en paramètre
        }
        if (o == null || getClass() != o.getClass()) {
            return false; // On vérifie si l'objet est null ou d'une autre classe
        }

        UserConnectionId that = (UserConnectionId) o; // Convertit l'objet o en type UserConnectionId
        return userId == that.userId && connectionId == that.connectionId; // Compare les champs userId et connectionId
    }

    // Garantie que des objets égaux (equals) ont le même code
    @Override
    public int hashCode() {
        return Objects.hash(userId, connectionId);
    }
}

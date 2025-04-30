package com.pay_my_buddy.OC_P6.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pay_my_buddy.OC_P6.model.UserConnectionId;
import com.pay_my_buddy.OC_P6.model.UserConnections;

@Repository
public interface UserConnectionsRepository extends JpaRepository<UserConnections, UserConnectionId> {

    List<UserConnections> findByUserId(Long userId);

    boolean existsByUserIdAndConnectionId(Long userId, Long connectionId);

    @Query("SELECT uc FROM UserConnections uc JOIN FETCH uc.connection WHERE uc.user.id = :userId")
    List<UserConnections> findConnectionsByUserId(@Param("userId") Long userId);

}

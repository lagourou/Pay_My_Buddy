package com.pay_my_buddy.OC_P6.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pay_my_buddy.OC_P6.model.UserConnectionId;
import com.pay_my_buddy.OC_P6.model.UserConnections;

@Repository
public interface UserConnectionsRepository extends JpaRepository<UserConnections, UserConnectionId> {

}

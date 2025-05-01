package com.src.proserv.main.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="user_service_request_task")
public class UserServiceRequestTask extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userUUID;

    private Long serviceOptionID;

    private Long serviceCategoryID;

    private Long serviceAddressID;

    private String status;
}

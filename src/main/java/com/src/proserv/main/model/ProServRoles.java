package com.src.proserv.main.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class ProServRoles  extends Auditable {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userUUID;

    private String roles;

}

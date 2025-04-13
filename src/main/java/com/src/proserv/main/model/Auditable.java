package com.src.proserv.main.model;

import java.time.LocalDateTime;

import javax.persistence.MappedSuperclass;

import lombok.Data;

@MappedSuperclass
@Data
public abstract class Auditable {

    String createdBy;
    LocalDateTime createdOn;
    String lastModifiedBy;
    LocalDateTime lastModifiedOn;
}

package com.src.proserv.main.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@Table(name="location_service_task")
public class LocationServiceTask extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long locationID;

    private Long serviceTaskID;

    private Boolean enabled;
}

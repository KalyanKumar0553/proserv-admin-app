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
@Table(name="service_task_option")
public class ServiceTaskOption extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    private String description;

    private Long serviceCategoryID;
    
    private Long serviceTaskID;
    
    private Long defaultAmount;
    
}

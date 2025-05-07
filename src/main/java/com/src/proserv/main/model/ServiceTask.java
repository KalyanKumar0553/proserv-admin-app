package com.src.proserv.main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name="service_task")
@NoArgsConstructor
@AllArgsConstructor
public class ServiceTask extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 255, nullable = false, unique = true)
    private String title;
    
    private Long serviceCategoryID;

    private String displayURL;

    private String description;
    
    private String inclusions;
    
    private String exclusions;
    
    private String note;
    
    private boolean enabled;
    
}
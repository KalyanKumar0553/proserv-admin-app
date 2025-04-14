package com.src.proserv.main.model;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

	@CreatedBy
	@Column(name = "created_by", updatable = false)
	String createdBy;
    
	@Column(name = "created_on", updatable = false)
	LocalDateTime createdOn;
    
    @LastModifiedBy
    String lastModifiedBy;
    
    LocalDateTime lastModifiedOn;
    
    String timezoneID;
    
    
    @PrePersist
    public void prePersist() {
        this.createdOn = LocalDateTime.now();
        this.lastModifiedOn = LocalDateTime.now();
        this.timezoneID = ZoneId.systemDefault().getId();
    }

    @PreUpdate
    public void preUpdate() {
        this.lastModifiedOn = LocalDateTime.now();
        this.timezoneID = ZoneId.systemDefault().getId();
    }
}

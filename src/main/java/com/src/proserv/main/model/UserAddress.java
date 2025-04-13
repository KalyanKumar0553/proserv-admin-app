package com.src.proserv.main.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "user_address")
public class UserAddress extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long addressID;

	private String userUUID;

	private String stateCode;

	private String districtCode;

	private String city;

	private String labelName;

	private String location;

	private String street;

	private String houseNumber;

	private String landmark;

	private String contactPerson;

	private String contactMobile;
}

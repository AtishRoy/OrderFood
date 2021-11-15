package com.mindtree.customer.management.model;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <pre>
 * <b>Description : </b>
 * Address.
 *
 * &#64;author $Author: Atish Roy $
 * </pre>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "address")
@ApiModel(value = "Address", description = "Address details")
public class Address implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7149653967805308710L;

	/**
	 * addressID.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "address_id")
	@ApiModelProperty(required = true, notes = "Id of the Address")
	private Long addressId;

	/**
	 * address.
	 */
	@Column(name = "address", nullable = false)
	@NotBlank(message = "Address Line in Address cannot be null")
	@ApiModelProperty(required = true, notes = "Address text of the Address")
	private String address;

	/**
	 * landmark.
	 */
	@Column(name = "landmark", nullable = true)
	// @NotBlank(message = "Landmark in Address cannot be null")
	@ApiModelProperty(required = false, notes = "Landmark of the Address")
	private String landmark;

	/**
	 * area.
	 */
	@Column(name = "area", nullable = false)
	@NotBlank(message = "Area in Address cannot be null")
	@ApiModelProperty(required = true, notes = "Area of the Address")
	private String area;

	/**
	 * city.
	 */
	@Column(name = "city", nullable = false)
	@NotBlank
	@ApiModelProperty(required = true, notes = "City of the Address")
	private String city;

	/**
	 * state.
	 */
	@Column(name = "state", nullable = false)
	@NotBlank(message = "State in Address cannot be null")
	@ApiModelProperty(required = true, notes = "State of the Address")
	private String state;

	/**
	 * latitude.
	 */
	@Column(name = "latitude", nullable = false)
	@NotBlank(message = "Latitude in Address cannot be null")
	@ApiModelProperty(required = true, notes = "Latitude of the Address")
	private String latitude;

	/**
	 * longitude.
	 */
	@Column(name = "longitude", nullable = false)
	@NotBlank(message = "Longitude in Address cannot be null")
	@ApiModelProperty(required = true, notes = "Longitude of the Address")
	private String longitude;

	/**
	 * pinCode.
	 */
	@Column(name = "pinCode", nullable = false)
	@NotBlank(message = "Pin Code in Address cannot be null")
	@ApiModelProperty(required = true, notes = "Pin Code of the Address")
	private String pinCode;

}

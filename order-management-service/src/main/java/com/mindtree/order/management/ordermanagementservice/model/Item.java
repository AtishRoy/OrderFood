package com.mindtree.order.management.ordermanagementservice.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Range;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Atish Roy.
 *
 */
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "item")
public class Item implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5410914868373863311L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "item_id")
	private Long itemId;

	@Column(name = "item_name")
	private String itemName;

	@Column(name = "quantity", nullable = false)
	@Range(min = 1, max = 500, message = "Quantity cannot be 0 / Quantity cannot exceed 500")
	private Integer quantity;

}

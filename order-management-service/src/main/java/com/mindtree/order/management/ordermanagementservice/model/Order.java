package com.mindtree.order.management.ordermanagementservice.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "order_details")
public class Order implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "order_id", unique = true)
	private Long orderId;

	@Column(name = "restaurant_id", nullable = false)
	private String restaurantId;

	@Column(name = "customer_id", nullable = false)
	private Long customerId;

	@JoinColumn(name = "order_id", nullable = false)
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Item> itemList;

	@Column(name = "total_price", nullable = false)
	@Range(min = 1, max = 10000, message = "Price should be between 1 to 10000")
	private Float totalPrice;

	@Column(name = "delivery_address", nullable = false)
	private String deliveryAddress;

	@Column(name = "contact_number", nullable = false)
	private String contactNumber;

	@Column(name = "order_status")
	@Enumerated(EnumType.STRING)
	private OrderStatusType orderStatus;

	@JoinColumn(name = "transaction_id")
	@OneToOne(cascade = CascadeType.ALL)
	private Transaction transaction;

}

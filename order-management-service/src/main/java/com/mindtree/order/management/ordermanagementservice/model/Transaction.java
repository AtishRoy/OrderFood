/**
 *
 */
package com.mindtree.order.management.ordermanagementservice.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author M1026341
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "transaction_detail")
public class Transaction implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "transaction_id", unique = true)
	private String transactionId;

	@Column(name = "payment_mode", nullable = false)
	@NotNull(message = "Payment Mode cannot be null")
	@Enumerated(EnumType.STRING)
	private PaymentMode paymentMode;

	@Column(name = "payment_status", nullable = false)
	@NotNull(message = "Payment Status cannot be null")
	@Enumerated(EnumType.STRING)
	private PaymentStatusType paymentStatus;

}

package com.mindtree.order.management.ordermanagementservice.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mindtree.order.management.ordermanagementservice.model.Order;
import com.mindtree.order.management.ordermanagementservice.model.OrderStatusType;

@Repository
public interface OrderManagementRepository extends JpaRepository<Order, Long> {
	
	@Transactional
	@Modifying
	@Query("UPDATE Order o SET o.orderStatus = :status WHERE o.orderId = :orderId")
    int cancelOrderById(Long orderId, OrderStatusType status);

	List<Order> findByRestaurantIdAndCustomerId(String restuarantId, Long customerId);
	
	List<Order> findByCustomerId(Long id, Pageable pageable);
	
	Order findByOrderId(Long id);
	
	List<Order> findByOrderIdAndRestaurantId(Long orderId, String restuarantId);
	
	List<Order> findByRestaurantId(String id, Pageable pageable);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE from Order o WHERE o.orderId = :orderId")
    int deleteByOrderId(Long orderId);
}

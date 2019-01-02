package com.demoapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.demoapp.entity.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	@Query("update OrderItem oi set oi.order.id = null where oi.id = ?1")
	List<Long> unlinkOrderItemFromOrder(Long id);

}

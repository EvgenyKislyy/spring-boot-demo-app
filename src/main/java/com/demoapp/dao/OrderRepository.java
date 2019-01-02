package com.demoapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.demoapp.dto.ReportDto;
import com.demoapp.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query("select new com.demoapp.dto.ReportDto(o.createdAt, sum(p.price*oi.quantity*o.amount)) from Order o inner join o.orderItems oi inner join oi.product p group by o.createdAt")
	List<ReportDto> getReports();

	@Query("select p.name from Order o inner join o.orderItems oi inner join oi.product p where o.id = ?1")
	List<String> getOrderProductNames(Long id);

	@Query("select o.id from com.demoapp.entity.Order o ")
	List<Long> getAllOrderIds();

	@Query("select oi.id from Order o inner join o.orderItems oi where o.id = ?1")
	List<Long> getOrderItemIdsByOrderId(Long id);

}

package com.demoapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.demoapp.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("select o.id from com.demoapp.entity.Order o inner join o.orderItems oi inner join oi.product p where p.id = ?1")
	List<Long> getOrderIds(Long productId);

	List<Product> findProductsByCategoryId(Long id);

}

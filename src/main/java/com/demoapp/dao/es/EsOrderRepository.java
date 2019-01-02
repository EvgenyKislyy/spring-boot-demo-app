package com.demoapp.dao.es;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.demoapp.entity.elastic.Order;

@Repository
public interface EsOrderRepository extends ElasticsearchRepository<Order, String> {

	List<Order> findByProductsName(String name);

}

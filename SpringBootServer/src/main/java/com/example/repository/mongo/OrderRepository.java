package com.example.repository.mongo;

import com.example.common.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Tran Minh Truyen
 */
@Repository
public interface OrderRepository extends MongoRepository<Order, Integer>{
	List<Order> findOrderByCustomerId(int customerId);
	Order findOrderById(int id);
	Order findOrderByIdAndCustomerId(int id, int customerId);
}

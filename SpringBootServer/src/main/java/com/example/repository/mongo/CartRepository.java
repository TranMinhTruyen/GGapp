package com.example.repository.mongo;

import com.example.common.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Tran Minh Truyen
 */
@Repository
public interface CartRepository extends MongoRepository<Cart, Integer> {
}

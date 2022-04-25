package com.example.repository.mongo;

import com.example.common.model.ProductImage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductImageRepository extends MongoRepository<ProductImage, Integer> {
}

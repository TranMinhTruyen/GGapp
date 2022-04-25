package com.example.repository.mongo;

import com.example.common.model.ConfirmKey;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfirmKeyRepository extends MongoRepository<ConfirmKey, String> {
	ConfirmKey findByEmailEquals(String email);
	void deleteByEmail(String email);
}

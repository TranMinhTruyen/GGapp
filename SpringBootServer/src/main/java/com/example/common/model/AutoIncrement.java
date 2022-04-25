package com.example.common.model;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Arrays;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */

public class AutoIncrement {

	private MongoRepository repository;

	public AutoIncrement(MongoRepository repository) {
		this.repository = repository;
	}

	public List getLastOfCollection(){
		List last = this.repository.findAll();
		if (!last.isEmpty()) return Arrays.asList(last.stream().skip(this.repository.count()-1).toArray());
		else return null;
	}
}

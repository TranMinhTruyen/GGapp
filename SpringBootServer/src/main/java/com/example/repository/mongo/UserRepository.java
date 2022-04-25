package com.example.repository.mongo;

import com.example.common.entity.Brand;
import com.example.common.model.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */
@Repository
public interface UserRepository extends MongoRepository<User, Integer>{
    List<User> findUserByFirstNameContainingOrLastNameContaining(String firstName, String lastName);
    User findUsersByAccountEqualsAndPasswordEquals(String account, String password);
    User findUserByAccount(String account);
	User findUserByEmailEqualsIgnoreCase(String email);
}

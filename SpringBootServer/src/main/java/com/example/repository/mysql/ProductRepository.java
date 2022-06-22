package com.example.repository.mysql;

import com.example.common.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Tran Minh Truyen
 */

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
	Product findProductById(int id);
	List<Product> findProductByNameContains(String name);
	List<Product> findAllByBrandId(int id);
	List<Product> findAllByCategoryId(int id);

	List<Product> findAllByNameEqualsIgnoreCase(String name);

	@Query("select p from Product p where p.isNew = true")
	List<Product> findAllByNewIsTrue();
}

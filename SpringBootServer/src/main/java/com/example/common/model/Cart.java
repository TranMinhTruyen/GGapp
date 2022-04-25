package com.example.common.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */
@Document(collection = "Cart")
@Data
public class Cart {

	private int id;

	@Field(value = "productList")
	private List<ListProduct> productList;

	@Field(value = "totalPrice")
	private float totalPrice;

	@Override
	public String toString() {
		return "Cart{" +
				"userId=" + id +
				", productList=" + productList +
				", totalPrice=" + totalPrice +
				'}';
	}
}

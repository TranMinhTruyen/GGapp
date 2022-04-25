package com.example.common.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */
@Document(collection = "Order")
@Data
public class Order {

	private int id;

	@Field(value = "customerId")
	private int customerId;

	@Field(value = "employeeId")
	private int employeeId;

	@Field(value = "createDate")
	private Date createDate;

	@Field(value = "cart")
	private List<ListProduct> listProducts;

	@Field(value = "totalPrice")
	private float totalPrice;

	@Field(value = "Andress")
	private String andress;

	@Field(value = "status")
	private String status;
}

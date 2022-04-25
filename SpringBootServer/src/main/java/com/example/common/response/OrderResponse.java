package com.example.common.response;

import com.example.common.model.Cart;
import com.example.common.model.ListProduct;
import com.example.common.request.OrderRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */
@Data
public class OrderResponse{
	private int id;
	private int customerId;
	private int employeeId;
	private Date createDate;
	private List<ListProduct> listProducts;
	private float totalPrice;
	private String status;
	private String andress;
}

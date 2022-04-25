package com.example.services;

import com.example.common.request.OrderRequest;
import com.example.common.response.CommonResponse;
import com.example.common.response.OrderResponse;

/**
 * @author Tran Minh Truyen
 */
public interface OrderServices {
	OrderResponse createOrderByCart(int customerId);
	OrderResponse createOrderByProductId(int customerId, int[] productId);
	CommonResponse getOrderByCustomerId(int page, int size, int id);
	boolean updateOrder(int id, OrderRequest orderRequest);
	boolean deleteOrder(int id, int customerId);
}

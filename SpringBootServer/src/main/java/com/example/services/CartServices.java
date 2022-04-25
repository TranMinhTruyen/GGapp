package com.example.services;

import com.example.common.model.ListProduct;
import com.example.common.request.CartRequest;
import com.example.common.request.ProductRequest;
import com.example.common.response.CartResponse;
import org.springframework.stereotype.Service;

/**
 * @author Tran Minh Truyen
 */
public interface CartServices {
	boolean createCart(int customerId, int productId, int productAmount);
	CartResponse getCartById(int id);
	boolean updateProductAmount(int customerId, int productId, long amount);
	boolean deleteCart(int id);
	boolean deleteCartAfterCreateOrder(int id);
	boolean addProductToCart(int customerId, int productId, int productAmount);
	boolean removeProductFromCart(int customerId, int productId);
	boolean isCartExists(int customerId);
}

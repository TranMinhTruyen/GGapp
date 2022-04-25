package com.example.common.response;

import com.example.common.model.ListProduct;
import com.example.common.request.CartRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Tran Minh Truyen
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CartResponse extends CartRequest {

	private float totalPrice;
}

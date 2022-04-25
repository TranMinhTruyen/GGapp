package com.example.common.request;

import com.example.common.model.ListProduct;
import lombok.Data;

import javax.persistence.Id;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */
@Data
public class CartRequest {
	private int customerId;
	private List<ListProduct> productList;
}

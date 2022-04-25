package com.example.services;

import com.example.common.request.ProductRequest;
import com.example.common.response.CommonResponse;
import com.example.common.response.ProductResponse;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */
public interface ProductServices {
	ProductResponse createProduct(ProductRequest productRequest) throws Exception;
	CommonResponse getAllProduct(int page, int size) throws Exception;
	CommonResponse getProductByKeyWord(int page, int size,
									   @Nullable String name,
									   @Nullable String brand,
									   @Nullable String category,
									   float fromPrice,
									   float toPrice) throws Exception;
	ProductResponse updateProduct(int id, ProductRequest productRequest) throws Exception;
	boolean deleteProduct(List<Integer> id);
	boolean deleteImageOfProduct(int productId, List<Integer> imageId);
	boolean isExists(String productName);
}

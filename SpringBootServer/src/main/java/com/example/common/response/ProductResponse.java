package com.example.common.response;

import lombok.Data;

import java.util.List;

/**
 * @author Tran Minh Truyen
 */
@Data
public class ProductResponse {
	private int id;
	private String name;
	private float price;
	private String type;
	private float discount;
	private float unitInStock;
	private String brand;
	private String category;
	private boolean isNew;
	private List<ProductImageResponse> image;
}

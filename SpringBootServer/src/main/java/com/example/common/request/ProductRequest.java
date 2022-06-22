package com.example.common.request;

import lombok.Data;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */

@Data
public class ProductRequest {
	private String name;
	private float price;
	private String type;
	private float discount;
	private float unitInStock;
	private int id_brand;
	private int id_category;
	private boolean isNew;
	private List<ProductImageRequest> image;
}

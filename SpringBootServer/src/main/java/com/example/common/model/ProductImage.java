package com.example.common.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

@Document(collection = "ProductImage")
@Data
public class ProductImage implements Serializable {

	private int id;

	@Field(value = "Images")
	private List<Image> images;
}

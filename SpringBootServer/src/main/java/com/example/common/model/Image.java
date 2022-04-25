package com.example.common.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
@Data
public class Image {
	@Field(value = "ImageID")
	private int imageId;

	@Field(value = "Image")
	private String image;
}

package com.example.common.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "News")
@Data
public class News {
	private int id;

	@Field(value = "userCreateId")
	private int userCreateId;

	@Field(value = "userCreateName")
	private String userCreateName;

	@Field(value = "createDate")
	private Date createDate;

	@Field(value = "newsTitle")
	private String newsTitle;

	@Field(value = "title")
	private String content;
}

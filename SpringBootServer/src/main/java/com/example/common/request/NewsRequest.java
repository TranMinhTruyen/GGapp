package com.example.common.request;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class NewsRequest {
	private String title;
	private String content;
}

package com.example.common.response;

import com.example.common.request.CategoryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryResponse extends CategoryRequest {
	private int id;
}

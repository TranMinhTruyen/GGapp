package com.example.services;

import com.example.common.request.CategoryRequest;
import com.example.common.response.CategoryResponse;
import com.example.common.response.CommonResponse;

public interface CategoryServices {
	boolean createCategory(CategoryRequest categoryRequest);
	CommonResponse getAllCategory(int page, int size);
	CommonResponse getCategoryByKeyword(int page, int size, String keyword);
	CategoryResponse updateCategory(int id, CategoryRequest categoryRequest);
	boolean deleteCategory(int id);
	boolean isExists(String categoryName);
}

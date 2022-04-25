package com.example.services;

import com.example.common.request.BrandRequest;
import com.example.common.response.BrandResponse;
import com.example.common.response.CommonResponse;

public interface BrandServices {
	boolean createBrand(BrandRequest brandRequest);
	CommonResponse getAllBrand(int page, int size);
	CommonResponse getBrandbyKeyword(int page, int size, String keyword);
	BrandResponse updateBrand(int id, BrandRequest brandRequest);
	boolean deleteBrand(int id);
	boolean isExists(String brandName);
}

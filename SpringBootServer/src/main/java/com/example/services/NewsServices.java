package com.example.services;

import com.example.common.request.NewsRequest;
import com.example.common.response.BaseResponse;
import com.example.common.response.CommonResponse;
import com.example.common.response.NewsResponse;

public interface NewsServices {
	NewsResponse createNews (int userCreateId, NewsRequest newsRequest) throws Exception;
	CommonResponse getAllNews (int page, int size) throws Exception;
	CommonResponse getNewsByKeyWord (int page, int size, int userCreateId, String userCreateName, String title) throws Exception;
	NewsResponse updateNews (int newsId, NewsRequest newsRequest) throws Exception;
	BaseResponse deleteNews (int newsId) throws Exception;
}

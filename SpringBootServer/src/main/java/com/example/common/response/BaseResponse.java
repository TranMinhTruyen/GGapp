package com.example.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
	private int statusCode;
	private String statusName;
	private String message;
	private Object payload;
}

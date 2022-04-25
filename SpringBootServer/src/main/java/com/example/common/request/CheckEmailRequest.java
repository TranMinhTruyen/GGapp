package com.example.common.request;
import lombok.Data;

@Data
public class CheckEmailRequest {
	private String email;
	private String confirmKey;
}

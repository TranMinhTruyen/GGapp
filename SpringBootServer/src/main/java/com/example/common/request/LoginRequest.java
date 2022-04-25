package com.example.common.request;

import lombok.Data;

/**
 * @author Tran Minh Truyen
 */
@Data
public class LoginRequest {
	private String account;
	private String password;
}

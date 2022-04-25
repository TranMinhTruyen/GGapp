package com.example.common.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Tran Minh Truyen
 */
@Data
public class JwtResponse {
	private String tokenType = "Bearer";
	private String accessToken;

	public JwtResponse(String accessToken) {
		this.accessToken = accessToken;
	}
}

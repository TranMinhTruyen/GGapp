package com.example.common.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserOrderRequest implements Serializable {
	private String andress;
	private int [] productId;
}

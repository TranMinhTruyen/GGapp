package com.example.common.response;

import com.example.common.request.BrandRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BrandResponse extends BrandRequest {
	private int id;
}

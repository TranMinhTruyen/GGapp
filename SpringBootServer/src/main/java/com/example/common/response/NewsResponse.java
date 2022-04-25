package com.example.common.response;

import com.example.common.request.NewsRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class NewsResponse extends NewsRequest {
	private int id;
	private int userCreateId;
	private String userCreateName;
	private Date createDate;
}

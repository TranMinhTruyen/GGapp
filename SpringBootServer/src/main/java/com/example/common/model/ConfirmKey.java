package com.example.common.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "ConfirmKey")
@Data
public class ConfirmKey {
	@Field(value = "Email")
	private String email;

	@Field(value = "Key")
	private String key;

	@Field(value = "Expire")
	private Date expire;

	@Override
	public String toString() {
		return "ConfirmKey{" +
				"email='" + email + '\'' +
				", key='" + key + '\'' +
				", expire=" + expire +
				'}';
	}
}

package com.example.common.response;

import com.example.common.request.UserRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author Tran Minh Truyen
 */
@Data
public class UserResponse {
	private String firstName;
	private String lastName;
	private String email;
	private Date birthDay;
	private String address;
	private String district;
	private String city;
	private String postCode;
	private String citizenID;
	private String image;
	private String role;
	private boolean isActive;
}

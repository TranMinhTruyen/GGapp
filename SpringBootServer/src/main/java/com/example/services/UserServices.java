package com.example.services;

import com.example.common.model.User;
import com.example.common.request.LoginRequest;
import com.example.common.request.UserRequest;
import com.example.common.response.CommonResponse;
import com.example.common.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Tran Minh Truyen
 */
public interface UserServices {
	UserResponse createUser(UserRequest userRequest) throws Exception;
	CommonResponse getAllUser(int page, int size) throws Exception;
	CommonResponse getUserByKeyWord(int page, int size, String keyword) throws Exception;
	User login(LoginRequest loginRequest) throws Exception;
	User resetPassword(String email) throws Exception;
	UserResponse getProfileUser(int id) throws Exception;
	String sendEmailConfirmKey(String email, String confirmKey);
	boolean checkConfirmKey(String email, String key);
	UserResponse updateUser(int id, UserRequest request) throws Exception;
	boolean deleteUser(int id) throws Exception;
	boolean accountIsExists(String account);
	boolean emailIsExists(String email);
	UserDetails loadUserByUsername(String account);
	UserDetails loadUserById(int id);
}

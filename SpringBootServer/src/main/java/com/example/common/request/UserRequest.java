package com.example.common.request;

import com.example.common.response.UserResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author Tran Minh Truyen
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserRequest extends UserResponse {
    private String account;
    private String password;
}

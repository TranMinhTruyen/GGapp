package com.example.controller;
import com.example.common.jwt.JWTTokenProvider;
import com.example.common.jwt.CustomUserDetail;
import com.example.common.model.ResetPassword;
import com.example.common.model.User;
import com.example.common.request.CheckEmailRequest;
import com.example.common.request.LoginRequest;
import com.example.common.request.UserRequest;
import com.example.common.response.BaseResponse;
import com.example.common.response.CommonResponse;
import com.example.common.response.JwtResponse;
import com.example.common.response.UserResponse;
import com.example.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author Tran Minh Truyen
 */

@Tag(name = "UserController")
@RestController(value = "UserController")
@CrossOrigin("*")
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private UserServices userServices;
    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))))
    @PostMapping(value = "sendConfirmKey")
    public BaseResponse sendConfirmKey(@RequestBody CheckEmailRequest checkEmailRequest) {
        BaseResponse baseResponse = new BaseResponse();
        if (checkEmailRequest != null && !userServices.emailIsExists(checkEmailRequest.getEmail())){
            try {
                String key = RandomStringUtils.randomAlphanumeric(10).toUpperCase();
                userServices.sendEmailConfirmKey(checkEmailRequest.getEmail(), key);
                baseResponse.setStatusCode(HttpStatus.OK.value());
                baseResponse.setStatusName(HttpStatus.OK.name());
                baseResponse.setMessage("Email has been sent");
                return baseResponse;
            } catch (Exception exception) {
                baseResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
                baseResponse.setStatusName(HttpStatus.FORBIDDEN.name());
                baseResponse.setMessage(exception.getMessage());
                return baseResponse;
            }
        }
        else {
            baseResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            baseResponse.setStatusName(HttpStatus.NOT_FOUND.name());
            baseResponse.setMessage("Email invalid");
            return baseResponse;
        }
    }

    @Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
            security = {@SecurityRequirement(name = "Authorization")})
    @PostMapping(value = "checkLoginStatus")
    public BaseResponse checkLoginStatus(HttpServletRequest request){
        BaseResponse baseResponse = new BaseResponse();
        String header = request.getHeader("Authorization");
        String token = null;
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            token = header.substring(7);
        }
        if (jwtTokenProvider.validateToken(token)) {
            baseResponse.setStatusCode(HttpStatus.OK.value());
            baseResponse.setStatusName(HttpStatus.OK.name());
            baseResponse.setMessage("Token valid");
            return baseResponse;
        } else {
            baseResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            baseResponse.setStatusName(HttpStatus.UNAUTHORIZED.name());
            baseResponse.setMessage("Token invalid");
            return baseResponse;
        }
    }

    @Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
            summary = "This is API to create user, a confirm key will be sent to email to activate user")
    @PostMapping(value = "createUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse createUser(@RequestBody UserRequest userRequest, @RequestParam String confirmKey) {
        BaseResponse baseResponse = new BaseResponse();
        if (confirmKey != null && userServices.checkConfirmKey(userRequest.getEmail(), confirmKey)) {
            try {
                UserResponse userResponse = userServices.createUser(userRequest);
                baseResponse.setStatusCode(HttpStatus.OK.value());
                baseResponse.setStatusName(HttpStatus.OK.name());
                baseResponse.setMessage("User is added");
                baseResponse.setPayload(userResponse);
                return baseResponse;
            } catch (Exception exception) {
                baseResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
                baseResponse.setStatusName(HttpStatus.FORBIDDEN.name());
                baseResponse.setMessage(exception.getMessage());
                return baseResponse;
            }
        } else {
            baseResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
            baseResponse.setStatusName(HttpStatus.FORBIDDEN.name());
            baseResponse.setMessage("Confirm key invalid");
            return baseResponse;
        }
    }

    @Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))))
    @PostMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse login(@RequestBody LoginRequest loginRequest) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            User user = userServices.login(loginRequest);
            CustomUserDetail customUserDetail = new CustomUserDetail(user);
            String jwt = jwtTokenProvider.generateToken(customUserDetail);
            JwtResponse jwtResponse = new JwtResponse(jwt);
            baseResponse.setStatusCode(HttpStatus.OK.value());
            baseResponse.setStatusName(HttpStatus.OK.name());
            baseResponse.setMessage("Login valid");
            baseResponse.setPayload(jwtResponse);
            return baseResponse;
        } catch (Exception exception) {
            baseResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
            baseResponse.setStatusName(HttpStatus.FORBIDDEN.name());
            baseResponse.setMessage(exception.getMessage());
            return baseResponse;
        }
    }

    @Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
            summary = "This is API to reset password, a new password will be sent to email")
    @PostMapping(value = "resetPassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse resetPassword(@Valid @RequestBody ResetPassword resetPassword) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            User user = userServices.resetPassword(resetPassword.getEmail());
            baseResponse.setStatusCode(HttpStatus.OK.value());
            baseResponse.setStatusName(HttpStatus.OK.name());
            baseResponse.setMessage("Email has been sent");
            baseResponse.setPayload(user);
            return baseResponse;
        } catch (Exception exception) {
            baseResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
            baseResponse.setStatusName(HttpStatus.FORBIDDEN.name());
            baseResponse.setMessage(exception.getMessage());
            return baseResponse;
        }
    }

    @Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))))
    @GetMapping(value="getAllUser")
    public BaseResponse getAllUser(@RequestParam int page, @RequestParam int size){
        BaseResponse baseResponse = new BaseResponse();
        try {
            CommonResponse response = userServices.getAllUser(page, size);
            baseResponse.setStatusCode(HttpStatus.OK.value());
            baseResponse.setStatusName(HttpStatus.OK.name());
            baseResponse.setMessage("Get users success");
            baseResponse.setPayload(response);
            return baseResponse;
        } catch (Exception exception) {
            baseResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            baseResponse.setStatusName(HttpStatus.NOT_FOUND.name());
            baseResponse.setMessage(exception.getMessage());
            return baseResponse;
        }
    }

    @Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))))
    @GetMapping(value="getUserByKeyword")
    public BaseResponse getUserByKeyword(@RequestParam int page,
                                             @RequestParam int size,
                                             @RequestParam(required = false) String keyword){
        BaseResponse baseResponse = new BaseResponse();
        try {
            CommonResponse response = userServices.getUserByKeyWord(page, size, keyword);
            baseResponse.setStatusCode(HttpStatus.OK.value());
            baseResponse.setStatusName(HttpStatus.OK.name());
            baseResponse.setMessage("Get users success");
            baseResponse.setPayload(response);
            return baseResponse;
        } catch (Exception exception) {
            baseResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            baseResponse.setStatusName(HttpStatus.NOT_FOUND.name());
            baseResponse.setMessage(exception.getMessage());
            return baseResponse;
        }
    }

    @Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
            security = {@SecurityRequirement(name = "Authorization")},
            summary = "This is API to update user infomation, user must login first to use this API")
    @PutMapping(value = "updateUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse updateUser(@RequestBody UserRequest userRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        BaseResponse baseResponse = new BaseResponse();
        try {
            UserResponse userResponse = userServices.updateUser(customUserDetail.getUser().getId(), userRequest);
            baseResponse.setStatusCode(HttpStatus.OK.value());
            baseResponse.setStatusName(HttpStatus.OK.name());
            baseResponse.setMessage("User is update");
            baseResponse.setPayload(userResponse);
            return baseResponse;
        } catch (Exception exception) {
            baseResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
            baseResponse.setStatusName(HttpStatus.FORBIDDEN.name());
            baseResponse.setMessage(exception.getMessage());
            return baseResponse;
        }
    }

    @Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
            security = {@SecurityRequirement(name = "Authorization")})
    @DeleteMapping(value = "deleteUser")
    public BaseResponse deleteUser(@RequestParam int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        BaseResponse baseResponse = new BaseResponse();
        if (authentication != null && (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER")) ||
                authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")))
        )
        {
            try {
                userServices.deleteUser(id);
                baseResponse.setStatusCode(HttpStatus.OK.value());
                baseResponse.setStatusName(HttpStatus.OK.name());
                baseResponse.setMessage("user is delete");
                return baseResponse;
            } catch (Exception exception) {
                baseResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
                baseResponse.setStatusName(HttpStatus.FORBIDDEN.name());
                baseResponse.setMessage(exception.getMessage());
                return baseResponse;
            }
        }
        else{
            if (authentication == null) {
                baseResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                baseResponse.setStatusName(HttpStatus.UNAUTHORIZED.name());
                baseResponse.setMessage("Please login");
                return baseResponse;
            }
            else {
                baseResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                baseResponse.setStatusName(HttpStatus.UNAUTHORIZED.name());
                baseResponse.setMessage("You don't have permission");
                return baseResponse;
            }
        }
    }

    @Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
            security = {@SecurityRequirement(name = "Authorization")},
            summary = "This is API to get user profile, user must login first to use this API")
    @PostMapping(value = "getProfileUser")
    public BaseResponse getProfileUser() {
        BaseResponse baseResponse = new BaseResponse();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        try {
            UserResponse userResponse = userServices.getProfileUser(customUserDetail.getUser().getId());
            baseResponse.setStatusCode(HttpStatus.OK.value());
            baseResponse.setStatusName(HttpStatus.OK.name());
            baseResponse.setMessage("Get profile success");
            baseResponse.setPayload(userResponse);
            return baseResponse;
        } catch (Exception exception) {
            baseResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
            baseResponse.setStatusName(HttpStatus.FORBIDDEN.name());
            baseResponse.setMessage(exception.getMessage());
            return baseResponse;
        }
    }
}

package com.example.controller;
import com.example.common.request.ProductRequest;
import com.example.common.response.BaseResponse;
import com.example.common.response.CommonResponse;
import com.example.common.response.ProductResponse;
import com.example.common.utils.Utils;
import com.example.services.ProductServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */

@Tag(name = "ProductController")
@RestController(value = "ProductController")
@CrossOrigin("*")
@RequestMapping("api/product")
public class ProductController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductServices productServices;

	@Autowired
	private Utils utils;

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@PostMapping(value = "createProduct", consumes = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse createProduct(@RequestBody ProductRequest productRequest){
		BaseResponse baseResponse = new BaseResponse();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && (utils.checkRole(authentication,"ADMIN") || utils.checkRole(authentication,"EMP"))) {
			try	{
				ProductResponse productResponse = productServices.createProduct(productRequest);
				baseResponse.setStatusCode(HttpStatus.OK.value());
				baseResponse.setStatusName(HttpStatus.OK.name());
				baseResponse.setMessage("Create product successful");
				baseResponse.setPayload(productResponse);
				return baseResponse;
			} catch (Exception exception) {
				baseResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
				baseResponse.setStatusName(HttpStatus.FORBIDDEN.name());
				baseResponse.setMessage(exception.getMessage());
				LOGGER.error(exception.getMessage());
				return baseResponse;
			}
		}
		else{
			baseResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());
			baseResponse.setStatusName(HttpStatus.UNAUTHORIZED.name());
			baseResponse.setMessage("You don't have permission");
			return baseResponse;
		}
	}


	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))))
	@GetMapping(value="getProductByKeyword")
	public BaseResponse getProductByKeyword(@RequestParam int page,
												@RequestParam int size,
												@RequestParam(required = false) String name,
												@RequestParam(required = false) String brand,
												@RequestParam(required = false) String category,
												@RequestParam(required = false, defaultValue = "0") float fromPrice,
												@RequestParam(required = false, defaultValue = "0") float toPrice)
	{
		BaseResponse baseResponse = new BaseResponse();
		try {
			CommonResponse commonResponse = productServices.getProductByKeyWord(page, size, name, brand,
					category, fromPrice, toPrice);
			baseResponse.setStatusCode(HttpStatus.OK.value());
			baseResponse.setStatusName(HttpStatus.OK.name());
			baseResponse.setMessage("Get product by keyword successful");
			baseResponse.setPayload(commonResponse);
			return baseResponse;
		} catch (Exception exception) {
			baseResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
			baseResponse.setStatusName(HttpStatus.FORBIDDEN.name());
			baseResponse.setMessage(exception.getMessage());
			return baseResponse;
		}
	}


	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))))
	@GetMapping(value="getAllProduct")
	public BaseResponse getAllProduct(@RequestParam int page,
										  @RequestParam int size){
		BaseResponse baseResponse = new BaseResponse();
		try {
			CommonResponse commonResponse = productServices.getAllProduct(page, size);
			baseResponse.setStatusCode(HttpStatus.OK.value());
			baseResponse.setStatusName(HttpStatus.OK.name());
			baseResponse.setMessage("Get all product successful");
			baseResponse.setPayload(commonResponse);
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
	@PutMapping(value = "updateProduct", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?>updateProduct(@RequestParam int id, @RequestBody ProductRequest productRequest) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && (utils.checkRole(authentication,"ADMIN") || utils.checkRole(authentication,"EMP")))
		{
			ProductResponse productResponse = productServices.updateProduct(id, productRequest);
			if (productResponse != null){
				return new ResponseEntity<>(productResponse, HttpStatus.OK);
			}
			else return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
		}
		else return new ResponseEntity<>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@DeleteMapping(value = "deleteImageOfProduct")
	public ResponseEntity<?>deleteImageOfProduct(@RequestParam int productId, @RequestParam List<Integer> imageId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null &&
				(
						authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")) ||
								authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("EMP"))
				)
		){
			if (productServices.deleteImageOfProduct(productId, imageId)){
				return new ResponseEntity<>("Image is deleted", HttpStatus.OK);
			}
			else return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
		}
		else return new ResponseEntity<>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
	}


	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@DeleteMapping(value = "deleteProduct")
	public ResponseEntity<?>deleteProduct(@RequestParam List<Integer> id){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))))
		{
			if (productServices.deleteProduct(id)){
				return new ResponseEntity<>("product is deleted", HttpStatus.OK);
			}
			else return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
		}
		else return new ResponseEntity<>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
	}
}

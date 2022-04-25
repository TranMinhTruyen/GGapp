package com.example.controller;
import com.example.common.jwt.CustomUserDetail;
import com.example.common.response.BaseResponse;
import com.example.common.response.CartResponse;
import com.example.services.CartServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tran Minh Truyen
 */

@Tag(name = "CartController")
@RestController(value = "CartController")
@CrossOrigin("*")
@RequestMapping("api/cart")
public class CartController {

	@Autowired
	private CartServices cartServices;

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@PostMapping(value = "createCartAndAddProductToCart")
	public ResponseEntity<?> createCartAndAddProductToCart(@RequestParam int productId,
														   @RequestParam(required = false, defaultValue = "1") int productAmount) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null &&
				(
				authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")) ||
						authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))
				)
		){
			CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
			if (!cartServices.isCartExists(customUserDetail.getUser().getId())) {
				if (cartServices.createCart(customUserDetail.getUser().getId(), productId, productAmount))
					return new ResponseEntity<>("Cart is created", HttpStatus.OK);
				else return new ResponseEntity<>("Error", HttpStatus.FORBIDDEN);
			}
			else {
				if (cartServices.addProductToCart(customUserDetail.getUser().getId(), productId, productAmount))
					return new ResponseEntity<>("Product is added", HttpStatus.OK);
				else return new ResponseEntity<>("Error", HttpStatus.FORBIDDEN);
			}
		}
		else return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@GetMapping(value="getCartById")
	public ResponseEntity<?>getCartById(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")) ||
								authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))
		){
			CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
			CartResponse cartResponse = cartServices.getCartById(customUserDetail.getUser().getId());
			BaseResponse baseResponse = new BaseResponse();
			if (cartResponse != null){
				baseResponse.setStatusCode(200);
				baseResponse.setMessage("Get cart success");
				baseResponse.setPayload(cartResponse);
				return new ResponseEntity<>(baseResponse, HttpStatus.OK);
			}
			else {
				baseResponse.setStatusCode(404);
				baseResponse.setMessage("Cart is not exist");
				baseResponse.setPayload(cartResponse);
				return new ResponseEntity<>(baseResponse, HttpStatus.OK);
			}
		}
		else return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@PutMapping(value = "updateProductAmount")
	public ResponseEntity<?>updateProductAmount(@RequestParam int productId,
												@RequestParam long amount) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null &&
				(
						authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")) ||
								authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))
				)
		){
			CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
			if (cartServices.updateProductAmount(customUserDetail.getUser().getId(), productId, amount))
				return new ResponseEntity<>("Product amount is updated", HttpStatus.OK);
			else return new ResponseEntity<>("Error", HttpStatus.NOT_FOUND);
		}
		else{
			return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
		}
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@DeleteMapping(value = "removeProductFromCart")
	public ResponseEntity<?>updateProductList(@RequestParam int productId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null &&
				(
						authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")) ||
								authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))
				)
		){
			CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
		 	if(cartServices.removeProductFromCart(customUserDetail.getUser().getId(), productId))
				return new ResponseEntity<>("Product is removed", HttpStatus.OK);
		 	else return new ResponseEntity<>("Error", HttpStatus.NOT_FOUND);
		}
		else{
			return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
		}
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@DeleteMapping(value = "deleteCart")
	public ResponseEntity<?>deleteCart(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null &&
				(
						authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")) ||
								authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))
				)
		){
			CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
			cartServices.deleteCart(customUserDetail.getUser().getId());
			return new ResponseEntity<>("Cart is deleted", HttpStatus.OK);
		}
		else
			return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
	}
}

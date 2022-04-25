package com.example.controller;
import com.example.common.jwt.CustomUserDetail;
import com.example.common.request.OrderRequest;
import com.example.common.request.UserOrderRequest;
import com.example.common.response.CommonResponse;
import com.example.common.response.OrderResponse;
import com.example.services.CartServices;
import com.example.services.OrderServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

@Tag(name = "OrderController")
@RestController(value = "OrderController")
@CrossOrigin("*")
@RequestMapping("api/order")
public class OrderController {

	@Autowired
	private OrderServices orderServices;

	@Autowired
	private CartServices cartServices;

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@PostMapping(value = "createOrder")
	public ResponseEntity<?> createOrder(@RequestBody UserOrderRequest userOrderRequest){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null &&
				(
						authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")) ||
								authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))
				)
		){
			CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
			if (userOrderRequest.getProductId() != null && userOrderRequest.getProductId().length > 0){
				OrderResponse orderResponse = orderServices.createOrderByProductId(customUserDetail.getUser().getId(),
						userOrderRequest.getProductId());
				if (orderResponse != null) {
					cartServices.deleteCartAfterCreateOrder(customUserDetail.getUser().getId());
					return new ResponseEntity<>(orderResponse, HttpStatus.OK);
				}
				else
					return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
			}
			else {
				if (!cartServices.isCartExists(customUserDetail.getUser().getId())){
					return new ResponseEntity<>("Not found cart", HttpStatus.NOT_FOUND);
				}
				else {
					OrderResponse orderResponse = orderServices.createOrderByCart(customUserDetail.getUser().getId());
					if (orderResponse != null) {
						cartServices.deleteCartAfterCreateOrder(customUserDetail.getUser().getId());
						return new ResponseEntity<>(orderResponse, HttpStatus.OK);
					}
					else
						return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
				}
			}
		}
		else
			return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
	}


	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@GetMapping(value="user/getOrder")
	public ResponseEntity<?>getOrder(@RequestParam int page, @RequestParam int size){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null &&
				(
						authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")) ||
								authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))
				)
		){
			CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
			CommonResponse commonResponse = orderServices.getOrderByCustomerId(page, size, customUserDetail.getUser().getId());
			if (commonResponse != null){
				return new ResponseEntity<>(commonResponse, HttpStatus.OK);
			}
			else return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
		}
		else
			return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
	}


	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@GetMapping(value="emp/getOrderByCustomerId")
	public ResponseEntity<?>getOrderByCustomerId(@RequestParam int page,
												 @RequestParam int size){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null &&
				(
						authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")) ||
								authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("EMP")) ||
								authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))
				)
		){
			CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
			CommonResponse commonResponse = orderServices.getOrderByCustomerId(page, size, customUserDetail.getUser().getId());
			if (commonResponse != null){
				return new ResponseEntity<>(commonResponse, HttpStatus.OK);
			}
			else return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
		}
		else
			return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
	}


	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@PutMapping(value = "user/userUpdateOrder", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?>userUpdateOrder(@RequestBody UserOrderRequest userOrderRequest) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null &&
				(
						authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")) ||
								authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))
				)
		){
			CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
			OrderRequest orderRequest = new OrderRequest();
			orderRequest.setAddress(userOrderRequest.getAndress());
			if (orderServices.updateOrder(customUserDetail.getUser().getId(), orderRequest)){
				return new ResponseEntity<>(orderRequest, HttpStatus.OK);
			}
			else return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
		}
		else
			return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
	}


	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@PutMapping(value = "emp/empUpdateOrder", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?>empUpdateOrder(@RequestParam int id, @RequestBody OrderRequest orderRequest) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null &&
				(
						authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")) ||
								authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("EMP"))
				)
		){
			CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
			orderRequest.setEmployeeId(customUserDetail.getUser().getId());
			if (orderServices.updateOrder(id, orderRequest)){
				return new ResponseEntity<>(orderRequest, HttpStatus.OK);
			}
			else return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
		}
		else
			return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
	}



	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@DeleteMapping(value = "deleteOrder")
	public ResponseEntity<?>deleteOrder(@RequestParam int id){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null &&
				(
						authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")) ||
								authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))
				)
		){
			CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
			if (orderServices.deleteOrder(id, customUserDetail.getUser().getId())){
				return new ResponseEntity<>("order is deleted", HttpStatus.OK);
			}
			else return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
		}
		else
			return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
	}
}

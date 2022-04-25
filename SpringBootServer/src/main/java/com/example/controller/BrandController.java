package com.example.controller;
import com.example.common.request.BrandRequest;
import com.example.common.request.ProductRequest;
import com.example.common.response.BrandResponse;
import com.example.common.response.CommonResponse;
import com.example.services.BrandServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController(value = "BrandController")
@Tag(name = "BrandController")
@CrossOrigin("*")
@RequestMapping("api/brand")
public class BrandController {

	@Autowired
	private BrandServices brandServices;

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@PostMapping(value = "createBrand", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createBrand(@RequestBody BrandRequest brandRequest){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")))
		){
			if (brandServices.isExists(brandRequest.getName()))
				return new ResponseEntity<>("Brand is exists", HttpStatus.BAD_REQUEST);
			if (brandServices.createBrand(brandRequest))
				return new ResponseEntity<>(brandRequest, HttpStatus.OK);
			else
				return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
		}
		else return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))))
	@GetMapping(value="getBrandByKeyword")
	public ResponseEntity<?> getBrandByKeyword(@RequestParam int page,
											   @RequestParam int size,
											   @RequestParam(required = false) String keyword){
		CommonResponse commonResponse = brandServices.getBrandbyKeyword(page, size, keyword);
		if (commonResponse != null){
			return new ResponseEntity<>(commonResponse, HttpStatus.OK);
		}
		else return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))))
	@GetMapping(value="getAllBrand")
	public ResponseEntity<?>getAllBrand(@RequestParam int page,
										@RequestParam int size){
		CommonResponse commonResponse = brandServices.getAllBrand(page, size);
		if (commonResponse != null){
			return new ResponseEntity<>(commonResponse, HttpStatus.OK);
		}
		else return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@PutMapping(value = "updateBrand", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?>updateBrand(@RequestParam int id, @RequestBody BrandRequest brandRequest) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null &&
				(
						authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")) ||
								authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("EMP"))
				)
		){
			BrandResponse brandResponse = brandServices.updateBrand(id, brandRequest);
			if (brandResponse != null){
				return new ResponseEntity<>(brandResponse, HttpStatus.OK);
			}
			else return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
		}
		else{
			if (authentication == null){
				return new ResponseEntity<>("Please login", HttpStatus.UNAUTHORIZED);
			}
			else
				return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
		}
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@DeleteMapping(value = "deleteBrand")
	public ResponseEntity<?>deleteBrand(@RequestParam int id){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null &&
				(
						authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")) ||
								authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("EMP"))
				)
		){
			if (brandServices.deleteBrand(id)){
				return new ResponseEntity<>("category is deleted", HttpStatus.OK);
			}
			else return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
		}
		else{
			if (authentication == null){
				return new ResponseEntity<>("Please login", HttpStatus.UNAUTHORIZED);
			}
			else
				return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
		}
	}
}

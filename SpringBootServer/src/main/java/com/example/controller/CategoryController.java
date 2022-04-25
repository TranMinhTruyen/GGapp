package com.example.controller;
import com.example.common.request.BrandRequest;
import com.example.common.request.CategoryRequest;
import com.example.common.response.BrandResponse;
import com.example.common.response.CategoryResponse;
import com.example.common.response.CommonResponse;
import com.example.services.CategoryServices;
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

@Tag(name = "CategoryController")
@RestController(value = "CategoryController")
@CrossOrigin("*")
@RequestMapping("api/category")
public class CategoryController {
	@Autowired
	private CategoryServices categoryServices;

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@PostMapping(value = "createCategory", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryRequest){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null &&
				(
						authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))
				)
		){
			if (categoryServices.isExists(categoryRequest.getName())){
				return new ResponseEntity<>("Category is exists", HttpStatus.UNAUTHORIZED);
			}
			if (categoryServices.createCategory(categoryRequest))
				return new ResponseEntity<>(categoryRequest, HttpStatus.OK);
			else
				return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
		}
		else return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))))
	@GetMapping(value="getCategoryByKeyword")
	public ResponseEntity<?> getCategoryByKeyword(@RequestParam int page,
												  @RequestParam int size,
												  @RequestParam(required = false) String keyword){
		CommonResponse commonResponse = categoryServices.getCategoryByKeyword(page, size, keyword);
		if (commonResponse != null){
			return new ResponseEntity<>(commonResponse, HttpStatus.OK);
		}
		else return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))))
	@GetMapping(value="getAllCategory")
	public ResponseEntity<?>getAllCategory(@RequestParam int page,
										   @RequestParam int size){
		CommonResponse commonResponse = categoryServices.getAllCategory(page, size);
		if (commonResponse != null){
			return new ResponseEntity<>(commonResponse, HttpStatus.OK);
		}
		else return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@PutMapping(value = "updateCategory", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?>updateCategory(@RequestParam int id, @RequestBody CategoryRequest categoryRequest) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))))
		{
			CategoryResponse categoryResponse = categoryServices.updateCategory(id, categoryRequest);
			if (categoryResponse != null){
				return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
			}
			else return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
		}
		else{
			return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
		}
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@DeleteMapping(value = "deleteCategory")
	public ResponseEntity<?>deleteBrand(@RequestParam int id){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))))
		{
			if (categoryServices.deleteCategory(id)){
				return new ResponseEntity<>("category is deleted", HttpStatus.OK);
			}
			else return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
		}
		else{
			return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
		}
	}
}

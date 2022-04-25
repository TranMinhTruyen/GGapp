package com.example.services.servicesImplement;
import com.example.common.entity.Category;
import com.example.common.entity.Product;
import com.example.common.request.CategoryRequest;
import com.example.common.response.CategoryResponse;
import com.example.common.response.CommonResponse;
import com.example.repository.mysql.CategoryRepository;
import com.example.repository.mysql.ProductRepository;
import com.example.repository.specification.CategorySpecification;
import com.example.services.CategoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServicesImp implements CategoryServices {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public boolean createCategory(CategoryRequest categoryRequest) {
		if (categoryRequest != null && !isExists(categoryRequest.getName())){
			Category newCategory = new Category();
			newCategory.setName(categoryRequest.getName());
			newCategory.setDescription(categoryRequest.getDescription());
			categoryRepository.save(newCategory);
			return true;
		}
		return false;
	}

	@Override
	public CommonResponse getAllCategory(int page, int size) {
		List<Category> result = categoryRepository.findAll();
		List<CategoryResponse> categoryList = new ArrayList<>();
		result.stream().forEach(item -> {
			CategoryResponse categoryResponse = new CategoryResponse();
			categoryResponse.setId(item.getId());
			categoryResponse.setName(item.getName());
			categoryResponse.setDescription(item.getDescription());
			categoryList.add(categoryResponse);
		});

		if (categoryList != null){
			return new CommonResponse().getCommonResponse(page, size, categoryList);
		}
		return null;
	}

	@Override
	public CommonResponse getCategoryByKeyword(int page, int size, String keyword) {
		List result = categoryRepository.findAll(new CategorySpecification(keyword));
		if (result != null){
			return new CommonResponse().getCommonResponse(page, size, result);
		}
		return getAllCategory(page, size);
	}

	@Override
	public CategoryResponse updateCategory(int id, CategoryRequest categoryRequest) {
		if (update(id, categoryRequest)){
			Optional<Category> category = categoryRepository.findById(id);
			Category result = category.get();
			CategoryResponse categoryResponse = new CategoryResponse();
			categoryResponse.setId(result.getId());
			categoryResponse.setName(result.getName());
			categoryResponse.setDescription(result.getDescription());
			return categoryResponse;
		}
		return null;
	}

	@Override
	public boolean deleteCategory(int id) {
		Optional<Category> category = categoryRepository.findById(id);
		if (category.isPresent()){
			List<Product> products = productRepository.findAllByCategoryId(id);
			if (products != null && !products.isEmpty()){
				products.stream().forEach(items -> {
					items.setCategory(null);
					productRepository.save(items);
				});
			}
			categoryRepository.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	public boolean isExists(String categoryName) {
		return !categoryRepository.findAll(new CategorySpecification(categoryName)).isEmpty();
	}

	private boolean update(int id, CategoryRequest categoryRequest){
		Optional<Category> category = categoryRepository.findById(id);
		if (categoryRequest != null && category.isPresent()){
			Category update = category.get();
			update.setName(categoryRequest.getName());
			update.setDescription(categoryRequest.getDescription());
			categoryRepository.save(update);
			return true;
		}
		return false;
	}
}

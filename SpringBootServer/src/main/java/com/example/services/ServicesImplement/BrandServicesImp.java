package com.example.services.servicesImplement;
import com.example.common.entity.Brand;
import com.example.common.entity.Product;
import com.example.common.request.BrandRequest;
import com.example.common.response.BrandResponse;
import com.example.common.response.CommonResponse;
import com.example.repository.mysql.BrandRepository;
import com.example.repository.mysql.ProductRepository;
import com.example.repository.specification.BrandSpecification;
import com.example.services.BrandServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BrandServicesImp implements BrandServices {

	@Autowired
	private BrandRepository brandRepository;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public boolean createBrand(BrandRequest brandRequest) {
		if (brandRequest != null && !isExists(brandRequest.getName())){
			Brand newBrand = new Brand();
			newBrand.setName(brandRequest.getName());
			newBrand.setDescription(brandRequest.getDescription());
			brandRepository.save(newBrand);
			return true;
		}
		return false;
	}

	@Override
	public CommonResponse getAllBrand(int page, int size) {
		List<Brand> result = brandRepository.findAll();
		if (!result.isEmpty() && result != null){
			return new CommonResponse().getCommonResponse(page, size, result);
		}
		else return null;
	}

	@Override
	public CommonResponse getBrandbyKeyword(int page, int size, String keyword) {
		List<Brand> result = brandRepository.findAll(new BrandSpecification(keyword));
		if (!result.isEmpty() && result != null){
			return new CommonResponse().getCommonResponse(page, size, result);
		}
		return getAllBrand(page, size);
	}

	@Override
	public BrandResponse updateBrand(int id, BrandRequest brandRequest) {
		if (update(id, brandRequest)){
			Optional<Brand> brand = brandRepository.findById(id);
			Brand result = brand.get();
			BrandResponse brandResponse = new BrandResponse();
			brandResponse.setId(result.getId());
			brandResponse.setName(result.getName());
			brandResponse.setDescription(result.getDescription());
			return brandResponse;
		}
		return null;
	}

	@Override
	public boolean deleteBrand(int id) {
		Optional<Brand> brand = brandRepository.findById(id);
		if (brand.isPresent()){
			List<Product> products = productRepository.findAllByBrandId(id);
			if (products != null && !products.isEmpty()){
				products.stream().forEach(items -> {
					items.setCategory(null);
					productRepository.save(items);
				});
			}
			brandRepository.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	public boolean isExists(String brandName) {
		return !brandRepository.findAll(new BrandSpecification(brandName)).isEmpty();
	}

	private boolean update (int id, BrandRequest brandRequest){
		Optional<Brand> brand = brandRepository.findById(id);
		if (brandRequest != null && brand.isPresent()){
			Brand update = brand.get();
			update.setName(brandRequest.getName());
			update.setDescription(brandRequest.getDescription());
			brandRepository.save(update);
			return true;
		}
		return false;
	}
}

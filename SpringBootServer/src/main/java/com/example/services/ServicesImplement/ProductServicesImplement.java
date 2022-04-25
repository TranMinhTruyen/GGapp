package com.example.services.servicesImplement;

import com.example.common.entity.Brand;
import com.example.common.entity.Category;
import com.example.common.entity.Product;
import com.example.common.model.Cart;
import com.example.common.model.Image;
import com.example.common.model.ListProduct;
import com.example.common.model.ProductImage;
import com.example.common.request.ProductImageRequest;
import com.example.common.request.ProductRequest;
import com.example.common.response.CommonResponse;
import com.example.common.response.ProductImageResponse;
import com.example.common.response.ProductResponse;
import com.example.repository.mongo.CartRepository;
import com.example.repository.mysql.BrandRepository;
import com.example.repository.mysql.CategoryRepository;
import com.example.repository.mongo.ProductImageRepository;
import com.example.repository.mysql.ProductRepository;
import com.example.repository.specification.ProductSpecification;
import com.example.services.ProductServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Tran Minh Truyen
 */

@Service
public class ProductServicesImplement implements ProductServices {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServicesImplement.class);

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BrandRepository brandRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductImageRepository productImageRepository;

	@Override
	public ProductResponse createProduct(ProductRequest productRequest) throws Exception {
		Optional<Brand> brand = brandRepository.findById(productRequest.getId_brand());
		Optional<Category> category = categoryRepository.findById(productRequest.getId_category());
		if (isExists(productRequest.getName())) {
			throw new Exception("Product is exists");
		}
		if (!brand.isPresent()) {
			throw new Exception("Brand not exists");
		}
		if (!category.isPresent()) {
			throw new Exception("Category not exists");
		}
		if (productRequest != null){
			Product newProduct = new Product();
			newProduct.setName(productRequest.getName());
			newProduct.setPrice(productRequest.getPrice());
			newProduct.setType(productRequest.getType());
			newProduct.setDiscount(productRequest.getDiscount());
			newProduct.setBrand(brand.get());
			newProduct.setCategory(category.get());
			newProduct.setInStock(productRequest.getUnitInStock());
			newProduct.setDayCreated(new Date());
			newProduct.setNew(true);
			Product result = productRepository.save(newProduct);
			if (result != null) {
				addOrUpdateProductImage(result.getId(), productRequest.getImage(), false);
				return getProductAfterUpdateOrCreate(result);
			} else throw new Exception("Error while create product");
		}
		else throw new Exception("Request is null");
	}

	private void addOrUpdateProductImage (int productId, List<ProductImageRequest> productImageList, boolean update) {
		if (!update && productImageList != null && productImageList.isEmpty()) {
			List<ProductImage> productImages = new ArrayList<>();
			ProductImage productImage = new ProductImage();
			productImage.setId(productId);
			List<Image> imageList = new ArrayList<>();
			for (ProductImageRequest i: productImageList){
				Image image = new Image();
				int imageIndex = productImageList.indexOf(i);
				image.setImageId(imageIndex + 1);
				image.setImage(productImageList.get(imageIndex).getImage());
				imageList.add(image);
			}
			productImage.setImages(imageList);
			productImageRepository.save(productImage);
		}
		if (update && productImageList != null && productImageList.isEmpty()) {
			Optional<ProductImage> productImage = productImageRepository.findById(productId);
			if (productImage.isPresent() && productImageList != null) {
				ProductImage result = productImage.get();
				List<Image> imageList = result.getImages();
				for (ProductImageRequest i: productImageList){
					Image image = new Image();
					if (imageList.isEmpty())
						image.setImageId(1);
					else {
						List<Image> imageIndex = imageList.stream().skip(imageList.size()-1).collect(Collectors.toList());
						image.setImageId(imageIndex.get(0).getImageId() + 1);
					}
					image.setImage(productImageList.get(productImageList.indexOf(i)).getImage());
					imageList.add(image);
				}
				result.setImages(imageList);
				productImageRepository.save(result);
			}
		}
	}

	private List<ProductImageResponse> getProductImage (int productId) {
		Optional<ProductImage> productImageList = productImageRepository.findById(productId);
		if (productImageList.isPresent()) {
			List<ProductImageResponse> productImageResponseList = new ArrayList<>();
			for (Image i: productImageList.get().getImages()){
				ProductImageResponse productImageResponse = new ProductImageResponse();
				productImageResponse.setImageId(i.getImageId());
				productImageResponse.setImage(i.getImage());
				productImageResponseList.add(productImageResponse);
			}
			return productImageResponseList;
		}
		else return null;
	}

	@Override
	public CommonResponse getAllProduct(int page, int size) {
		List<Product> productList = productRepository.findAll();
		List<ProductResponse> result = new ArrayList<>();
		productList.stream().forEach(items -> {
			ProductResponse productResponse = new ProductResponse();
			productResponse.setId(items.getId());
			productResponse.setName(items.getName());
			productResponse.setPrice(items.getPrice());
			productResponse.setType(items.getType());
			productResponse.setBrand(items.getBrand().getName());
			productResponse.setCategory(items.getCategory().getName());
			productResponse.setDiscount(items.getDiscount());
			productResponse.setUnitInStock(items.getInStock());
			productResponse.setNew(items.isNew());
			productResponse.setImage(getProductImage(items.getId()));
			result.add(productResponse);
		});
		if (!result.isEmpty()){
			return new CommonResponse().getCommonResponse(page, size, result);
		}
		else return null;
	}

	@Override
	public CommonResponse getProductByKeyWord(int page, int size, String name, String brand, String category, float price) {
		List<ProductResponse> productResponseList = filterProduct(name, brand, category, price);
		if (productResponseList != null){
			return new CommonResponse().getCommonResponse(page, size, productResponseList);
		}
		else return getAllProduct(page, size);
	}

	@Override
	public ProductResponse updateProduct(int id, ProductRequest productRequest) {
		Optional<Product> product = productRepository.findById(id);
		if (product.isPresent()){
			Product update = product.get();
			if (productRequest.getName() != null && !productRequest.getName().isEmpty())
				update.setName(productRequest.getName());

			if (productRequest.getType() != null && !productRequest.getType().isEmpty())
				update.setType(productRequest.getType());

			if (productRequest.getPrice() != 0)
				update.setPrice(productRequest.getPrice());;

			if (productRequest.getUnitInStock() != 0)
				update.setInStock(productRequest.getUnitInStock());

			if (productRequest.getImage() != null && !productRequest.getImage().isEmpty())
				addOrUpdateProductImage(update.getId(), productRequest.getImage(), true);
			update.setDiscount(productRequest.getDiscount());
			Product result = productRepository.save(update);
			updateProductFromCart(id, update);
			if (result != null)
				return getProductAfterUpdateOrCreate(result);
			else return null;
		}
		return null;
	}

	@Override
	public boolean deleteProduct(List<Integer> id) {
			List<Product> product = productRepository.findAllById(id);
			if (!product.isEmpty()){
				for (Integer i: id){
					deleteProductFromCart(i);
				}
				productRepository.deleteAllById(id);
				productImageRepository.deleteAllById(id);
				return true;
			}
		else return false;
	}

	public void updateProductFromCart(int productId, Product product){
		List<Cart> cartResult = cartRepository.findAll();
		float totalPrice = 0;
		for (Cart cart: cartResult){
			for (ListProduct listProduct: cart.getProductList()){
				if (listProduct.getId() == productId){
					listProduct.setProductName(product.getName());
					listProduct.setProductPrice(product.getPrice());
					listProduct.setDiscount(product.getDiscount());
					for (ListProduct items: cart.getProductList()){
						totalPrice += (items.getProductPrice()-(items.getProductPrice() * (items.getDiscount() / 100)))
								* items.getProductAmount();
					}
					cart.setTotalPrice(totalPrice);
					break;
				}
			}
		}
		cartRepository.saveAll(cartResult);
	}

	public void deleteProductFromCart(int productId){
		List<Cart> cartResult = cartRepository.findAll();
		float totalPrice = 0;

		for (Cart cart: cartResult){
			for (ListProduct listProduct: cart.getProductList()){
				if (listProduct.getId() == productId){
					cart.getProductList().remove(listProduct);
					for (ListProduct items: cart.getProductList()){
						totalPrice += (items.getProductPrice()-(items.getProductPrice() * (items.getDiscount() / 100)))
								* items.getProductAmount();
					}
					cart.setTotalPrice(totalPrice);
					break;
				}
			}
		}
		cartRepository.saveAll(cartResult);
		cartResult.stream().forEach(cart -> {
			if(cart.getProductList().isEmpty()){
				cartRepository.delete(cart);
			}
		});
	}

	@Override
	public boolean deleteImageOfProduct(int productId, List<Integer> imageId) {
		Optional<ProductImage> result = productImageRepository.findById(productId);
		if (result.isPresent()){
			ProductImage productImage = result.get();
			for (Integer i: imageId){
				for (Image image: productImage.getImages()) {
					if (image.getImageId() == i) {
						productImage.getImages().remove(image);
						break;
					}
				}
			}
			productImageRepository.save(productImage);
			return true;
		}
		return false;
	}

	@Override
	public boolean isExists(String name) {
		return !productRepository.findAll(new ProductSpecification(name)).isEmpty();
	}

	public ProductResponse getProductAfterUpdateOrCreate(Product product){
		ProductResponse response = new ProductResponse();
		response.setId(product.getId());
		response.setName(product.getName());
		response.setPrice(product.getPrice());
		response.setDiscount(product.getDiscount());
		response.setCategory(product.getCategory().getName());
		response.setBrand(product.getBrand().getName());
		response.setUnitInStock(product.getInStock());
		response.setType(product.getType());
		response.setDiscount(product.getDiscount());
		response.setImage(getProductImage(product.getId()));
		return response;
	}

	private List<ProductResponse> filterProduct(@Nullable String name,
												@Nullable String brand,
												@Nullable String category,
												float price){
		List<Product> productList = productRepository.findAll();
		List<Product> filter = new ArrayList();
		List<ProductResponse> productResponseList = new ArrayList();
		if (name != null && price == 0){
			productList.stream().forEach(items -> {
				if (items.getName().toLowerCase().contains(name.toLowerCase())){
					filter.add(items);
				}
			});
		}

		if (category == null && brand != null && price == 0){
			productList.stream().forEach(items -> {
				if (items.getBrand().getName().toLowerCase().contains(brand.toLowerCase())){
					filter.add(items);
				}
			});
		}

		if (brand == null && category != null && price == 0){
			productList.stream().forEach(items -> {
				if (items.getCategory().getName().toLowerCase().contains(category.toLowerCase())){
					filter.add(items);
				}
			});
		}

		if (price != 0 && brand == null && category == null){
			productList.stream().forEach(items -> {
				if (items.getPrice() <= price){
					filter.add(items);
				}
			});
		}

		if (price != 0 && brand != null && category == null){
			productList.stream().forEach(items -> {
				if (items.getPrice() <= price && items.getBrand().getName().toLowerCase().contains(brand.toLowerCase())){
					filter.add(items);
				}
			});
		}

		if (price != 0 && brand == null && category != null){
			productList.stream().forEach(items -> {
				if (items.getPrice() <= price && items.getCategory().getName().toLowerCase().contains(category.toLowerCase())){
					filter.add(items);
				}
			});
		}

		if (price != 0 && brand != null && category != null){
			productList.stream().forEach(items -> {
				if (items.getPrice() <= price && items.getBrand().getName().toLowerCase().contains(brand.toLowerCase())
						&& items.getCategory().getName().toLowerCase().contains(category.toLowerCase())){
					filter.add(items);
				}
			});
		}

		if (brand != null && category != null && price == 0){
			productList.stream().forEach(items -> {
				if (items.getBrand().getName().toLowerCase().equals(brand.toLowerCase()) &&
						items.getCategory().getName().toLowerCase().equals(category.toLowerCase())){
					filter.add(items);
				}
			});
		}
		filter.stream().forEach(items -> {
			ProductResponse productResponse = new ProductResponse();
			productResponse.setId(items.getId());
			productResponse.setName(items.getName());
			productResponse.setPrice(items.getPrice());
			productResponse.setType(items.getType());
			productResponse.setUnitInStock(items.getInStock());
			productResponse.setBrand(items.getBrand().getName());
			productResponse.setCategory(items.getCategory().getName());
			productResponse.setDiscount(items.getDiscount());
			productResponse.setImage(getProductImage(productResponse.getId()));
			productResponseList.add(productResponse);
		});
		if (!productResponseList.isEmpty()){
			return productResponseList;
		}
		else
			return null;
	}
}

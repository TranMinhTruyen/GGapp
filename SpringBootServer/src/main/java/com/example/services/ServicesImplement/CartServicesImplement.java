package com.example.services.servicesImplement;
import com.example.common.entity.Product;
import com.example.common.model.Cart;
import com.example.common.model.ListProduct;
import com.example.common.model.User;
import com.example.common.response.CartResponse;
import com.example.repository.mongo.CartRepository;
import com.example.repository.mongo.UserRepository;
import com.example.repository.mysql.ProductRepository;
import com.example.services.CartServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Tran Minh Truyen
 */
@Service
public class CartServicesImplement implements CartServices {
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean createCart(int customerId, int productId, int productAmount){
		Optional<Product> productResult = productRepository.findById(productId);
		Optional<User> userResult = userRepository.findById(customerId);
		if (productResult.isPresent() && userResult.isPresent() && productResult.get().getInStock() > 0){
			Cart newCart = new Cart();
			List<ListProduct> listProducts = new ArrayList<>();
			Product product = productResult.get();
			listProducts.add(new ListProduct(product.getId(), product.getName(), product.getPrice(),
					product.getDiscount(), productAmount > 1 ? productAmount : 1));
			product.setInStock(productAmount > 1 ? (product.getInStock() - productAmount) : (product.getInStock() - 1));
			productRepository.save(product);
			newCart.setId(userResult.get().getId());
			newCart.setProductList(listProducts);
			newCart.setTotalPrice((product.getPrice()-(product.getPrice()*(product.getDiscount()/100))) * productAmount);
			cartRepository.save(newCart);
			return true;
		}
		else return false;
	}

	@Override
	public CartResponse getCartById(int id){
		Optional<Cart> cartResult = cartRepository.findById(id);
		if (cartResult.isPresent()){
			CartResponse cartResponse = new CartResponse();
			cartResponse.setCustomerId(cartResult.get().getId());
			cartResponse.setProductList(cartResult.get().getProductList());
			cartResponse.setTotalPrice(cartResult.get().getTotalPrice());
			return cartResponse;
		}
		else return null;
	}

	@Override
	public boolean updateProductAmount(int customerId, int productId, long amount) {
		Optional<Cart> cartResult = cartRepository.findById(customerId);
		Optional<User> userResult = userRepository.findById(customerId);
		Optional<Product> productResult = productRepository.findById(productId);
		if (cartResult.isPresent() && userResult.isPresent()){
			float totalPrice = 0;
			Cart update = cartResult.get();
			List<ListProduct> list = update.getProductList();
			for (ListProduct items: list){
				if (items.getId() == productId){
					if (items.getProductAmount() > amount){
						productResult.get().setInStock(productResult.get().getInStock() + (items.getProductAmount() - amount));
						productRepository.save(productResult.get());
					}
					else {
						productResult.get().setInStock(productResult.get().getInStock() - (amount - items.getProductAmount()));
						productRepository.save(productResult.get());
					}
					items.setProductAmount(amount);
					break;
				}
			}
			for (ListProduct items: update.getProductList()){
				totalPrice += (items.getProductPrice()-(items.getProductPrice() * (items.getDiscount() / 100)))
						* items.getProductAmount();
			}
			update.setProductList(list);
			update.setTotalPrice(totalPrice);
			cartRepository.save(update);
			return true;
		}
		return false;
	}

	@Override
	public boolean addProductToCart(int customerId, int productId, int productAmount) {
		Optional<Cart> cartResult = cartRepository.findById(customerId);
		Optional<Product> productResult = productRepository.findById(productId);
		Optional<User> userResult = userRepository.findById(customerId);
		float totalPrice = 0;
		if (cartResult.isPresent() && productResult.isPresent() && userResult.isPresent()){
			Cart cart = cartResult.get();
			Product product = productResult.get();
			List<ListProduct> productInCart = cart.getProductList();

			if (!productInCart.stream().anyMatch(a -> a.getId() == product.getId())){
				productInCart.add(new ListProduct(product.getId(), product.getName(), product.getPrice(),
						product.getDiscount(), productAmount > 0 ? productAmount : 1));
				for (ListProduct items: productInCart){
					totalPrice += (items.getProductPrice()-(items.getProductPrice() * (items.getDiscount() / 100)))
							* items.getProductAmount();
				}
				product.setInStock(product.getInStock() - 1);
				productRepository.save(product);
				cart.setId(userResult.get().getId());
				cart.setProductList(productInCart);
				cart.setTotalPrice(totalPrice);
				cartRepository.save(cart);
				return true;
			}
			else {
				long amount = 0;
				for (ListProduct items: productInCart){
					if (items.getId() == productResult.get().getId()){
						amount = items.getProductAmount() + productAmount;
						break;
					}
				}
				return updateProductAmount(customerId, productId, amount);
			}
		}
		else return false;
	}

	@Override
	public boolean removeProductFromCart(int customerId, int productId) {
		Optional<Cart> cartResult = cartRepository.findById(customerId);
		Optional<Product> productResult = productRepository.findById(productId);
		if (cartResult.isPresent() && !cartResult.get().getProductList().isEmpty() &&
				cartResult.get().getProductList().stream().anyMatch(a -> a.getId() == productResult.get().getId())){
			float totalPrice = 0;
			Cart update = cartResult.get();
			List<ListProduct> list = update.getProductList();
			for (ListProduct items: list){
				if (items.getId() == productResult.get().getId()){
					productResult.get().setInStock(productResult.get().getInStock() + items.getProductAmount());
					productRepository.save(productResult.get());
					list.remove(items);
					break;
				}
			}
			for (ListProduct items: update.getProductList()){
				totalPrice += (items.getProductPrice()-(items.getProductPrice() * (items.getDiscount() / 100))) * items.getProductAmount();
			}
			update.setTotalPrice(totalPrice);
			update.setProductList(list);
			cartRepository.save(update);
			if (cartResult.get().getProductList().isEmpty()){
				return deleteCart(customerId);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean isCartExists(int customerId) {
		return cartRepository.findById(customerId).isPresent();
	}

	@Override
	public boolean deleteCart(int id){
		Optional<Cart> cartResult = cartRepository.findById(id);
		if (cartResult.isPresent()){
			if (!cartResult.get().getProductList().isEmpty()){
				cartResult.get().getProductList().stream().forEach(items ->{
					returnProductFromCart(items.getId(), items.getProductAmount());
				});
				cartRepository.deleteById(id);
				return true;
			}
			cartRepository.deleteById(id);
			return true;
		}
		else return false;
	}

	private void returnProductFromCart(int productId, float amount){
		Optional<Product> update = productRepository.findById(productId);
		update.get().setInStock(update.get().getInStock() + amount);
		productRepository.save(update.get());
	}

	@Override
	public boolean deleteCartAfterCreateOrder(int id) {
		Optional<Cart> cartResult = cartRepository.findById(id);
		if (cartResult.isPresent()){
			cartRepository.deleteById(id);
			return true;
		}
		return false;
	}
}

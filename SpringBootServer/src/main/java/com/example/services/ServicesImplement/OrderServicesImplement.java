package com.example.services.servicesImplement;

import com.example.common.model.AutoIncrement;
import com.example.common.model.Cart;
import com.example.common.model.ListProduct;
import com.example.common.model.Order;
import com.example.common.model.User;
import com.example.common.request.OrderRequest;
import com.example.common.response.CommonResponse;
import com.example.common.response.OrderResponse;
import com.example.repository.mongo.CartRepository;
import com.example.repository.mongo.OrderRepository;
import com.example.repository.mongo.UserRepository;
import com.example.services.OrderServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Tran Minh Truyen
 */
@Service
public class OrderServicesImplement implements OrderServices {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public OrderResponse createOrderByCart(int customerId) {
		List<Order> last  = new AutoIncrement(orderRepository).getLastOfCollection();
		Optional<Cart> cartResult = cartRepository.findById(customerId);
		Optional<User> userResult = userRepository.findById(customerId);
		if (userResult.isPresent() && cartResult.isPresent()) {
			Order newOrder = new Order();
			if (last != null)
				newOrder.setId(last.get(0).getId()+1);
			else newOrder.setId(1);
			newOrder.setCustomerId(userResult.get().getId());
			newOrder.setCreateDate(new Date());
			newOrder.setListProducts(cartResult.get().getProductList());
			newOrder.setStatus("Processing");
			newOrder.setTotalPrice(cartResult.get().getTotalPrice());
			newOrder.setAndress(userResult.get().getAddress().toString());
			Order order = orderRepository.save(newOrder);
			return getOrderAfterCreateOrUpdate(order);
		}
		else return null;
	}

	@Override
	public OrderResponse createOrderByProductId(int customerId, int[] productId) {
		List<Order> last  = new AutoIncrement(orderRepository).getLastOfCollection();
		Optional<Cart> cartResult = cartRepository.findById(customerId);
		Optional<User> userResult = userRepository.findById(customerId);
		if (userResult.isPresent() && cartResult.isPresent() && productId != null) {
			Order newOrder = new Order();
			List<ListProduct> productList = new ArrayList();
			if (last != null)
				newOrder.setId(last.get(0).getId() + 1);
			else newOrder.setId(1);
			newOrder.setCustomerId(userResult.get().getId());
			newOrder.setCreateDate(new Date());
			cartResult.get().getProductList().stream().forEach(i -> {
				for (int id: productId) {
					if (i.getId() == id) {
						productList.add(i);
					}
				}
			});
			newOrder.setListProducts(productList);
			newOrder.setStatus("Processing");
			newOrder.setTotalPrice(cartResult.get().getTotalPrice());
			newOrder.setAndress(userResult.get().getAddress().toString());
			Order order = orderRepository.save(newOrder);
			return getOrderAfterCreateOrUpdate(order);
		}
		else return null;
	}


	private OrderResponse getOrderAfterCreateOrUpdate(Order order){
		OrderResponse orderResponse = new OrderResponse();
		orderResponse.setId(order.getId());
		orderResponse.setCustomerId(order.getCustomerId());
		orderResponse.setEmployeeId(order.getEmployeeId());
		orderResponse.setCreateDate(order.getCreateDate());
		orderResponse.setListProducts(order.getListProducts());
		orderResponse.setTotalPrice(order.getTotalPrice());
		orderResponse.setStatus(order.getStatus());
		orderResponse.setAndress(order.getAndress());
		return orderResponse;
	}

	@Override
	public CommonResponse getOrderByCustomerId(int page, int size, int id) {
		List<Order> result = orderRepository.findOrderByCustomerId(id);
		if (result != null){
			return new CommonResponse().getCommonResponse(page, size, result);
		}
		else return null;
	}

	@Override
	@Transactional
	public boolean updateOrder(int id, OrderRequest orderRequest) {
		Optional<Order> order = orderRepository.findById(id);
		if(order.isPresent()){
			Order update = order.get();
			update.setEmployeeId(orderRequest.getEmployeeId());
			update.setStatus(orderRequest.getStatus());
			update.setAndress(orderRequest.getAddress());
			orderRepository.save(update);
			return true;
		}
		else return false;
	}

	@Override
	public boolean deleteOrder(int id, int customerId) {
		Order order = orderRepository.findOrderByIdAndCustomerId(id, customerId);
		if (order != null){
			orderRepository.deleteById(id);
			return true;
		}
		else return false;
	}
}

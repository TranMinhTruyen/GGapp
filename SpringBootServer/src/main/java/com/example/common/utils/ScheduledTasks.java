package com.example.common.utils;

import com.example.common.entity.Product;
import com.example.common.model.ConfirmKey;
import com.example.repository.mongo.ConfirmKeyRepository;
import com.example.repository.mysql.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
public class ScheduledTasks {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);

	@Autowired
	ConfirmKeyRepository confirmKeyRepository;

	@Autowired
	ProductRepository productRepository;

	@Scheduled(fixedRate = 300000)
	private void clearConfirmKey() {
		LOGGER.info("Start clear confirmkey");
		Date now = new Date();
		List<ConfirmKey> getAllConfirmKey = confirmKeyRepository.findAll();
		for (ConfirmKey key: getAllConfirmKey) {
			if (key.getExpire().before(now)){
				confirmKeyRepository.deleteByEmail(key.getEmail());
				LOGGER.info("Confirmkey is deleted: ", key.getKey());
			}
		}
		getAllConfirmKey.clear();
	}

	@Scheduled(cron = "0 0 23 * * ?")
	private void checkProductIsNew() {
		LOGGER.info("Start check product is new");
		Date now = new Date();
		List<Product> getAllProduct = productRepository.findAllByNewIsTrue();
		for (Product product: getAllProduct) {
			int checkYear = product.getDayCreated().getYear();
			int checkMonth = product.getDayCreated().getMonth();
			if (now.getYear() == checkYear && now.getMonth() - checkMonth > 1) {
				product.setNew(false);
				productRepository.save(product);
			}
		}
	}
}

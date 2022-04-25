package com.example.services.servicesImplement;

import com.example.common.model.AutoIncrement;
import com.example.common.model.News;
import com.example.common.model.User;
import com.example.common.request.NewsRequest;
import com.example.common.response.BaseResponse;
import com.example.common.response.CommonResponse;
import com.example.common.response.NewsResponse;
import com.example.repository.mongo.NewsRepository;
import com.example.repository.mongo.UserRepository;
import com.example.services.NewsServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NewsServicesImp implements NewsServices {

	private static final Logger LOGGER = LoggerFactory.getLogger(NewsServicesImp.class);

	@Autowired
	private NewsRepository newsRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public NewsResponse createNews(int userCreateId, NewsRequest newsRequest) throws Exception {
		Optional<User> user = userRepository.findById(userCreateId);
		List<User> last = new AutoIncrement(newsRepository).getLastOfCollection();
		if (user.isPresent() && newsRequest != null) {
			News news = new News();
			if (last != null)
				news.setId(last.get(0).getId() + 1);
			else news.setId(1);
			news.setUserCreateId(user.get().getId());
			news.setUserCreateName(user.get().getFirstName() + user.get().getLastName());
			news.setCreateDate(new Date());
			news.setNewsTitle(newsRequest.getTitle());
			news.setContent(newsRequest.getContent());
			News result = newsRepository.save(news);
			if (result != null) {
				return getNewsAfterCreateOrUpdate(result);
			} else {
				throw new Exception("Error while created news");
			}
		} else {
			throw new Exception("Not found userId");
		}
	}


	@Override
	public CommonResponse getAllNews (int page, int size) throws Exception {
		List result = newsRepository.findAll();
		if (result != null){
			return new CommonResponse().getCommonResponse(page, size, result);
		} else throw new Exception("Not found");
	}

	@Override
	public CommonResponse getNewsByKeyWord(int page, int size, int userCreateId, String userCreateName, String title) throws Exception {
		List<NewsResponse> newsResponseList = filterNews(userCreateId, userCreateName, title);
		if (!newsResponseList.isEmpty() && newsResponseList != null) {
			return new CommonResponse().getCommonResponse(page, size, newsResponseList);
		}
		else return getAllNews(page, size);
	}


	@Override
	public NewsResponse updateNews(int newsId, NewsRequest newsRequest) throws Exception {
		Optional<News> news = newsRepository.findById(newsId);
		if (news.isPresent()){
			News update = news.get();
			if (newsRequest.getTitle() != null || !newsRequest.getTitle().isEmpty()){
				update.setNewsTitle(newsRequest.getTitle());
			}
			if (newsRequest.getContent() != null || !newsRequest.getContent().isEmpty()){
				update.setContent(newsRequest.getContent());
			}
			News result = newsRepository.save(update);
			if (result != null) {
				return getNewsAfterCreateOrUpdate(result);
			} else throw new Exception("Error while update news");
		} else throw new Exception("Not found newsId");
	}

	@Override
	public BaseResponse deleteNews(int newsId) throws Exception {
		Optional<News> news = newsRepository.findById(newsId);
		BaseResponse baseResponse = new BaseResponse();
		if (news.isPresent()) {
			newsRepository.deleteById(newsId);
			baseResponse.setStatusCode(200);
			baseResponse.setMessage("News is deleted");
			baseResponse.setPayload(news);
			return baseResponse;
		} else throw new Exception("Not found newsId");
	}

	private NewsResponse getNewsAfterCreateOrUpdate (News news) {
		NewsResponse newsResponse = new NewsResponse();
		newsResponse.setId(news.getId());
		newsResponse.setUserCreateId(news.getUserCreateId());
		newsResponse.setUserCreateName(news.getUserCreateName());
		newsResponse.setCreateDate(news.getCreateDate());
		newsResponse.setTitle(news.getNewsTitle());
		newsResponse.setContent(news.getContent());
		return newsResponse;
	}

	private List<NewsResponse> filterNews (@Nullable int userCreateId,
										   @Nullable String userCreateName,
										   @Nullable String title) throws Exception {
		List<News> newsList = newsRepository.findAll();
		Optional<User> user = userRepository.findById(userCreateId);
		List<News> filter = new ArrayList<>();
		List<NewsResponse> newsResponseList = new ArrayList<>();
		if (userCreateName != null) {
			newsList.stream().forEach(item -> {
				if (item.getUserCreateName().equalsIgnoreCase(userCreateName)){
					filter.add(item);
				}
			});
		}

		if (user.isPresent()) {
			newsList.stream().forEach(item -> {
				if (item.getId() == user.get().getId()){
					filter.add(item);
				}
			});
		}

		if (title != null) {
			newsList.stream().forEach(item -> {
				if (item.getNewsTitle().equalsIgnoreCase(title)){
					filter.add(item);
				}
			});
		}

		if (title != null && user.isPresent()) {
			newsList.stream().forEach(item -> {
				if (item.getNewsTitle().equalsIgnoreCase(title) && item.getId() == user.get().getId()){
					filter.add(item);
				}
			});
		}

		if (title != null && userCreateName != null) {
			newsList.stream().forEach(item -> {
				if (item.getNewsTitle().equalsIgnoreCase(title) && item.getUserCreateName().equalsIgnoreCase(userCreateName)){
					filter.add(item);
				}
			});
		}

		if (user.isPresent() && userCreateName != null) {
			newsList.stream().forEach(item -> {
				if (item.getId() == user.get().getId() && item.getUserCreateName().equalsIgnoreCase(userCreateName)){
					filter.add(item);
				}
			});
		}

		filter.stream().forEach(item -> {
			NewsResponse newsResponse = new NewsResponse();
			newsResponse.setId(item.getId());
			newsResponse.setUserCreateId(item.getUserCreateId());
			newsResponse.setUserCreateName(item.getUserCreateName());
			newsResponse.setCreateDate(item.getCreateDate());
			newsResponse.setTitle(item.getNewsTitle());
			newsResponse.setContent(item.getContent());
			newsResponseList.add(newsResponse);
		});

		if (!newsResponseList.isEmpty()) {
			return newsResponseList;
		} else throw new Exception("Not found");
	}
}

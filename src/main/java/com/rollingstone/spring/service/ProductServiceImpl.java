package com.rollingstone.spring.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rollingstone.exceptions.HTTP400Exception;
import com.rollingstone.spring.dao.ProductDaoRepository;
import com.rollingstone.spring.model.Category;
import com.rollingstone.spring.model.Product;
import com.rollingstone.spring.service.feign.CategoryFeignInterface;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final static Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductDaoRepository productDao;

    @Autowired
    CategoryFeignInterface categoryClient;

    @Override
    @HystrixCommand(fallbackMethod = "saveWithoutValidation")
    public Product save(Product product) {

	Category category = null;
	Category parentCategory = null;

	if (product.getCategory() == null) {
	    logger.info("Product Category is null :");
	    throw new HTTP400Exception("Bad Request as Category Can not be empty");
	} else {
	    logger.info("Product Category is not null :" + product.getCategory());
	    logger.info("Product Category is not null ID :" + product.getCategory().getId());
	    try {
		category = categoryClient.getCategory(product.getCategory().getId());
	    }
	    catch(Exception e) {
		logger.info("Product Category Does not Exist :" + product.getCategory().getId());
		throw new HTTP400Exception("Bad Request as the Category Provided is an Invalid one");
	    }
	}
	if (product.getParentCategory() == null) {
	    logger.info("Product Parent Category is null :");
	    throw new HTTP400Exception("Bad Request as Parent Category Can not be empty");
	} else {
	    logger.info("Product Parent Category is not null :" + product.getParentCategory());
	    logger.info("Product Parent Category is not null Id :" + product.getParentCategory().getId());
	    try {
		category = categoryClient.getCategory(product.getParentCategory().getId());
	    }
	    catch(Exception e) {
		logger.info("Product Parent Category Does not Exist :" + product.getParentCategory().getId());
		throw new HTTP400Exception("Bad Request as the Parent Category Provided is an Invalid one");
	    }

	}

	return productDao.save(product);
    }

    @Override
    public Optional<Product> get(long id) {
        return productDao.findById(id);
    }

    @Override
    @HystrixCommand(fallbackMethod = "getProductsByPageFallback")
    public Page<Product> getProductsByPage(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("productCode").descending());
        return productDao.findAll(pageable);
    }

    @Override
    public void update(long id, Product product) {
        productDao.save(product);
    }

    @Override
    public void delete(long id) {
        productDao.deleteById(id);
    }

    public Page<Product> getProductsByPageFallback(Integer pageNumber, Integer pageSize) {
	logger.info("Circuit Breaker Enabled Searching Product.");
	return Page.empty();
    }

    public Product saveWithoutValidation(Product product) {
	logger.info("Circuit Breaker Enabled Saving Product without Category Validation.");
	return productDao.save(product);
    }

}

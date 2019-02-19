package com.rollingstone.spring.service;

import com.rollingstone.spring.model.Product;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ProductService {

   Product save(Product product);
   Optional<Product> get(long id);
   Page<Product> getProductsByPage(Integer pageNumber, Integer pageSize);
   void update(long id, Product product);
   void delete(long id);
}

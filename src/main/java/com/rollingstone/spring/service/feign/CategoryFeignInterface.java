package com.rollingstone.spring.service.feign;

import com.rollingstone.spring.model.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "rollingstone-ecommerce-category-api")
public interface CategoryFeignInterface {

    @GetMapping("/category/{id}")
    Category getCategory(@PathVariable("id") long id);

}

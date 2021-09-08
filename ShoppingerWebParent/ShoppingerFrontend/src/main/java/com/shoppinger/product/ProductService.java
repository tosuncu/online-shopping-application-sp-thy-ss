package com.shoppinger.product;

import com.shoppinger.common.entity.Product;
import com.shoppinger.common.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    public static final int PRODUCTS_PER_PAGE = 10;
    public static final int SEARCH_RESULTS_PER_PAGE = 10;

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> listByCategory(int pageNum, Integer categoryId) {
        String categoryIDMatch = "-" + String.valueOf(categoryId) + "-";
        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);

        return productRepository.listByCategory(categoryId, categoryIDMatch, pageable);
    }

    public Product getProduct(String alias) throws ProductNotFoundException {
        Product product = productRepository.findProductByAlias(alias);
        if(product == null) {
            throw new ProductNotFoundException("Could not found any product with alias :"+ alias);
        }
        return product;
    }

    public Page<Product> search(String keyword, int pageNum){
        Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_RESULTS_PER_PAGE);
        return productRepository.search(keyword,pageable);
    }
}

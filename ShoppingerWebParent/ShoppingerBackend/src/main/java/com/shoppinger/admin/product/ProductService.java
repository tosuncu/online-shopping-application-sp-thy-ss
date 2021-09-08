package com.shoppinger.admin.product;

import com.shoppinger.admin.paging.PagingAndSortingHelper;
import com.shoppinger.common.exception.ProductNotFoundException;
import com.shoppinger.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ProductService {

    public static final int PRODUCTS_PER_PAGE = 5;
    @Autowired
    private ProductRepository productRepository;

    public List<Product> listAll() {

        return (List<Product>) productRepository.findAll();
    }


    public void listProductssByPage(int pageNum, PagingAndSortingHelper helper, Integer categoryId) {
        Pageable pageable = helper.createPageable(PRODUCTS_PER_PAGE,pageNum);
        String keyword = helper.getKeyword();
        Page<Product> page = null;

        if (keyword != null && !keyword.isEmpty()){
            if (categoryId != null && categoryId > 0) {
                String categoryIdMatch = "-"+ String.valueOf(categoryId)+ "-";
                page = productRepository.searchInCategory(categoryId,categoryIdMatch,keyword,pageable);
            } else {
                page = productRepository.findAll(keyword,pageable);
            }
        } else {
            if (categoryId != null && categoryId > 0) {
                String categoryIdMatch = "-"+ String.valueOf(categoryId)+ "-";
                page = productRepository.findAllInCategory(categoryId, categoryIdMatch,pageable);
            } else {
                page = productRepository.findAll(pageable);
            }
        }
        helper.updateModelAttributes(pageNum,page);
    }

    public Product save(Product product) {
        if (product.getId() == null) {
            product.setCreatedTime(new Date());
        }
        if (product.getAlias() == null || product.getAlias().isEmpty()) {
            String defaultAlias = product.getName().replaceAll(" ", "-");
            product.setAlias(defaultAlias);
        } else {
            product.setAlias(product.getAlias().replaceAll(" ", "-"));
        }

        product.setUpdatedTime(new Date());
        return productRepository.save(product);
    }


    public void saveProductPrice(Product productInForm){
        Product productInDb = productRepository.findById(productInForm.getId()).get();
        productInDb.setCost(productInForm.getCost());
        productInDb.setPrice(productInForm.getPrice());
        productInDb.setDiscountPercent(productInForm.getDiscountPercent());

        productRepository.save(productInDb);
    }

    public String checkUnique(Integer id, String name) {
        boolean isCreatingNew = (id == null || id == 0);
        Product productByNAme = productRepository.findByName(name);

        if (isCreatingNew) {
            if (productByNAme != null) return "Duplicate";
        } else {
            if (productByNAme != null && productByNAme.getId() != id) {
                return "Duplicate";
            }
        }
        return "OK";
    }

    public void updateProductEnabledStatus(Integer id, boolean enabled) {
        productRepository.updateEnabledStatus(id, enabled);
    }

    public void delete(Integer id) throws ProductNotFoundException {
        Long countById = productRepository.countById(id);

        if (countById == null || countById == 0) {
            throw new ProductNotFoundException("Could not found any product with ID: " + id);
        }
        productRepository.deleteById(id);
    }

    public Product getProductById(Integer id) throws ProductNotFoundException {
        try {
            return productRepository.findById(id).get();
        } catch (NoSuchElementException exception) {
            throw new ProductNotFoundException("Could not find any product with id: " + id);
        }

    }
}

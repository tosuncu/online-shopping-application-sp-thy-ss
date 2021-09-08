package com.shoppinger.admin.product;

import com.shoppinger.common.entity.Brand;
import com.shoppinger.common.entity.Category;
import com.shoppinger.common.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateProduct(){
        Brand brand = entityManager.find(Brand.class,37);
        Category category = entityManager.find(Category.class, 5);

        Product product = new Product();
        product.setName("Acer Aspire Desktop");
        product.setAlias("acer_aspire_desktop");
        product.setShortDescription("A good desktop from Acer");
        product.setFullDescription("Very very good desktop from Acer for full description");

        product.setBrand(brand);
        product.setCategory(category);

        product.setPrice(1000);
        product.setCost(799);
        product.setEnabled(true);
        product.setInStock(true);

        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);

    }
    @Test
    public void testListAllProducts(){
        Iterable<Product> iterable = productRepository.findAll();
        iterable.forEach(System.out::println);

    }
    @Test
    public void testGetProduct(){
     Integer id = 5;
     Product product = productRepository.findById(id).get();
        System.out.println(product);
       assertThat(product).isNotNull();
    }

    @Test
    public void testUpdateProduct(){
        Integer id = 5;
        Product product = productRepository.findById(id).get();
        product.setPrice(9999);

        productRepository.save(product);
        Product updatedProduct = entityManager.find(Product.class,id);

       assertThat(updatedProduct.getPrice()).isEqualTo(9999);
    }

    @Test
    public void testDeleteProduct(){
        Integer id = 5;
        productRepository.deleteById(id);

        Optional<Product> result = productRepository.findById(id);
        assertThat(!result.isPresent());

    }


    @Test
    public void testSaveProductWithImages(){
        Integer productId = 6;
        Product product = productRepository.findById(productId).get();

        product.setMainImage("main image.jpg");
        product.addExtraImage("extra image 1.png");
        product.addExtraImage("extra image 2.png");
        product.addExtraImage("extra image 3.png");

        Product savedProduct = productRepository.save(product);
        assertThat(savedProduct.getImages().size()).isEqualTo(3);
    }
    @Test
    public void testProductWithDetails(){
        Integer productId = 6;
        Product product = productRepository.findById(productId).get();

        product.addProductDetails("Device Memory", "128 GB");
        product.addProductDetails("CPU Model", "Mediatek");
        product.addProductDetails("OS", "Android 10");

        Product savedProduct = productRepository.save(product);
        assertThat(savedProduct.getDetails()).isNotEmpty();



    }




}

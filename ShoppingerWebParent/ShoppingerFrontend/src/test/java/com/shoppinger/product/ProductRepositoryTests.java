package com.shoppinger.product;

import com.shoppinger.common.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testFindByAlias(){
        String alias = "canon-eos-m50";
        Product product = productRepository.findProductByAlias(alias);

        assertThat(product).isNotNull();
    }
}

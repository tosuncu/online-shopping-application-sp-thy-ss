package com.shoppinger.admin.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@SpringBootApplication
@EntityScan({"com.shoppinger.common.entity","com.shoppinger.admin.user"})
public class ShoppingerBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingerBackendApplication.class, args);
    }

}

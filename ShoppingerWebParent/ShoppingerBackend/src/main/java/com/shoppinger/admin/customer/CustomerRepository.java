package com.shoppinger.admin.customer;

import com.shoppinger.admin.paging.SearchRepository;
import com.shoppinger.common.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends SearchRepository<Customer,Integer> {
    @Query("SELECT c FROM Customer c WHERE CONCAT(c.email, ' ', c.firstName, ' ',c.lastName,' ', "
    + "c.addresLine1, ' ', c.addresLine2, ' ', c.city, ' ', c.state, ' ', "
    + "c.postalCode, ' ', c.country.name, ' ' ) LIKE %?1%")
    Page<Customer> findAll(String keyword, Pageable pageable);

   // Page<Customer> findAll(Pageable pageable);

    @Query("UPDATE Customer c SET c.enabled = ?2 WHERE c.id = ?1")
    @Modifying
    void updateEnabledStatus(Integer id, boolean enabled);
    @Query("SELECT c FROM Customer c WHERE c.email = ?1")
    Customer findByEmail(String email);

    Long countById(Integer id);

    Page<Customer> findAll(Pageable pageable);
}

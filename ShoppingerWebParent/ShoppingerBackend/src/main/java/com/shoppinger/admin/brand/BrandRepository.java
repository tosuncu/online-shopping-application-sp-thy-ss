package com.shoppinger.admin.brand;

import com.shoppinger.admin.paging.SearchRepository;
import com.shoppinger.common.entity.Brand;
import com.shoppinger.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BrandRepository extends SearchRepository<Brand,Integer> {

    Long countById(Integer id);

    Brand findByName(String name);

    @Query("SELECT b FROM Brand b WHERE b.name LIKE %?1% ")
    Page<Brand> findAll(String keyword, Pageable pageable);

    @Query("SELECT NEW Brand(b.id, b.name) FROM Brand b ORDER BY b.name ASC")
    List<Brand> findAll();
}

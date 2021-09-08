package com.shoppinger.admin.brand;

import com.shoppinger.admin.paging.PagingAndSortingHelper;
import com.shoppinger.common.exception.BrandNotFoundException;
import com.shoppinger.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class BrandService {

    public static final int BRANDS_PER_PAGE = 10;

    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> listAllBrands(){

        return  (List<Brand>) brandRepository.findAll();
    }

    public void listBrandsByPage(int pageNum, PagingAndSortingHelper helper){
        helper.listEntities(pageNum,BRANDS_PER_PAGE, brandRepository);
    }

    public Brand save(Brand brand){
        return brandRepository.save(brand);
    }

    public Brand getBrandById(Integer id) throws BrandNotFoundException {
        try {
            return brandRepository.findById(id).get();
        }catch (NoSuchElementException ex) {
            throw new BrandNotFoundException("Could not find any brand with ID: "+id);
        }
    }

    public void delete(Integer id) throws BrandNotFoundException {
        Long countById = brandRepository.countById(id);

        if (countById == null || countById ==0){
            throw new BrandNotFoundException("Could not found any brand with ID: "+ id);
        }
        brandRepository.deleteById(id);
    }

    public String checkUnique(Integer id, String name){
        boolean isCreatingNew = (id == null || id == 0);
        Brand brandByName = brandRepository.findByName(name);

        if(isCreatingNew){
            if(brandByName != null) return "Duplicate";
        } else {
            if(brandByName != null && brandByName.getId() != id) {
                return "Duplicate";
            }
        }
        return "OK";
    }


}

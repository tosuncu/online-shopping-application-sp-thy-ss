package com.shoppinger.admin.customer;

import com.shoppinger.admin.paging.PagingAndSortingHelper;
import com.shoppinger.admin.setting.CountryRepository;
import com.shoppinger.common.entity.Country;
import com.shoppinger.common.entity.Customer;
import com.shoppinger.common.entity.Product;
import com.shoppinger.common.entity.User;
import com.shoppinger.common.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CustomerService {
    public static final int CUSTOMERS_PER_PAGE = 10;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public void listByPage(int pageNum, PagingAndSortingHelper helper) {
        helper.listEntities(pageNum, CUSTOMERS_PER_PAGE, customerRepository);
    }

    public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
        customerRepository.updateEnabledStatus(id, enabled);
    }

    public Customer get(Integer id) throws CustomerNotFoundException {
        try {
            return customerRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new CustomerNotFoundException("Could not find any customer with id: " + id);
        }

    }

    public List<Country> listAllCountries() {
        return countryRepository.findAllByOrderByNameAsc();
    }

    public boolean isEmailUnique(Integer id, String email) {
        Customer customerByEmail = customerRepository.findByEmail(email);
        if (customerByEmail != null && customerByEmail.getId() != id) {
            //found anotesdfds
            return false;
        }
        return true;
    }

    public void save(Customer customerInform) {
        Customer customerInDB = customerRepository.findById(customerInform.getId()).get();
        if (!customerInform.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(customerInform.getPassword());
            customerInform.setPassword(encodedPassword);
        } else {

            customerInform.setPassword(customerInDB.getPassword());
        }
        customerInform.setEnabled(customerInDB.isEnabled());
        customerInform.setCreatedTime(customerInDB.getCreatedTime());
        customerInform.setVerificationCode(customerInDB.getVerificationCode());
        customerRepository.save(customerInform);
    }
    public void delete(Integer id) throws CustomerNotFoundException {
        Long count =customerRepository.countById(id);
        if (count == null || count == 0){
            throw new CustomerNotFoundException("Could not find any customer with ID: "+ id);
        }
        customerRepository.deleteById(id);
    }


}

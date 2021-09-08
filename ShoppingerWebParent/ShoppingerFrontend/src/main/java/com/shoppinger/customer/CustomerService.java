package com.shoppinger.customer;

import com.shoppinger.common.entity.AuthenticationType;
import com.shoppinger.common.entity.Country;
import com.shoppinger.common.entity.Customer;
import com.shoppinger.setting.CountryRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<Country> listAllCountries(){
       return countryRepository.findAllByOrderByNameAsc();
    }
    public boolean isEmailUnique(String email){
        Customer customer = customerRepository.findByEmail(email);

        return customer == null;
    }
    public void registerCustomer(Customer customer){
        encodePassword(customer);
        customer.setEnabled(false);
        customer.setCreatedTime(new Date());
        customer.setAuthenticationType(AuthenticationType.DATABASE);

        String randomCode = RandomString.make(64);
        customer.setVerificationCode(randomCode);

        customerRepository.save(customer);
    }

    private void encodePassword(Customer customer) {
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
    }

    public boolean verify(String verificationCode){
        Customer customer = customerRepository.findByVerificationCode(verificationCode);

        if(customer == null || customer.isEnabled()) {
            return false;
        }else {
            customerRepository.enable(customer.getId());
            return true;
        }

    }
    public void updateAuthenticationType(Customer customer, AuthenticationType authenticationType){
        if(!customer.getAuthenticationType().equals(authenticationType)){
            customerRepository.updateAuthenticationType(customer.getId(), authenticationType);
        }
    }
    public Customer getCustomerByEmail(String email){
        return customerRepository.findByEmail(email);
    }

    public void addNewCustomerUponOAuthLogin(String name, String email, String countryCode, AuthenticationType authenticationType) {
        Customer customer = new Customer();
        customer.setEmail(email);
        setName(name,customer);

        customer.setEnabled(true);
        customer.setCreatedTime(new Date());
        customer.setPassword("");
        customer.setAddresLine1("");
        customer.setCity("");
        customer.setState("");
        customer.setPhoneNumber("");
        customer.setPostalCode("");
        customer.setCountry(countryRepository.findByCode(countryCode));
        customer.setAuthenticationType(authenticationType);
        customerRepository.save(customer);
    }

    private void setName(String name, Customer customer){
        String[] nameArray = name.split(" ");
        if(nameArray.length < 2) {
            customer.setFirstName(name);
            customer.setLastName(" ");
        } else {
            String lastName = nameArray[nameArray.length - 1];
            customer.setLastName(lastName);
            String firstName =  name.replaceFirst(" " + lastName, "");
            customer.setFirstName(firstName);
        }
    }

}


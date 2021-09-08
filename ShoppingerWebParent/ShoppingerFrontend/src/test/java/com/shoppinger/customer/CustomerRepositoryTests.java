package com.shoppinger.customer;

import com.shoppinger.common.entity.AuthenticationType;
import com.shoppinger.common.entity.Country;
import com.shoppinger.common.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    public void testCreateCustomer(){
        Integer countryId = 234;
        Country country = entityManager.find(Country.class, countryId);

        Customer customer = new Customer();
        customer.setCountry(country);
        customer.setFirstName("Abduraman");
        customer.setLastName("Abdyramab");
        customer.setPassword("password123");
        customer.setEmail("abduraman@gmail.com");
        customer.setPhoneNumber("90-534-203-9306");
        customer.setAddresLine1("1564 Evrenseki");
        customer.setAddresLine2("fsfsfddsfds");
        customer.setCity("Manavgat");
        customer.setState("Antalya");
        customer.setPostalCode("07600");
        customer.setCreatedTime(new Date());

        Customer savedCustomer = customerRepository.save(customer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isGreaterThan(0);

    }

    @Test
    public void testCreateCustomer2(){
        Integer countryId = 106;
        Country country = entityManager.find(Country.class, countryId);

        Customer customer = new Customer();
        customer.setCountry(country);
        customer.setFirstName("Ali");
        customer.setLastName("Yıldırım");
        customer.setPassword("password123");
        customer.setEmail("aliyıldırım@gmail.com");
        customer.setPhoneNumber("90-534-203-9306");
        customer.setAddresLine1("1453, Davaristan Mahallesi, Konya yolu");
        customer.setAddresLine2("Kuzey Ankara");
        customer.setCity("Mumbai");
        customer.setState("Maharastra");
        customer.setPostalCode("400013");
        customer.setCreatedTime(new Date());

        Customer savedCustomer = customerRepository.save(customer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isGreaterThan(0);

    }

    @Test
    public void testListCustomers(){
        Iterable<Customer> customers = customerRepository.findAll();
        customers.forEach(System.out::println);

        assertThat(customers).hasSizeGreaterThan(1);
    }

    @Test
    public void testUpdateCustomer(){
        Integer customerId = 1;
        String verificationCode = "code_123";

        Customer customer = customerRepository.findById(customerId).get();

        customer.setVerificationCode(verificationCode);

        Customer updatedCustomer = customerRepository.save(customer);
        assertThat(updatedCustomer.getVerificationCode()).isEqualTo(verificationCode);

    }
    @Test
    public void testGetCustomer(){
        Integer customerId = 2;
        Optional<Customer> byId = customerRepository.findById(customerId);

        assertThat(byId).isPresent();

        Customer customer = byId.get();
        System.out.println(customer);

    }
    @Test
    public void testFindByEmail(){
        String email = "aliyıldırım@gmail.com";
        Customer customer = customerRepository.findByEmail(email);

        assertThat(customer).isNotNull();
        System.out.println(customer);
    }

    @Test
    public void testFindByVerificationCode(){
        String verificationCode = "code_123";
        Customer customer = customerRepository.findByVerificationCode(verificationCode);

        assertThat(customer).isNotNull();
        System.out.println(customer);
    }

    @Test
    public void testEnableCustomer(){
        Integer customerId = 2;
        customerRepository.enable(customerId);

        Customer customer = customerRepository.findById(customerId).get();
        assertThat(customer.isEnabled()).isTrue();
    }
    @Test
    public void testUpdateAuthenticationType(){
        Integer id = 3;
        customerRepository.updateAuthenticationType(id, AuthenticationType.DATABASE);

        Customer customer = customerRepository.findById(id).get();

        assertThat(customer.getAuthenticationType()).isEqualTo(AuthenticationType.DATABASE);

    }
}

package com.shoppinger.admin.setting;

import com.shoppinger.common.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CountryRepositoryTests {
    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void testCreateCountry(){
        Country country = countryRepository.save(new Country("Algeria", "ALG"));
        assertThat(country).isNotNull();
        assertThat(country.getId()).isGreaterThan(0);
    }

    @Test
    public void  testListCountries(){
        List<Country> countryList = countryRepository.findAllByOrderByNameAsc();
        countryList.forEach(System.out::println);

        assertThat(countryList.size()).isGreaterThan(0);
    }

    @Test
    public void testUpdateCountry(){
        Integer id = 2;
        String name = "Cezayir";

        Country country = countryRepository.findById(id).get();
        country.setName(name);

        Country updatedCountry = countryRepository.save(country);

        assertThat(updatedCountry.getName()).isEqualTo(name);
    }

    @Test
    public void getCountry(){
        Integer id = 2;
        Country country = countryRepository.findById(id).get();
        assertThat(country).isNotNull();
    }

    @Test
    public void testDeleteCountry(){
        Integer id = 2;
        countryRepository.deleteById(id);

        Optional<Country> byId = countryRepository.findById(id);
        assertThat(byId.isEmpty());
    }


}

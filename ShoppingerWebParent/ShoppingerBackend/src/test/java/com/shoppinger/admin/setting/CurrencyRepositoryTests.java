package com.shoppinger.admin.setting;

import com.shoppinger.common.entity.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CurrencyRepositoryTests {
    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    public void testCreateCurrencies(){
        List<Currency> currencyList = Arrays.asList(
                new Currency("United States Dollar", "$", "USD"),
                new Currency("British Pound", "£", "GBP"),
                new Currency("Japanese Yen", "¥", "JPY"),
                new Currency("Euro", "€", "EUR"),
                new Currency("Russian Ruble", "₽", "RUB"),
                new Currency("South Korean Won", "₩", "KRW"),
                new Currency("Chineese Yuan", "¥", "CNY"),
                new Currency("Brasilian Real", "R$", "BRL"),
                new Currency("Australian Dollar", "$", "AUD"),
                new Currency("Canadian Dollar", "C$", "CAD"),
                new Currency("Vietnames đồng", "₫", "VND"),
                new Currency("Indian Rupe", "₹", "INR"),
                new Currency("Turkish Lira", "₺","TRY")
        );

        currencyRepository.saveAll(currencyList);

        Iterable<Currency> iterable = currencyRepository.findAll();
        assertThat(iterable).size().isEqualTo(13);

    }
    @Test
    public void testListAllCurrenciesOrderByNameAsc(){
        List<Currency> currencies = currencyRepository.findAllByOrderByNameAsc();
        currencies.forEach(System.out::println);
        assertThat(currencies.size()).isGreaterThan(0);
    }

}

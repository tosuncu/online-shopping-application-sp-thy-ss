package com.shoppinger.admin.setting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppinger.common.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import  org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.CsrfRequestPostProcessor;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class CountryRestControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "tosuncu07@gmail.com", password = "887569-mht", roles = "ADMIN")
    public void testListCountries() throws Exception {
        String url = "/countries/list";
        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        System.out.println(jsonResponse);

        Country[] countries = objectMapper.readValue(jsonResponse, Country[].class);

        assertThat(countries).hasSizeGreaterThan(0);
    }

    @Test
    @WithMockUser(username = "tosuncu07@gmail.com", password = "887569-mht", roles = "ADMIN")
    public void testCreateCountry() throws Exception {
        String url = "/countries/save";
        String countryName = "Canada";
        String countryCode = "CA";
        Country country = new Country(countryName,countryCode);

        MvcResult mvcResult = mockMvc.perform(post(url).contentType("application/json")
                .content(objectMapper.writeValueAsString(country)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        System.out.println("Country ID:" + response);

    }
}

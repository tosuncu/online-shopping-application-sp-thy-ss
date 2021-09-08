package com.shoppinger.admin.setting;

import com.shoppinger.common.entity.Country;
import com.shoppinger.common.entity.State;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class StateRepositoryTests {
    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testCreateChianasStates(){
        Integer countryId = 1;
        Country country = testEntityManager.find(Country.class,countryId);

      //  State state = stateRepository.save(new State("Shangai", country));
       // State state = stateRepository.save(new State("Beijing", country));
        State state = stateRepository.save(new State("ChangChing", country));

        assertThat(state).isNotNull();
        assertThat(state.getId()).isGreaterThan(0);

    }

    @Test
    public void testListStatesByCountry(){
        Integer countryId = 1;
        Country country = testEntityManager.find(Country.class, countryId);

        List<State> stateList = stateRepository.findByCountryOrderByNameAsc(country);
        stateList.forEach(System.out::println);

        assertThat(stateList.size()).isGreaterThan(0);
    }

    @Test
    public void testUpdateState(){
        Integer stateId = 3;
        String stateName = "CongChing";

        State state = stateRepository.findById(stateId).get();
        state.setName(stateName);
        State updatedState = stateRepository.save(state);

        assertThat(updatedState.getName()).isEqualTo(stateName);
    }
    @Test
    public void testDeleteState(){
        Integer id = 2;
        stateRepository.deleteById(id);

        Optional<State> byId = stateRepository.findById(id);
        assertThat(byId.isEmpty());
    }
}

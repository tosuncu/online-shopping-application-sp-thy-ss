package com.shoppinger.setting;

import com.shoppinger.common.entity.Country;
import com.shoppinger.common.entity.State;
import com.shoppinger.common.entity.StateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StateRestController {
    @Autowired
    private StateRepository stateRepository;

    @GetMapping("/settings/list_states_by_country/{id}")
    public List<StateDTO> listByCountry(@PathVariable("id") Integer countryId){
        List<State> stateList = stateRepository.findByCountryOrderByNameAsc(new Country(countryId));
        List<StateDTO> result = new ArrayList<>();

        for (State state : stateList) {
            result.add(new StateDTO(state.getId(), state.getName()));
        }
        return result;
    }

}

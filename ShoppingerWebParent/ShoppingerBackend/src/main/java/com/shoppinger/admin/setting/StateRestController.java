package com.shoppinger.admin.setting;

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

    @GetMapping("/states/list_by_country/{id}")
    public List<StateDTO> listByCountry(@PathVariable("id") Integer countryId){
        List<State> stateList = stateRepository.findByCountryOrderByNameAsc(new Country(countryId));
        List<StateDTO> result = new ArrayList<>();

        for (State state : stateList) {
            result.add(new StateDTO(state.getId(), state.getName()));
        }
        return result;
    }
    @PostMapping("/states/save")
    public String save (@RequestBody State state){
        State savedState = stateRepository.save(state);
        return String.valueOf(savedState.getId());
    }
    @DeleteMapping("/states/delete/{id}")
    public void delete(@PathVariable("id") Integer id){
        stateRepository.deleteById(id);
    }
}

package com.shoppinger.setting;

import com.shoppinger.common.entity.Country;
import com.shoppinger.common.entity.State;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StateRepository extends CrudRepository<State,Integer> {

    List<State> findByCountryOrderByNameAsc(Country country);
}

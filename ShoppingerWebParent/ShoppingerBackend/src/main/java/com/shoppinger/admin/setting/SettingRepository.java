package com.shoppinger.admin.setting;

import com.shoppinger.common.entity.Setting;
import com.shoppinger.common.entity.SettingCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SettingRepository extends CrudRepository<Setting, String > {

    List<Setting> findByCategory(SettingCategory category);

}

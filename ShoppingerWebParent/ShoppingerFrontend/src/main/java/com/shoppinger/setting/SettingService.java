package com.shoppinger.setting;

import com.shoppinger.common.entity.Setting;
import com.shoppinger.common.entity.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class SettingService {
    @Autowired
    private SettingRepository settingRepository;


    public   List<Setting> getGeneralSettings(){
        return settingRepository.findByTwoCategories(SettingCategory.GENERAL,SettingCategory.CURRENCY);
    }

    public EmailSettingBag getEmailSettings(){
        List<Setting> settings = settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
        settings.addAll(settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES));
        return new EmailSettingBag(settings);
    }
}

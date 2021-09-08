package com.shoppinger.admin.setting;

import com.shoppinger.common.entity.Setting;
import com.shoppinger.common.entity.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingService {
    @Autowired
    private SettingRepository settingRepository;

    public List<Setting> listAllSettings(){
        return (List<Setting>) settingRepository.findAll();
    }

    public GeneralSettingBag getGeneralSettings(){
        List<Setting> settings = new ArrayList<>();

        List<Setting> generalSettings = settingRepository.findByCategory(SettingCategory.GENERAL);
        List<Setting> currencySettings = settingRepository.findByCategory(SettingCategory.CURRENCY);

        settings.addAll(generalSettings);
        settings.addAll(currencySettings);

        return new GeneralSettingBag(settings);
    }

    public void saveAll(Iterable<Setting> settings) {
        settingRepository.saveAll(settings);
    }

    public List<Setting> getMailServerSettings(){
        return settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
    }

    public List<Setting> getMailTemplateSettings(){
        return settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES);
    }

}

package com.shoppinger.admin.setting;

import com.shoppinger.common.entity.Setting;
import com.shoppinger.common.entity.SettingBag;

import java.util.List;

public class GeneralSettingBag extends SettingBag {
    public GeneralSettingBag(List<Setting> settingList) {
        super(settingList);
    }

    public void updateCurrencySymbol(String value) {
        super.update("CURRENCY_SYMBOL", value);
    }
    public void updateSiteLogo(String value) {
        super.update("SITE_LOGO", value);
    }
}

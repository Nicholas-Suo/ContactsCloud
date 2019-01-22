package tech.xiaosuo.com.contactscloud.Menu;

import tech.xiaosuo.com.contactscloud.R;

public class SettingsMenu extends BaseMenu{
    public static final String menuName = "settings_menu";
    @Override
    public BaseMenu createMenu() {
        SettingsMenu settingsMenu = new SettingsMenu();
        settingsMenu.setMenuName(menuName);
        settingsMenu.setIconId(R.drawable.cloud_icon);
        settingsMenu.setNameId(R.string.settings);
        settingsMenu.setCloudCount(BaseMenu.hidden);
        return settingsMenu;
    }
}

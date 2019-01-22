package tech.xiaosuo.com.contactscloud.Menu;

import tech.xiaosuo.com.contactscloud.R;

public class BindPhoneNumberMenu extends BaseMenu {
    public static final String menuName = "bind_phone_number_menu";
    @Override
    public BaseMenu createMenu() {
        BindPhoneNumberMenu bindPhoneNumberMenu = new BindPhoneNumberMenu();
        bindPhoneNumberMenu.setMenuName(menuName);
        bindPhoneNumberMenu.setIconId(R.drawable.cloud_icon);
        bindPhoneNumberMenu.setNameId(R.string.replace_phone_number);
        bindPhoneNumberMenu.setCloudCount(BaseMenu.hidden);
        return bindPhoneNumberMenu;
    }
}

package tech.xiaosuo.com.contactscloud.Menu;

import tech.xiaosuo.com.contactscloud.R;

public class ModifyPasswordMenu extends BaseMenu{
    public static final String menuName = "modify_password_menu";
    @Override
    public BaseMenu createMenu() {
        ModifyPasswordMenu modifyPasswordMenu = new ModifyPasswordMenu();
        modifyPasswordMenu.setMenuName(menuName);
        modifyPasswordMenu.setIconId(R.drawable.cloud_icon);
        modifyPasswordMenu.setNameId(R.string.modify_password);
        modifyPasswordMenu.setCloudCount(BaseMenu.hidden);
        return modifyPasswordMenu;
    }
}

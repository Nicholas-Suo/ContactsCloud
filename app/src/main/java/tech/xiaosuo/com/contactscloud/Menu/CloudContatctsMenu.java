package tech.xiaosuo.com.contactscloud.Menu;

import tech.xiaosuo.com.contactscloud.R;

public class CloudContatctsMenu extends BaseMenu {
    public static final String menuName = "cloud_contacts";
    @Override
    public BaseMenu createMenu() {
        CloudContatctsMenu cloudContactMenu = new CloudContatctsMenu();
        cloudContactMenu.setMenuName(menuName);
        cloudContactMenu.setIconId(R.drawable.cloud_icon);
        cloudContactMenu.setNameId(R.string.cloud_contacts);
        cloudContactMenu.setCloudCount(0);
        return cloudContactMenu;
    }
}

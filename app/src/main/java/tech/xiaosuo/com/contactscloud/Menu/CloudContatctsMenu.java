package tech.xiaosuo.com.contactscloud.Menu;

import tech.xiaosuo.com.contactscloud.R;

public class CloudContatctsMenu extends MenuBean {

    @Override
    public MenuBean createMenu() {
        CloudContatctsMenu cloudContactMenu = new CloudContatctsMenu();
        cloudContactMenu.setIconId(R.drawable.cloud_icon);
        cloudContactMenu.setNameId(R.string.cloud_contacts);
        cloudContactMenu.setCloudCount(0);
        return cloudContactMenu;
    }
}

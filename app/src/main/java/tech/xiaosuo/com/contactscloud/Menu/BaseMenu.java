package tech.xiaosuo.com.contactscloud.Menu;

public abstract class BaseMenu {
    int nameId;
    int cloudCount;
    int iconId;
    String menuName;

    public int getNameId() {
        return nameId;
    }

    public void setNameId(int nameId) {
        this.nameId = nameId;
    }

    public int getCloudCount() {
        return cloudCount;
    }

    public int getIconId() {
        return iconId;
    }

    public void setCloudCount(int cloudCount) {
        this.cloudCount = cloudCount;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public abstract BaseMenu createMenu();
}

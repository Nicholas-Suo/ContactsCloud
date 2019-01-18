package tech.xiaosuo.com.contactscloud.Menu;

public abstract class MenuBean {
    int nameId;
    int cloudCount;
    int iconId;

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

    public abstract MenuBean createMenu();
}

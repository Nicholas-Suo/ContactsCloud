package tech.xiaosuo.com.contactscloud.Menu;




public class MenuFactory {

    public  static <T extends BaseMenu>  T createMenu(Class<T> clz){
        BaseMenu menuBean = null;
        try {
            menuBean = ((BaseMenu)Class.forName(clz.getName()).newInstance()).createMenu();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return (T)menuBean;
    }

}

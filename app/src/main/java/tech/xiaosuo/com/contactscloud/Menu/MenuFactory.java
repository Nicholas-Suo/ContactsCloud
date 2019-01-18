package tech.xiaosuo.com.contactscloud.Menu;




public class MenuFactory {

    public  static <T extends MenuBean>  T createMenu(Class<T> clz){
           MenuBean menuBean = null;
        try {
            menuBean = ((MenuBean)Class.forName(clz.getName()).newInstance()).createMenu();
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

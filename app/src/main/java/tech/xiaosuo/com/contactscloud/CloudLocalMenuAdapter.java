package tech.xiaosuo.com.contactscloud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import tech.xiaosuo.com.contactscloud.Menu.BaseMenu;
import tech.xiaosuo.com.contactscloud.Menu.CloudContatctsMenu;
import tech.xiaosuo.com.contactscloud.Menu.MenuFactory;

public class CloudLocalMenuAdapter extends BaseAdapter {


    List<BaseMenu> menuList = null;
    Context context;

    public  CloudLocalMenuAdapter(Context context){
        this.context = context;
      //  initMenuList();
    }

    public void setMenuList(List<BaseMenu> menuList) {
        this.menuList = menuList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return menuList == null ? 0 : menuList.size();
    }

    @Override
    public Object getItem(int position) {
        return menuList == null ? null : menuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemHolder holder = null;
         if(convertView == null){
             convertView = LayoutInflater.from(context).inflate(R.layout.cloud_local_menu_item,null,false);
             holder = new ItemHolder();
             holder.menuNameView = (TextView)convertView.findViewById(R.id.cloud_local_menu_item_text);
             holder.menuImageView = (ImageView)convertView.findViewById(R.id.cloud_local_menu_item_icon);
             holder.menuCountView = (TextView)convertView.findViewById(R.id.cloud_local_menu_item_count);
             convertView.setTag(holder);
        }else{
             holder = (ItemHolder)convertView.getTag();
         }

        BaseMenu menuBean = (BaseMenu)getItem(position);
         if(menuBean != null){
             holder.menuImageView.setImageResource(menuBean.getIconId());
             holder.menuNameView.setText(menuBean.getNameId());
             int count = menuBean.getCloudCount();
             if(count == BaseMenu.hidden){
                 holder.menuCountView.setText("");
                // holder.menuCountView.setVisibility(View.INVISIBLE);
             }else{
                 holder.menuCountView.setText(String.valueOf(count));
             }

         }

        return convertView;
    }

    private class ItemHolder{
        TextView menuNameView;
        ImageView menuImageView;
        TextView menuCountView;
    }

  /*  *//* list menu item bean,object*//*
   public static class MenuBean{
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
   }*/

/*    *//**
     * init the menu list ,for show MENU:Cloud Contacts,Local Contacts
     *//*
   private void initMenuList(){
       if(menuList == null){
           menuList = new ArrayList<BaseMenu>();
       }else{
           menuList.clear();
       }
       BaseMenu cloudContactMenu = MenuFactory.createMenu(CloudContatctsMenu.class);

*//*       MenuBean cloudContactMenu = new MenuBean();
       cloudContactMenu.setIconId(R.drawable.cloud_icon);
       cloudContactMenu.setNameId(R.string.cloud_contacts);
       cloudContactMenu.setCloudCount(0);*//*
       menuList.add(cloudContactMenu);

   }*/

}

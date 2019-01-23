package tech.xiaosuo.com.contactscloud.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import tech.xiaosuo.com.contactscloud.CloudLocalMenuAdapter;
import tech.xiaosuo.com.contactscloud.Menu.BaseMenu;
import tech.xiaosuo.com.contactscloud.Menu.BindPhoneNumberMenu;
import tech.xiaosuo.com.contactscloud.Menu.CloudContatctsMenu;
import tech.xiaosuo.com.contactscloud.Menu.MenuFactory;
import tech.xiaosuo.com.contactscloud.Menu.ModifyPasswordMenu;
import tech.xiaosuo.com.contactscloud.Menu.SettingsMenu;
import tech.xiaosuo.com.contactscloud.ModifyPasswordActivity;
import tech.xiaosuo.com.contactscloud.R;
import tech.xiaosuo.com.contactscloud.bmob.UserInfo;

public class MyInfoFragment extends BaseFragment  implements View.OnClickListener, AdapterView.OnItemClickListener{

    private final String TAG = "MyInfoFragment";
    CloudLocalMenuAdapter myInfoMenuAdapter = null;
    ListView listView;
    View myInfoFragmentView;
    Context context;
    LinearLayout myInfoLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myInfoFragmentView = inflater.inflate(R.layout.myinfo_fragment,null,false);

        return myInfoFragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getContext();
        myInfoLayout = (LinearLayout)myInfoFragmentView.findViewById(R.id.myinfo_layout);
        myInfoLayout.setOnClickListener(this);

        listView = (ListView) myInfoFragmentView.findViewById(R.id.myinfo_menu_list_view);
        myInfoMenuAdapter = new CloudLocalMenuAdapter(context);
        initMenuList();
        listView.setAdapter(myInfoMenuAdapter);
        listView.setOnItemClickListener(this);

        Button button = myInfoFragmentView.findViewById(R.id.login_out);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                logOut();
            }
        });
    }


    /**
     * logout the user.
     */
    private void logOut(){
        UserInfo.logOut();
      /*  Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);*/
    }

    @Override
    void initMenuList() {
        List<BaseMenu> menuList = new ArrayList<BaseMenu>();
        BaseMenu bindPhoneNumberMenu = MenuFactory.createMenu(BindPhoneNumberMenu.class);
        menuList.add(bindPhoneNumberMenu);
        BaseMenu passwordMenu = MenuFactory.createMenu(ModifyPasswordMenu.class);
        menuList.add(passwordMenu);
        BaseMenu settingsMenu = MenuFactory.createMenu(SettingsMenu.class);
        menuList.add(settingsMenu);
        if(myInfoMenuAdapter != null){
            myInfoMenuAdapter.setMenuList(menuList);
        }
    }


    @Override
    public void onClick(View v) {
        UserInfo bmobUser = UserInfo.getCurrentUser(UserInfo.class);
        if(bmobUser == null){
            showNeedLoginDialog(context);
            return;
        }
          int id = v.getId();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UserInfo bmobUser = UserInfo.getCurrentUser(UserInfo.class);
        if(bmobUser == null){
            showNeedLoginDialog(context);
            return;
        }
        BaseMenu menuItem = (BaseMenu)myInfoMenuAdapter.getItem(position);
        String name = menuItem.getMenuName();
        if(BindPhoneNumberMenu.menuName.equals(name)){
            Log.d(TAG," open the bind phone number activity");
        }else if(ModifyPasswordMenu.menuName.equals(name)){
            Log.d(TAG," open the modify password activity");
            Intent mdPwdintent = new Intent(context, ModifyPasswordActivity.class);
            startActivity(mdPwdintent);
        }else if(SettingsMenu.menuName.equals(name)){
            Log.d(TAG," open the Settings activity");
        }
    }
}

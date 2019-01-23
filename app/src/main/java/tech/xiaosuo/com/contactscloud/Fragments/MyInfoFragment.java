package tech.xiaosuo.com.contactscloud.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import tech.xiaosuo.com.contactscloud.ReplacePhoneNumberActivity;
import tech.xiaosuo.com.contactscloud.Utils;
import tech.xiaosuo.com.contactscloud.bmob.UserInfo;

public class MyInfoFragment extends BaseFragment  implements View.OnClickListener, AdapterView.OnItemClickListener{

    private final String TAG = "MyInfoFragment";
    CloudLocalMenuAdapter myInfoMenuAdapter = null;
    ListView listView;
    View myInfoFragmentView;
    Context context;
    LinearLayout myInfoLayout;
    TextView userNameView;
    TextView mobileNummerView;
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
        userNameView = myInfoFragmentView.findViewById(R.id.user_name_myinfo_view);
        mobileNummerView = myInfoFragmentView.findViewById(R.id.phone_number_myinfo_view);

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

    @Override
    public void onResume() {
        super.onResume();
        UserInfo bmobUser = UserInfo.getCurrentUser(UserInfo.class);
        if(bmobUser != null){
            userNameView.setText(bmobUser.getUsername());
            String number = Utils.modifyPhoneMiddleNumberUsingStar(bmobUser.getMobilePhoneNumber());
            mobileNummerView.setText(number);
        }
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
            Intent bindPhoneintent = new Intent(context, ReplacePhoneNumberActivity.class);
            startActivity(bindPhoneintent);
        }else if(ModifyPasswordMenu.menuName.equals(name)){
            Log.d(TAG," open the modify password activity");
            Intent mdPwdintent = new Intent(context, ModifyPasswordActivity.class);
            startActivity(mdPwdintent);
        }else if(SettingsMenu.menuName.equals(name)){
            Log.d(TAG," open the Settings activity");
        }
    }

/*
    */
/**
     * if moidfy the phone number
     * ,need input the password to verify
     *//*

    private void showInputPasswordDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
  */
/*      EditText passwordView = new EditText(context);
        passwordView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);*//*

       // builder.setView(R.layout.dialog_input_password);
        View passwordViewLayout = LayoutInflater.from(context).inflate(R.layout.dialog_input_password,null,false);
        final EditText pwdView = passwordViewLayout.findViewById(R.id.dialog_password_view);
        pwdView.setError(null);
        builder.setTitle(R.string.dialog_title).setMessage(R.string.pls_input_password);
        builder.setView(passwordViewLayout);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pwd = pwdView.getText().toString();
                if(pwd == null){
                    pwdView.setError(getActivity().getString(R.string.password_incrrect));
                }
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                   dialog.dismiss();
            }
        });

        builder.create().show();
    }
*/



}

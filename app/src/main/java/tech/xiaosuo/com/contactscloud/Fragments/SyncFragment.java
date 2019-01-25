package tech.xiaosuo.com.contactscloud.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tech.xiaosuo.com.contactscloud.CloudLocalMenuAdapter;
import tech.xiaosuo.com.contactscloud.ErrorCode;
import tech.xiaosuo.com.contactscloud.LoginActivity;
import tech.xiaosuo.com.contactscloud.Menu.BaseMenu;
import tech.xiaosuo.com.contactscloud.Menu.CloudContatctsMenu;
import tech.xiaosuo.com.contactscloud.Menu.MenuFactory;
import tech.xiaosuo.com.contactscloud.R;
import tech.xiaosuo.com.contactscloud.bmob.UserInfo;

public class SyncFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener{

    private static final String TAG = "SyncFragment";
    ListView mListView = null;
    ImageView syncImageView = null;
    CloudLocalMenuAdapter menuAdapter = null;
    Context mContext = null;
    View syncFragmentView;
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        syncFragmentView = inflater.inflate(R.layout.sync_fragment,null,false);
        mContext = getContext();
        return syncFragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListView = (ListView)syncFragmentView.findViewById(R.id.main_list_view);
        menuAdapter = new CloudLocalMenuAdapter(mContext);
        mListView.setAdapter(menuAdapter);
        initMenuList();
        mListView.setOnItemClickListener(this);
        syncImageView = (ImageView)syncFragmentView.findViewById(R.id.sync_image_view);
        syncImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.sync_image_view:
                UserInfo bmobUser = UserInfo.getCurrentUser(UserInfo.class);
                if(bmobUser == null){
                    showNeedLoginDialog(mContext);
                    return;
                }
                boolean result = requestContactsPermission();
                if(result){
                    testLoadContacts();
                }
                break;
                default:
                    break;
        }
    }

/*    *//**
     * show the dialog: you need login first.
     *
     *//*
    private void showNeedLoginDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.dialog_title).setMessage(R.string.pls_login).setPositiveButton(R.string.ok,new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }*/


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UserInfo bmobUser = UserInfo.getCurrentUser(UserInfo.class);
        if(bmobUser == null){
            showNeedLoginDialog(mContext);
            return;
        }
        BaseMenu menuItem = (BaseMenu)menuAdapter.getItem(position);
        String name = menuItem.getMenuName();
        if(CloudContatctsMenu.menuName.equals(name)){
            Log.d(TAG," open the cloud contacts menu list");

        }
    }

    /**
     * init the menu list ,for show MENU:Cloud Contacts,Local Contacts
     */
    @Override
    void initMenuList() {
        List<BaseMenu> menuList = new ArrayList<BaseMenu>();
        BaseMenu cloudContactMenu = MenuFactory.createMenu(CloudContatctsMenu.class);
        menuList.add(cloudContactMenu);
        if(menuAdapter != null){
            menuAdapter.setMenuList(menuList);
        }
    }

    private void testLoadContacts(){
        ContentResolver resolver = getActivity().getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,new String[]{ContactsContract.Contacts.NAME_RAW_CONTACT_ID},null,null,ContactsContract.Contacts.NAME_RAW_CONTACT_ID);
       // Cursor cursor = resolver.query(ContactsContract.Data.CONTENT_URI,new String[]{ContactsContract.Data.RAW_CONTACT_ID},null,null,ContactsContract.Data.RAW_CONTACT_ID);
        int contactNumber = cursor.getCount();
        Log.d(TAG, " the contactNumber: " + contactNumber);
        while(cursor.moveToNext()){
           int rawIdIndex = cursor.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID);
           int rawId  = cursor.getInt(rawIdIndex);
           String selection = ContactsContract.Data.RAW_CONTACT_ID + " =?";
           String[] selectionArgs = new String[]{Integer.toString(rawId)};
           Cursor dataCursor = resolver.query(ContactsContract.Data.CONTENT_URI,null,selection,selectionArgs,ContactsContract.Data.RAW_CONTACT_ID);
            Log.d(TAG, " rawId is : " + rawId);
           while (dataCursor.moveToNext()){
               int mimeTypeIndex = dataCursor.getColumnIndex(ContactsContract.Data.MIMETYPE);
               String mimeType = dataCursor.getString(mimeTypeIndex);
               Log.d(TAG, " mimeType is : " + mimeType);
           }

        }
        cursor.close();
    }

     private boolean requestContactsPermission(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            Log.d(TAG, " the sdk version not request permission");
               return  true;
        }
         if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             if (getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                 Log.d(TAG, " has get the read contact permission");
                 return true;
             }
         }

        if(shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)){
            Log.d(TAG, " should show request permission rational,snaker");
            Snackbar.make(syncImageView,R.string.contact_permission_rationale,Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok,new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},REQUEST_READ_CONTACTS);
                }
            }).show();
        }else{
            Log.d(TAG, " should request permission read contacts");
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},REQUEST_READ_CONTACTS);
        }

        return false;
     }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_READ_CONTACTS){
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                testLoadContacts();
            }else{
                Log.d(TAG, " can not get the read contact permission");
                Toast.makeText(mContext,R.string.pls_open_read_contacts_permisson,Toast.LENGTH_LONG).show();
            }
        }
    }

     /*    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
*//*            Snackbar.make(mPhoneNumberView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });*//*
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }*/
    private void mimeType(){

        //vnd.android.cursor.item/contact_event
       // ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE

        // vnd.android.cursor.item/email_v2
       // ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE

        //vnd.android.cursor.item/group_membership
       // ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE

        //vnd.android.cursor.item/sip_address
        // ContactsContract.CommonDataKinds.SipAddress.CONTENT_ITEM_TYPE

        //vnd.android.cursor.item/im
        // ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE

        // vnd.android.cursor.item/note
        // ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE

        // vnd.android.cursor.item/name
        // ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE

        //vnd.android.cursor.item/organization
        // ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE

        //vnd.android.cursor.item/phone_v2
        // ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE

        //vnd.android.cursor.item/photo
        // ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE

       //vnd.android.cursor.item/website
        // ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE

       // vnd.android.cursor.item/nickname
        //  ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE

        //vnd.android.cursor.item/postal-address_v2
        //  ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE

    }
}

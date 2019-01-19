package tech.xiaosuo.com.contactscloud.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.ListView;

import tech.xiaosuo.com.contactscloud.CloudLocalMenuAdapter;
import tech.xiaosuo.com.contactscloud.ErrorCode;
import tech.xiaosuo.com.contactscloud.LoginActivity;
import tech.xiaosuo.com.contactscloud.Menu.BaseMenu;
import tech.xiaosuo.com.contactscloud.Menu.CloudContatctsMenu;
import tech.xiaosuo.com.contactscloud.R;
import tech.xiaosuo.com.contactscloud.bmob.UserInfo;

public class SyncFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener{

    private static final String TAG = "SyncFragment";
    ListView mListView = null;
    ImageView syncImageView = null;
    CloudLocalMenuAdapter menuAdapter = null;
    Context mContext = null;
    View syncFragmentView;

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
                    showNeedLoginDialog();
                    return;
                }

                break;
                default:
                    break;
        }
    }

    /**
     * show the dialog: you need login first.
     *
     */
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

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UserInfo bmobUser = UserInfo.getCurrentUser(UserInfo.class);
        if(bmobUser == null){
            showNeedLoginDialog();
            return;
        }
        BaseMenu menuItem = (BaseMenu)menuAdapter.getItem(position);
        String name = menuItem.getMenuName();
        if(CloudContatctsMenu.menuName.equals(name)){
            Log.d(TAG," open the cloud contacts menu list");

        }
    }
}

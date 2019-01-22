package tech.xiaosuo.com.contactscloud.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;

import tech.xiaosuo.com.contactscloud.LoginActivity;
import tech.xiaosuo.com.contactscloud.R;

public abstract class BaseFragment extends Fragment {
    abstract void initMenuList();
    /**
     * show the dialog: you need login first.
     *
     */
    public void showNeedLoginDialog(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dialog_title).setMessage(R.string.pls_login).setPositiveButton(R.string.ok,new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(context, LoginActivity.class);
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
}

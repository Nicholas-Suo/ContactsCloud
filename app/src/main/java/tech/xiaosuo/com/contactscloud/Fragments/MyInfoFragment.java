package tech.xiaosuo.com.contactscloud.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import tech.xiaosuo.com.contactscloud.R;
import tech.xiaosuo.com.contactscloud.bmob.UserInfo;

public class MyInfoFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myinfo_fragment,null,false);
        Button button = view.findViewById(R.id.login_out);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                logOut();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    /**
     * logout the user.
     */
    private void logOut(){
        UserInfo.logOut();
      /*  Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);*/
    }
}

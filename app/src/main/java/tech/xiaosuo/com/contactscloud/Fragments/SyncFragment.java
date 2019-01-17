package tech.xiaosuo.com.contactscloud.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import tech.xiaosuo.com.contactscloud.CloudLocalMenuAdapter;
import tech.xiaosuo.com.contactscloud.R;

public class SyncFragment extends BaseFragment {

    ListView mListView = null;
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
    }
}

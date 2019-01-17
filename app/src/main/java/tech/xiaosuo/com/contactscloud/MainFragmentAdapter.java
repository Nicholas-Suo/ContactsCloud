package tech.xiaosuo.com.contactscloud;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class MainFragmentAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentList = null;

    public MainFragmentAdapter(FragmentManager fm,List<Fragment> list) {
        super(fm);
        fragmentList = list;
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList == null ? null : fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }
}

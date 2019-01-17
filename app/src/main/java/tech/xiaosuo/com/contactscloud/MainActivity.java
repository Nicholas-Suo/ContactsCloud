package tech.xiaosuo.com.contactscloud;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tech.xiaosuo.com.contactscloud.Fragments.MyInfoFragment;
import tech.xiaosuo.com.contactscloud.Fragments.SyncFragment;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

   // private TextView mTextMessage;
    private final String TAG = "MainActivity";
    private final int SYNC_PAGE = 0;
    private final int MYINFO_PAGE= 1;
    private ViewPager mainViewPager;
    List<Fragment> mFragmentList = null;
    MainFragmentAdapter mainFragmentAdapter = null;
    BottomNavigationView navigation;
    Context mContext = null;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                 //   mTextMessage.setText(R.string.title_home);
                    mainViewPager.setCurrentItem(SYNC_PAGE);
                    return true;
                case R.id.navigation_myinfo:
                  //  mTextMessage.setText(R.string.title_dashboard);
                    mainViewPager.setCurrentItem(MYINFO_PAGE);
                    return true;
/*                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;*/
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
     //   getSupportActionBar().hide();
      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        initFragments();
        mainFragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager(),mFragmentList);
      //  mTextMessage = (TextView) findViewById(R.id.message);

        mainViewPager = (ViewPager)findViewById(R.id.main_view_pager);
        mainViewPager.addOnPageChangeListener(this);
        mainViewPager.setAdapter(mainFragmentAdapter);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    /**
     * init the fragment view
     */
    private void initFragments(){

         if(mFragmentList == null){
             mFragmentList = new ArrayList<Fragment>();
         }else{
             mFragmentList.clear();
         }

        SyncFragment syncFragment = new SyncFragment();
        mFragmentList.add(syncFragment);
        MyInfoFragment myInfoFragment = new MyInfoFragment();
        mFragmentList.add(myInfoFragment);

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
        Log.d(TAG," onPageScrolled " + i + " v" + v + " i1 " + i1);
    }

    @Override
    public void onPageSelected(int i) {
        Log.d(TAG," onPageSelected " + i);
        if(navigation != null){
            navigation.getMenu().getItem(i).setChecked(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {
        Log.d(TAG," onPageScrollStateChanged " + i);
    }
}

package com.example.hars;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.hars.Adapters.FragmentAdapter;
import com.example.hars.Application.App;
import com.example.hars.Fragments.MyStatusFragment;
import com.example.hars.Fragments.ReserveFragment;
import com.example.hars.Fragments.SettingFragment;
import com.example.hars.Service.MyService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity{

    private TabLayout mTabLayout;
    public ViewPager mViewPager;

    private int[] tabIcons = {
            R.drawable.ic_timer_white_24dp,
            R.drawable.ic_home_white_24dp,
            R.drawable.ic_settings_white_24dp
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((App)getApplicationContext()).ref(this);

        initViewPager();

    }





    private void initViewPager() {
        mTabLayout = findViewById(R.id.tab_layout_main);
        mViewPager = findViewById(R.id.view_pager_main);

        List<String> titles = new ArrayList<>();

        titles.add(getString(R.string.tab_title_main_1));
        titles.add(getString(R.string.tab_title_main_2));
        titles.add(getString(R.string.tab_title_main_3));

        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(2)));


        List<Fragment> fragments = new ArrayList<>();

        fragments.add(new ReserveFragment());

        MyStatusFragment myStatusFragment = new MyStatusFragment();
        fragments.add(myStatusFragment);
        //registerObserver(myStatusFragment);

        fragments.add(new SettingFragment());


        mViewPager.setOffscreenPageLimit(4);
        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        Objects.requireNonNull(mTabLayout.getTabAt(0)).setIcon(tabIcons[0]);
        Objects.requireNonNull(mTabLayout.getTabAt(1)).setIcon(tabIcons[1]);
        Objects.requireNonNull(mTabLayout.getTabAt(2)).setIcon(tabIcons[2]);

    }
    //TODO : 인자로 seat num 받게끔
    public void startService(){
        Log.d("HHH", "calle startService");
        // TODO: 인자로 자리 섹터- 번호 를 받고
        // 여기서 비콘 major minor로 변환한다.`
        //mUser.user_reserve(section,seat);

        Intent intent2 = new Intent(this, MyService.class);
        intent2.putExtra("major", 10001);
        intent2.putExtra("minor", 19641);
        ContextCompat.startForegroundService(this, intent2);

        //TODO : 대기 화면 보여주기
        /*
        Intent intent = new Intent(this, ReservingActivity.class);
        intent.putExtra("major", section);
        intent.putExtra("minor", seat);
        startActivity(intent);
        */
    }

    public void stopService(){
        //mUser.get_user_ref().child("status").setValue(User.Status_user.ONLINE);
        //mUser.user_stop();
        Intent intent = new Intent(this, MyService.class);
        this.stopService(intent);
    }
}

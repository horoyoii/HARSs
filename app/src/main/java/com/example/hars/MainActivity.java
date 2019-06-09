package com.example.hars;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.hars.Adapters.FragmentAdapter;
import com.example.hars.Application.App;
import com.example.hars.Fragments.MyStatusFragment;
import com.example.hars.Fragments.ReserveFragment;
import com.example.hars.Fragments.SettingFragment;
import com.example.hars.Models.User;
import com.example.hars.Service.MyService;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.yqritc.scalablevideoview.ScalableType;
import com.yqritc.scalablevideoview.ScalableVideoView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements Subject{

    public ArrayList<Observer> observers;
    private static int PICK_IMAGE_REQUEST = 1;
    ImageView imgView;
    Button button;

    @Override
    public void registerObserver(Observer ob) {
        observers.add(ob);
    }



    @Override
    public void deleteObserver(Observer ob) {
        int index = observers.indexOf(ob);
        observers.remove(index);
    }

    @Override
    public void notifyTheStatus(User.Status_user status) {
        switch (status) {
            case ONLINE:
                mViewPager.setCurrentItem(1);
                break;
            case RESERVING:
                mViewPager.setCurrentItem(1);
                break;
            case RESERVING_OVER:
            case OCCUPYING:
            case OCCUPYING_OVER:
            case STEPPING_OUT:
            case STEPPING_OUT_OVER:
                mViewPager.setCurrentItem(1);
                break;
        }


        for(Observer ob : observers){
            ob.update(status);
        }
    }

    public void SwitchCurrentFragment(int n){
        mViewPager.setCurrentItem(n);
    }

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
        observers = new ArrayList<>();

        initViewPager();



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();

            boolean emailVerified = user.isEmailVerified();

            String uid = user.getUid();
            Log.d("HHH", uid);

        }


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
        registerObserver(myStatusFragment);

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
    public void startService(String selectedSeatNum){

        Intent intent2 = new Intent(this, MyService.class);
        intent2.putExtra("major", 10001);
        intent2.putExtra("minor", 19641);
        ContextCompat.startForegroundService(this, intent2);


        ((App)getApplication()).getFirebaseUtil().getThisUserRef().child("status").setValue(User.Status_user.RESERVING);
        Intent intent = new Intent(this, ReservingActivity.class);
        intent.putExtra("seatNum", selectedSeatNum);
        startActivity(intent);

    }

    public void stopService(){
        Intent intent = new Intent(this, MyService.class);
        this.stopService(intent);
    }
}

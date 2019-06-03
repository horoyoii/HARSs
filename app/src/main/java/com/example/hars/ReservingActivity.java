package com.example.hars;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.example.hars.Application.App;
import com.example.hars.Models.User;
import com.example.hars.Service.MyService;

public class ReservingActivity extends AppCompatActivity implements Observer{
    public static final String R_MAJOR = "major";
    public static final String R_MINOR = "minor";
    private int section, seatNum;
    String selectedSeatNum;
    Button cancel;
    CircleProgressBar mProgressBar;

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((App)getApplicationContext()).ma.deleteObserver(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserving);

        ((App)getApplicationContext()).ma.registerObserver(this);

        mProgressBar=findViewById(R.id.progressbar);
        mProgressBar.setMax(20);
        mProgressBar.setProgressTextSize(80);
        mProgressBar.setProgressFormatter(new CircleProgressBar.ProgressFormatter() {
            @Override
            public CharSequence format(int progress, int max) {
                return max-progress+"s";
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(
                messageReceiver, new IntentFilter(MyService.ACTION_COUNTER_RES));


        Intent intent = getIntent();
        section = intent.getIntExtra(R_MAJOR, 0);
        seatNum = intent.getIntExtra(R_MINOR, 0);


        selectedSeatNum = intent.getStringExtra("seatNum");

        TextView textView = findViewById(R.id.rsv_txt);
        String us = "Go To Seat : "+selectedSeatNum;

        textView.setText(us);

        cancel = findViewById(R.id.rsv_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show_toast_msg("예약 취소", true);
                finish();
                ((App) getApplicationContext()).ma.stopService();
            }
        });

    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int dis = intent.getIntExtra("msg", 0);
            mProgressBar.setProgress(dis);
        }
    };

    @Override
    public void update(User.Status_user status) {
        switch (status){
            case OCCUPYING:
                ((App)getApplication()).setSelectedSeatNum(selectedSeatNum);
                finish();
                break;
            case RESERVING_OVER:
                Toast.makeText(this, "예약 시간 초과", Toast.LENGTH_LONG).show();
                finish();
                break;
        }
    }
}

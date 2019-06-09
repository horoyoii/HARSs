package com.example.hars.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.hars.Application.App;
import com.example.hars.MainActivity;
import com.example.hars.Models.User;
import com.example.hars.Observer;
import com.example.hars.R;
import com.example.hars.Service.MyService;
import com.example.hars.Util.FirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/*
파이어베이스로부터 listener 가 있다.


 */

public class MyStatusFragment extends Fragment implements Observer {
    private static final String TAG = MyStatusFragment.class.getName();
    private View view;
    ProgressBar mProgressBar;
    //private FirebaseUtil mFirebaseUtil;
    //private User mUser;

    private FrameLayout container_status;
    private Button button_stop;
    private TextView sta, using_time, user_email, CurrentSeat, grade;
    private static int PICK_IMAGE_REQUEST = 1;
    ImageView imgView;

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach()");
        super.onAttach(context);

    }

    public void loadImagefromGallery(View view) {
        //Intent 생성
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //ACTION_PIC과 차이점?
        intent.setType("image/*"); //이미지만 보이게
        //Intent 시작 - 갤러리앱을 열어서 원하는 이미지를 선택할 수 있다.
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);


        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                messageReceiver, new IntentFilter(MyService.ACTION_COUNTER_USE));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");

        view = inflater.inflate(R.layout.fragment_mystatus, container, false);
        sta = view.findViewById(R.id.ms_txt_status);
        button_stop = view.findViewById(R.id.msF_btnStop);
        sta = view.findViewById(R.id.ms_txt_status);
        using_time = view.findViewById(R.id.ms_txt_time);
        user_email = view.findViewById(R.id.txt_user_mail);
        button_stop = view.findViewById(R.id.msF_btnStop);
        CurrentSeat = view.findViewById(R.id.ms_txt_seat);
        grade = view.findViewById(R.id.ms_txt_grade);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                messageReceiver, new IntentFilter(MyService.ACTION_COUNTER_USE));


        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated()");
        super.onActivityCreated(savedInstanceState);


        if (savedInstanceState != null) {
            // Restore last state for checked position.

        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");


        ((App)getActivity().getApplication()).getFirebaseUtil().
                getThisUserRef().
                child("status").
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                //TODO : 이 부분이 에러가 뜰 수 있다. - 사용중일 때 앱을 껏다 킨 경우 어떤 것들이 초기화되지 않은 상태가 된다.
                try{
                    ((MainActivity)getActivity()).notifyTheStatus(User.Status_user.valueOf(value));
                }catch (Exception e){

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void update(User.Status_user status) {
        switch (status){
            case ONLINE:
                using_time.setText("-");
                sta.setText("-");
                button_stop.setText("예약하기");
                CurrentSeat.setText("-");
                button_stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((App)getActivity().getApplication()).ma.SwitchCurrentFragment(0);

                    }
                });
                break;
            case RESERVING:
                sta.setText("예약중");
                break;
            case RESERVING_OVER:
                sta.setTextSize(13);
                sta.setText("예약시간초과");
                button_stop.setText("확인");
                button_stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((App)getActivity().getApplication()).getFirebaseUtil().getThisUserRef().child("status").setValue(User.Status_user.ONLINE);
                        sta.setTextSize(17);
                    }
                });
                break;
            case OCCUPYING:
                sta.setText("사용중");
                CurrentSeat.setText(((App)getActivity().getApplication()).getSelectedSeatNum());
                button_stop.setText("사용종료");
                button_stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((App)getActivity().getApplication()).getFirebaseUtil().getThisUserRef().child("status").setValue(User.Status_user.ONLINE);
                        Toast.makeText(getActivity(), "사용종료", Toast.LENGTH_LONG).show();
                        ((MainActivity)getActivity()).stopService();
                    }
                });
                break;
            case STEPPING_OUT:
                sta.setText("자리비움 중");
                break;
            case STEPPING_OUT_OVER:
                sta.setText("자리비움 초과");
                CurrentSeat.setText("자동 반납되었습니다.");
                button_stop.setText("확인");
                button_stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((App)getActivity().getApplication()).getFirebaseUtil().getThisUserRef().child("status").setValue(User.Status_user.ONLINE);
                        Toast.makeText(getActivity(), "-10점 ",Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
    }



    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("HEEL", "onReceivce on MyStatus");
            int second = intent.getIntExtra("msg", 0);
            String min = String.valueOf(second /60);
            String sec = String.valueOf(second% 60);
            String total_time = min+ " : "+sec;
            using_time.setText(total_time);
        }
    };





    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }





}

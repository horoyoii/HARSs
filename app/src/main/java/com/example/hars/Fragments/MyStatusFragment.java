package com.example.hars.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.hars.Application.App;
import com.example.hars.MainActivity;
import com.example.hars.R;
import com.example.hars.Util.FirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MyStatusFragment extends Fragment  { //TODO : implements Observer
    private static final String TAG = MyStatusFragment.class.getName();
    private View view;
    ProgressBar mProgressBar;
    //private FirebaseUtil mFirebaseUtil;
    //private User mUser;

    private FrameLayout container_status;
    private Button button_stop;
    private TextView sta, using_time, user_email, CurrentSeat;
    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach()");
        super.onAttach(context);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        //LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
        //        messageReceiver, new IntentFilter(MyService.ACTION_COUNTER_USE));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");

        view = inflater.inflate(R.layout.fragment_mystatus, container, false);
        sta = view.findViewById(R.id.ms_txt_status);
        button_stop = view.findViewById(R.id.msF_btnStop);

        FirebaseUtil firebaseUtil = ((App)getActivity().getApplication()).getFirebaseUtil();
        firebaseUtil.getThisUserRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("HHH", dataSnapshot.getKey());
                Log.d("HHH", dataSnapshot.getValue().toString());
                sta.setText(dataSnapshot.child("status").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        button_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).startService();
            }
        });



        /*
        sta = view.findViewById(R.id.ms_txt_status);
        using_time = view.findViewById(R.id.ms_txt_time);
        user_email = view.findViewById(R.id.txt_user_mail);
        user_email.setText(Singleton.getInstance().getUserMail());
        button_stop = view.findViewById(R.id.button2);
        CurrentSeat = view.findViewById(R.id.ms_txt_seat);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                messageReceiver, new IntentFilter(MyService.ACTION_COUNTER_USE));
        */

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

        /*
        mFirebaseUtil = ((MainActivity)getActivity()).mFirebaseUtil;
        mUser = ((MainActivity)getActivity()).mUser;

        mUser.get_user_ref().child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                if (value != null && getActivity() instanceof  MainActivity){
                    ((MainActivity)getActivity()).notifyTheStatus(User.Status_user.valueOf(value));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */

    }
    /*
    @Override
    public void update(User.Status_user status) {
        Log.d("TESS", "call update in MyStatusFragment : "+status);
        switch (status){
            case ONLINE:
                using_time.setText("-");
                sta.setText("-");
                button_stop.setText("예약하기");
                button_stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO : GoTo reservation fragmnet
                    }
                });
                break;
            case RESERVING:
                sta.setText("예약중");
                break;
            case RESERVING_OVER:
                sta.setTextSize(13);
                sta.setText("예약시간초과");
                button_stop.setText("초기화");
                button_stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mUser.user_reservation_over_confirm();
                        sta.setTextSize(17);
                    }
                });
                break;
            case OCCUPYING:
                sta.setText("사용중");
                button_stop.setText("사용종료");
                button_stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((MainActivity)getActivity()).show_toast_msg("사용종료", true);
                        ((MainActivity)getActivity()).stopService();
                    }
                });
                break;
            case STEPPING_OUT:
                sta.setText("자리비움 중");
                break;
            case STEPPING_OUT_OVER:
                sta.setText("자리비움 초과");
                button_stop.setText("초기화");
                button_stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mUser.user_step_out_over_confirm();
                        sta.setTextSize(17);
                    }
                });
                break;
            case SUBSCRIBING:
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
    */


    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach()");
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }



    @Override
    public void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();


    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
    }


}

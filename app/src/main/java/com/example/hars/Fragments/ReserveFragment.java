package com.example.hars.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import static com.example.hars.Service.MyService.point;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.example.hars.Message;
import com.example.hars.R;
import com.example.hars.ShowingMapActivity;


// In this case, the fragment displays simple text based on the page
public class ReserveFragment extends Fragment {
    private static final String TAG = ReserveFragment.class.getName();
    private View view;
    Button button;
    ImageView start_img;
    public static final int sub = 101,not_sub=102;
    final Handler handler = new Handler();



    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach()");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        view = inflater.inflate(R.layout.fragment_reserveandcheck, container, false);

        start_img = view.findViewById(R.id.imageView_start);
        start_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int period =604080000;
                if (point<-25){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            point=0;
                        }
                    }, (period*10) );
                    Intent intent = new Intent(getActivity(), Message.class);
                    startActivity(intent);

                }
                else {
                    Intent intent = new Intent(getActivity(), ShowingMapActivity.class);
                    startActivity(intent);
                }

            }
        });
        ImageView imageView = view.findViewById(R.id.imgAlbum);
        RequestOptions requestOptions = new RequestOptions().centerCrop();
        DrawableImageViewTarget target = new DrawableImageViewTarget(imageView);
        Glide.with(this).setDefaultRequestOptions(requestOptions.override(1300,500)).load(R.raw.jal2).into(target);

        button = view.findViewById(R.id.btn_st);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated()");
        super.onActivityCreated(savedInstanceState);


        if (savedInstanceState != null) {
            // Restore last state for checked position.

        }
    }

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

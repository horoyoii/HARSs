package com.example.hars.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.example.hars.MainActivity;
import com.example.hars.R;
import com.example.hars.ReservingActivity;


// In this case, the fragment displays simple text based on the page
public class ReserveFragment extends Fragment {
    private static final String TAG = ReserveFragment.class.getName();
    private View view;
    Button button;
    //private FirebaseUtil mFirebaseUtil;
    //private User mUser;

    private GridView gridView;
    //private GridAdapter adapter;
    //private ArrayList<Section> sectionItems;

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

        /*
        mFirebaseUtil = ((MainActivity)getActivity()).mFirebaseUtil;
        mUser = ((MainActivity)getActivity()).mUser;

        GridView gridView = (GridView) view.findViewById(R.id.grid_section_info);
        sectionItems = new ArrayList<>();
        adapter = new GridAdapter(((MainActivity)getActivity()).getApplicationContext(), sectionItems);
        gridView.setAdapter(adapter);

        sectionItems.add(new Section("A", Singleton.getInstance().getUsingCounterSectionA(), "20"));
        sectionItems.add(new Section("B", Singleton.getInstance().getUsingCounterSectionB(), "20"));
        sectionItems.add(new Section("C", Singleton.getInstance().getUsingCounterSectionC(), "20"));
        sectionItems.add(new Section("D", Singleton.getInstance().getUsingCounterSectionD(), "20"));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ShowingMapActivity.class);
                switch (i){
                    case 0:
                        intent.putExtra(SECTOR_POSITION, "A");
                        break;
                    case 1:
                        intent.putExtra(SECTOR_POSITION, "B");
                        break;
                    case 2:
                        intent.putExtra(SECTOR_POSITION, "C");
                        break;
                    case 3:
                        intent.putExtra(SECTOR_POSITION, "D");
                        break;
                }
                startActivity(intent);
            }
        });
        */

        ImageView imageView = view.findViewById(R.id.imgAlbum);
        RequestOptions requestOptions = new RequestOptions().centerCrop();
        DrawableImageViewTarget target = new DrawableImageViewTarget(imageView);
        Glide.with(this).setDefaultRequestOptions(requestOptions.override(1300,500)).load(R.raw.jal2).into(target);

        button = view.findViewById(R.id.btn_st);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReservingActivity.class);
                ((MainActivity)getActivity()).startService();
                startActivity(intent);
            }
        });

        //adapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated()");
        super.onActivityCreated(savedInstanceState);


        if (savedInstanceState != null) {
            // Restore last state for checked position.

        }
        /*
        mFirebaseUtil = ((MainActivity)getActivity()).mFirebaseUtil;
        mUser = ((MainActivity)getActivity()).mUser;
        */
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

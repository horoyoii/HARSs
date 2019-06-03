package com.example.hars;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.hars.Application.App;

import java.util.ArrayList;
import java.util.Collections;

import pl.polidea.view.ZoomView;

public class ShowingMapActivity extends AppCompatActivity implements View.OnClickListener {
    private CustomDialog mCustomDialog;

    private Button[] btnListA = new Button[32];

    private String selectedSeatNum;
    public static String EXTRA_EPICENTER = "EXTRA_EPICENTER";
    public static String SECTOR_POSITION = "SECTOR_POSITION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing_map);
        Intent intent = getIntent();

        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.showing_map_item, null, false);
        LinearLayout linearLayout = v.findViewById(R.id.laylay);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(1050, 2000);

        String SeatSector = "A";
        ZoomViewLoad(v, layoutParams, SeatSector);

        LoadButton(v);
        MarkUsingSeat(v);
    }


    public void ZoomViewLoad(View v, LinearLayout.LayoutParams layoutParams, String SectorPosition){
        ZoomView zoomView = new ZoomView(this);
        zoomView.addView(v);
        zoomView.setLayoutParams(layoutParams);
        zoomView.setMiniMapEnabled(false); // 좌측 상단 검은색 미니맵 설정
        zoomView.setMaxZoom(4f); // 줌 Max 배율 설정  1f 로 설정하면 줌 안됩니다.
        zoomView.setMiniMapCaption("Mini Map Test"); //미니 맵 내용
        zoomView.setMiniMapCaptionSize(20); // 미니 맵 내용 글씨 크기 설정
        zoomView.setMiniMapHeight(400);

        switch (SectorPosition){
            case "A":
                zoomView.zoomTo(3f,250f,10f);
                break;
            case "B":
                zoomView.zoomTo(3f,1350f,250f);
                break;
            case "C":
                zoomView.zoomTo(3f,250f,750f);
                break;
            case "D":
                zoomView.zoomTo(3f,1350f,750f);
                break;

            default:
                //zoomView.zoomTo(1f,500f,500f);
                break;
        }

        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        container.addView(zoomView);
    }

    public void MarkUsingSeat(View v){
        ArrayList<Integer> seatA = new ArrayList<>(Collections.nCopies(32, 0));
        seatA.set(0, 1);
        seatA.set(3, 1);
        seatA.set(4, 1);
        seatA.set(8, 1);
        seatA.set(9, 1);
        seatA.set(12, 1);
        seatA.set(14, 1);
        seatA.set(17, 1);


        for(int i =0;i<seatA.size();i++) {
            Log.d("KKK", String.valueOf(i));
            if (seatA.get(i) == 1) {
                btnListA[i].setBackgroundColor(getResources().getColor(R.color.black));
                btnListA[i].setText("X");
                btnListA[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "이미 사용중입니다.",Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                btnListA[i].setOnClickListener(this);
            }
        }

    }

    public void LoadButton(View v){
        btnListA[0] = (Button)v.findViewById(R.id.a00);
        btnListA[0].setTag("00");
        btnListA[1] = (Button)v.findViewById(R.id.a01);
        btnListA[1].setTag("01");
        btnListA[2] = (Button)v.findViewById(R.id.a02);
        btnListA[2].setTag("02");
        btnListA[3] = (Button)v.findViewById(R.id.a03);
        btnListA[3].setTag("03");
        btnListA[4] = (Button)v.findViewById(R.id.a04);
        btnListA[4].setTag("04");
        btnListA[5] = (Button)v.findViewById(R.id.a05);
        btnListA[5].setTag("05");
        btnListA[6] = (Button)v.findViewById(R.id.a06);
        btnListA[6].setTag("06");
        btnListA[7] = (Button)v.findViewById(R.id.a07);
        btnListA[7].setTag("07");
        btnListA[8] = (Button)v.findViewById(R.id.a08);
        btnListA[8].setTag("08");
        btnListA[9] = (Button)v.findViewById(R.id.a09);
        btnListA[9].setTag("09");
        btnListA[10] = (Button)v.findViewById(R.id.a10);
        btnListA[10].setTag("10");
        btnListA[11] = (Button)v.findViewById(R.id.a11);
        btnListA[11].setTag("11");
        btnListA[12] = (Button)v.findViewById(R.id.a12);
        btnListA[12].setTag("12");
        btnListA[13] = (Button)v.findViewById(R.id.a13);
        btnListA[13].setTag("13");
        btnListA[14] = (Button)v.findViewById(R.id.a14);
        btnListA[14].setTag("14");
        btnListA[15] = (Button)v.findViewById(R.id.a15);
        btnListA[15].setTag("15");
        btnListA[16] = (Button)v.findViewById(R.id.a16);
        btnListA[16].setTag("16");
        btnListA[17] = (Button)v.findViewById(R.id.a17);
        btnListA[17].setTag("17");
        btnListA[18] = (Button)v.findViewById(R.id.a18);
        btnListA[18].setTag("18");
        btnListA[19] = (Button)v.findViewById(R.id.a19);
        btnListA[19].setTag("19");



    }

    @Override
    public void onClick(View v)
    {
        // 클릭된 뷰를 버튼으로 받아옴
        Button newButton = (Button) v;

        // 향상된 for문을 사용, 클릭된 버튼을 찾아냄
        for(Button tempButton : btnListA)
        {
            // 클릭된 버튼을 찾았으면
            if(tempButton == newButton)
            {
                // 위에서 저장한 버튼의 포지션을 태그로 가져옴
                selectedSeatNum = (String)v.getTag();
                // 태그로 가져온 포지션을 이용해 리스트에서 출력할 데이터를 꺼내서 토스트 메시지 출력
                mCustomDialog = new CustomDialog(ShowingMapActivity.this,
                        "예약 확인", // 제목
                        "[ "+selectedSeatNum+" ] 자리를 예약하시겠습니까?", // 내용
                        leftListener, // 왼쪽 버튼 이벤트
                        rightListener); // 오른쪽 버튼 이벤트
                mCustomDialog.show();
            }
        }


    }

    private View.OnClickListener leftListener = new View.OnClickListener() {
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), selectedSeatNum,
                    Toast.LENGTH_SHORT).show();
            mCustomDialog.dismiss();
            int sectionNum =0;
            switch (selectedSeatNum.charAt(0)){
                case 'a':
                    sectionNum=1;
                    break;
                case 'b':
                    sectionNum=2;
                    break;
                case 'c':
                    sectionNum = 3;
                    break;
                case 'd':
                    sectionNum = 4;
                    break;
            }
            int seatNum = Character.getNumericValue(selectedSeatNum.charAt(1))*10 + Character.getNumericValue(selectedSeatNum.charAt(2));
            //Log.d("KKK", String.valueOf(sectionNum)+" + "+String.valueOf(seatNum));

            ((App)getApplicationContext()).ma.startService(selectedSeatNum);
            finish();
        }
    };

    private View.OnClickListener rightListener = new View.OnClickListener() {
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "취소",
                    Toast.LENGTH_SHORT).show();
            mCustomDialog.dismiss();
        }
    };


}


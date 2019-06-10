package com.example.hars;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.hars.R;

public class Message extends Activity {

    Button okBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.message);

        okBtn=(Button)findViewById(R.id.okBtn);

    }
    public void mOnClose(View v){
        finish();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }//바깥레이어 클릭시 닫기 막기

    @Override
    public void onBackPressed(){
        return;
    }

}


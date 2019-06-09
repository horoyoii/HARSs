package com.example.hars.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.hars.Application.App;
import com.example.hars.MainActivity;
import com.example.hars.Models.User;
import com.example.hars.Observer;
import com.example.hars.R;
import com.example.hars.Util.FirebaseUtil;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.HashMap;

import static com.example.hars.Application.App.CHANNEL_ID;

public class MyService extends Service implements BeaconConsumer { // TODO : implement Observer
    public static final int EMPTY_RANGE = 3;
    public static final String ACTION_COUNTER_RES = "ACTION_COUNTER_RES";
    public static final String ACTION_COUNTER_USE = "ACTION_COUNTER_USE";
    private enum DISTANCE{
        IMMEDIATE, NEAR, FAR
    }

    private static final String TAG = "HHH";
    private int major, minor;
    private User.Status_user status;
    private DISTANCE distance;
    private BeaconManager beaconManager;
    private boolean isOutofRange;
    private int threadhold;
    private int usingtime;
    private User mUser;
    private FirebaseUtil firebaseUtil;
    private int Limit_using_time;
    private int Limit_reserving_time;
    private int Limit_empty_time;
    private FirebaseUtil mFirebaseUtil;
    HashMap<User.Status_user, Notification> Notify;
    public static int point =-40;

    public MyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("HHH", "Service onCreate");
        Init();
        Limit_using_time = 10;
        Limit_reserving_time = 40;
        Limit_empty_time = 10;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null) {
            major = intent.getIntExtra("major", 0);
            minor = intent.getIntExtra("minor", 0);
        }
        setStatus(User.Status_user.RESERVING);
        // do heavy work on a background THREAD
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        logg("call onDestroy");
        super.onDestroy();
        beaconManager.unbind(this);

    }


    // ================================================================================================



    private void setStatus(User.Status_user to){
        stopForeground(true);
        switch (to){
            case RESERVING:
                Log.d("HHH", "called in setstatus reserving");
                startForeground(1,Notify.get(User.Status_user.RESERVING));
                break;
            case OCCUPYING:
                Log.d("HHH", "called in setstatus occupying");
                startForeground(1,Notify.get(User.Status_user.OCCUPYING));
                distance = DISTANCE.IMMEDIATE;
                status = User.Status_user.OCCUPYING;
                firebaseUtil.getThisUserRef().child("status").setValue(User.Status_user.OCCUPYING);
                break;
            case STEPPING_OUT:
                startForeground(1,Notify.get(User.Status_user.STEPPING_OUT));
                distance = DISTANCE.NEAR;
                status = User.Status_user.STEPPING_OUT;
                firebaseUtil.getThisUserRef().child("status").setValue(User.Status_user.STEPPING_OUT);
                break;
        }
    }


    private void sendReservingTime(int threadhold){
        Intent intent = new Intent(ACTION_COUNTER_RES);
        intent.putExtra("msg", threadhold);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendUsingTime(int usingtime){
        Intent intent = new Intent(ACTION_COUNTER_USE);
        intent.putExtra("msg", usingtime);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    @Override
    public void onBeaconServiceConnect() {
        beaconManager.removeAllRangeNotifiers();
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                // The callback for these APIs is didRangeBeaconsInRegion(Region region,
                // Collection<Beacon>)which gives you a list of every beacon matched in the last scan interval.
                // unbind 전까지 1초에 한번씩 호출된다.
                threadhold++;
                usingtime++;
                //TODO: 향후에 추가

//                logg("현재 상태 : "+String.valueOf(status));
//                logg("threadhold : "+String.valueOf(threadhold));
//                logg("usingtime : "+String.valueOf(usingtime));

                if(status == User.Status_user.RESERVING) {
                    sendReservingTime(threadhold);
                    if (threadhold >= Limit_reserving_time) {
                        // 예약 타임 아웃
                        firebaseUtil.getThisUserRef().child("status").setValue(User.Status_user.RESERVING_OVER);
                        stopForeground(true);
                        stopSelf();
                    }
                }else{
                    sendUsingTime(usingtime);
                }

                if (beacons.size() > 0) {
                    for (Beacon beacon : beacons) {
                        //Log.i(TAG, String.valueOf(beacon.getId2())+" and "+String.valueOf(beacon.getId3()));

                        if(major == beacon.getId2().toInt() && minor == beacon.getId3().toInt()){
                            //Log.i(TAG, "The Target beacon I see is about "+beacons.iterator().next().getDistance()+" meters away.");
                            if(status == User.Status_user.RESERVING){// 예약 -> 시작직전의 상황
                                if(beacon.getDistance() < EMPTY_RANGE) {
                                    setStatus(User.Status_user.OCCUPYING);
                                    usingtime = 0;
                                }
                            }else{ // 시작 후의 상황
                                switch (status){
                                    case OCCUPYING:
                                        if(beacon.getDistance() > EMPTY_RANGE && threadhold > 4){ //
                                            setStatus(User.Status_user.STEPPING_OUT);
                                            threadhold = 0;
                                        }
                                        if(beacon.getDistance() < EMPTY_RANGE){
                                            threadhold = 0;
                                        }
                                        break;
                                    case STEPPING_OUT:
                                        if(beacon.getDistance() < EMPTY_RANGE){
                                            threadhold = 0;
                                            setStatus(User.Status_user.OCCUPYING);
                                        }
                                        // 비움 초과 시
                                        if(threadhold >= Limit_empty_time){
                                            //mUser.user_step_out_over();
                                            firebaseUtil.getThisUserRef().child("status").setValue(User.Status_user.STEPPING_OUT_OVER);
                                            //mUser.user_step_out_over();
                                            point-=10;
                                            stopForeground(true);
                                            stopSelf();
                                        }
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        });

        beaconManager.addMonitorNotifier(new MonitorNotifier() {

            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time!");
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon");
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: " + state);
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));

        } catch (RemoteException e) {
        }


        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }

    private void Init(){
        // 1) init beaconManager
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);

        // 2) init Notifications
        Notify = new HashMap<>();
        Intent notificationIntent = new Intent(this, MainActivity.class); // Notif 눌렀을 때 돌아갈 activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notify.put(User.Status_user.RESERVING, new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("예약중")
                .setContentText("에약중입니다.")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build());
        Notify.put(User.Status_user.OCCUPYING, new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("사용중")
                .setContentText("사용중입니다.")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build());

        Notify.put(User.Status_user.STEPPING_OUT, new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("자리비움")
                .setContentText("자리비움중입니다.")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build());

        // 3) init fields
        distance = DISTANCE.FAR;
        threadhold = 0;
        usingtime = 0;
        status = User.Status_user.RESERVING;
        mFirebaseUtil = new FirebaseUtil();
        //mUser = new User(mFirebaseUtil);
        firebaseUtil = ((App)getApplication()).getFirebaseUtil();


        //Limit_reserving_time = Singleton.getInstance().getLimitsReserving();
        //Limit_empty_time = Singleton.getInstance().getLimitsStepOut();
    }



    // =============================================================================================
    // 디버깅 용
    private void logg(String msg){
        Log.d(TAG, msg);
    }


}


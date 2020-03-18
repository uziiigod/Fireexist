package com.carryhjr.youfang;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );

        wendu = (TextView) wendu.findViewById (R.id.wendu);
        shidu = (TextView) shidu.findViewById (R.id.shidu);
        getData();
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void notifyFire() {
        NotificationManager manager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(MainActivity.this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("火灾预警");
        builder.setContentTitle("火灾预警");
        builder.setContentText("主人家中失火啦");
        builder.setWhen(System.currentTimeMillis()); //发送时间

        long[] vibrates = {0, 1000, 0, 1000,0,2000};
        builder.setVibrate(vibrates);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        Notification notification = builder.build();
        manager.notify(1, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void notifyThief() {
        NotificationManager manager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder ( MainActivity.this );
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("失窃预警");
        builder.setContentTitle("失窃预警");
        builder.setContentText("主人家中有生人闯入啦");
        builder.setWhen(System.currentTimeMillis()); //发送时间

        long[] vibrates = {0, 1000, 0, 1000,0,2000};
        builder.setVibrate(vibrates);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        Notification notification = builder.build();
        manager.notify(2, notification);
    }



    private TextView wendu,shidu;
    public static final int SHOW_RESPONSE = 0;
    private Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE:
                    refresh();

            }
        }
    };
    private String data;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void refresh() {

        String[] str = data.split(",");
        if ((str[2].indexOf('1') != -1) && flagFire) {
            notifyFire();
        }
        if ((str[3].indexOf('1') != -1) && flagThief) {
            notifyThief();
        }

        wendu.setText(str[0]);
        shidu.setText(str[1]);
    }

    public void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket client = new Socket("2549816450qq.e2.luyouxia.net", 28166);
                    BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    PrintWriter pw = new PrintWriter(client.getOutputStream(),true);
                    pw.println("shouji");
                    pw.flush();
                    while(true) {
                        String str = br.readLine();
                        if (str.indexOf(',') != -1) {
                            data = str;
                        }

                        Message message = new Message();
                        message.what = SHOW_RESPONSE;
                        // // 将服务器返回的结果存放到Message中
                        handler.sendMessage(message);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private ToggleButton thief,fire;
    private boolean flagThief,flagFire;
    private void initView() {
        flagFire = true;
        flagThief = true;

        thief = (ToggleButton) thief.findViewById(R.id.thief); //获取到控件
        thief.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    flagThief = true;
                }else{
                    flagThief = false;
                }
            }
        });
        fire = (ToggleButton) fire.findViewById(R.id.fire); //获取到控件
        fire.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    flagFire = true;
                }else{
                    flagFire = false;
                }
            }
        })
    }
}
package com.example.online_sduwifiverify;

import java.io.IOException;
import java.util.zip.Inflater;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class pushmessage extends Service{

	//private static final String TAG = "MyService";  
	private WifiAdmin mWifi;
	String[] m={"QLSC_STU","QLSC_IPV6","QLSC","sdu","ipv6_only","google","4over6"};  
    
  
    @Override  
    public IBinder onBind(Intent arg0) {  
        return null;  
    }  
  
    @Override  
    public void onCreate() {  
        Log.i("tag", "onCreate");
        mWifi = new WifiAdmin(pushmessage.this); 
        boolean flag=wificheak();
        if(flag)pingtest();
        //Toast.makeText(this, "show media player", Toast.LENGTH_SHORT).show();  
      //广播服务注册
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);

        
        
    }  
  
    @Override  
    public void onDestroy() {  
        Log.i("TAG", "onDestroy"); 
        super.onDestroy();
        unregisterReceiver(mReceiver);

        //Toast.makeText(this, "stop media player", Toast.LENGTH_SHORT);  
        
    }  
  
    @Override  
    public void onStart(Intent intent, int startId) {  
        Log.i("TAG", "onStart");  
        boolean flag=wificheak();
        if(flag)pingtest();
        new Thread(new MyThread()).start();
        
    }
    
  
	public void pingtest() {
    	//MainActivity.showzt.setText("worong");
    	NotificationManager barManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    	
    	Runtime run = Runtime.getRuntime(); 
    	Process proc = null; 
    	try { 
    	String str = "ping -c 1 -i 0.2 -W 1 "+ "202.194.15.22"; 
    	//System.out.println(str); 
    	proc = run.exec(str); 
    	int result = proc.waitFor(); 
    	if(result == 0) 
    	{ 
	
//    		Notification notification = new Notification();  
//            notification.icon = R.drawable.ic_launcher;  
//            notification.tickerText = "可以上网了~"; 
//            //Intent intent = new Intent(this, MainActivity.class);  
//           // PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);  
//            // 点击状态栏的图标出现的提示信息设置   
//           // notification.setLatestEventInfo(this, "pass", "轻触以打开软件进行认证", pendingIntent);  
//            notification.flags|=Notification.FLAG_AUTO_CANCEL;
           // barManager.notify(1, notification); 
            barManager.cancel(0);


    	Log.i("ping", "pass");
    	} 
    	else 
    	{ 
    	//Toast.makeText(pushmessage.this, "ping测试失败", Toast.LENGTH_SHORT).show(); 
    		Notification notification = new Notification();  
            // 设置显示在手机最上边的状态栏的图标   
            notification.icon = R.drawable.ic_launcher;  
            notification.tickerText = "wifi需要认证";  
            Intent intent = new Intent(this, MainActivity.class);  
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);  
            // 点击状态栏的图标出现的提示信息设置   
            notification.setLatestEventInfo(this, "Wi-Fi需要认证", "轻触以打开软件进行认证", pendingIntent);  
            //notification.FLAG_AUTO_CANCEL;
            notification.flags|=Notification.FLAG_AUTO_CANCEL;
            barManager.notify(0, notification); 
    	Log.i("ping", "false");
    	} 
    	} catch (IOException e) { 
    	e.printStackTrace(); 
    	} catch (InterruptedException e) { 
    	e.printStackTrace(); 
    	} finally { 
    	proc.destroy(); 
    	} 
    	
    	} 
	
      public boolean wificheak(){
		String ssid=mWifi.getWifiInfo();
		int xx=-1;
		int time=0;
		while(xx==-1){
			
		 xx=ssid.indexOf(m[time]);
		 time++;
		 if(time>6)break;
		}
		Log.i("wificheak-pushmessage", xx+""+ssid);
		if(xx==-1)return false;
		else return true;
	}
    
   //===
      

      private BroadcastReceiver mReceiver = new BroadcastReceiver() {

          @Override
          public void onReceive(Context context, Intent intent) {
              String action = intent.getAction();
              if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                  Log.d("mark", "网络状态已经改变");
                  State wifiState = null;  
                  State mobileState = null;  
                  ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
                  wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();  
                  mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();  
                  if (wifiState != null && mobileState != null  
                          && State.CONNECTED != wifiState  
                          && State.CONNECTED == mobileState) {  
                      // 手机网络连接成功   
                  } else if (wifiState != null && mobileState != null  
                          && State.CONNECTED != wifiState  
                          && State.CONNECTED != mobileState) {  
                      // 手机没有任何的网络   
                  } else if (wifiState != null && State.CONNECTED == wifiState) {  
                      // 无线网络连接成功   
                	  boolean flag=wificheak();
                      if(flag)pingtest();
                  }  

                  
                  
                  
              }
          }

	
      };

      //===
      Handler handler = new Handler() { 
    	    public void handleMessage(Message msg) { 
    	    	Log.i("time", "ok");
    	    	boolean flag=wificheak();
                if(flag)pingtest();
    	        super.handleMessage(msg); 
    	    } 
    	};
    	public class MyThread implements Runnable { 
    	    @Override
    	    public void run() { 
    	        // TODO Auto-generated method stub 
    	        while (true) { 
    	            try { 
    	                Thread.sleep(300000);// 线程暂停300秒，单位毫秒 
    	                //Thread.sleep(10000);// 线程暂停10秒，单位毫秒 
    	                Message message = new Message(); 
    	                message.what = 1; 
    	                handler.sendMessage(message);// 发送消息 
    	            } catch (InterruptedException e) { 
    	                // TODO Auto-generated catch block 
    	                e.printStackTrace(); 
    	            } 
    	        } 
    	    } 
    	}

}

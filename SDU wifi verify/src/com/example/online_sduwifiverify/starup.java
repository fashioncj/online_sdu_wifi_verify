package com.example.online_sduwifiverify;

import android.content.BroadcastReceiver;  
import android.content.Context;  
import android.content.Intent;  
import android.util.Log;  
  
public class starup extends BroadcastReceiver {  
    //��дonReceive����  
    @Override  
    public void onReceive(Context context, Intent intent) {  
        //��ߵ�XXX.class����Ҫ�����ķ���  
        Intent service = new Intent(context,pushmessage.class);  
        context.startService(service);  
        Log.v("TAG", "�����Զ������Զ�����.....");  
        //����Ӧ�ã�����Ϊ��Ҫ�Զ�������Ӧ�õİ���
         //Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);

       //context.startActivity(intent );        
    }  
  
}  

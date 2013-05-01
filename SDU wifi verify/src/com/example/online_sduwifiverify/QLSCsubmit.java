package com.example.online_sduwifiverify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class QLSCsubmit extends Activity implements Runnable {
	
	Thread submitmessage;
	String usernameString="";
	String passwordString="";
	String ipaddressString="";
	long nowtime;
	Handler h;
	//TextView zt;
	//Inflater 
	
	
	
	
   public void submit_qlsc_stu(){
//	   LayoutInflater inflater = getLayoutInflater();
//	   View view = inflater.inflate(R.layout.activity_main, null);
//	   TextView zt=(TextView)view.findViewById(R.id.zhuangtai);
	   
	  HttpClient qlsc_stuhttpclient=new DefaultHttpClient();
	  HttpPost qlsc_stuHttpPost=new HttpPost("http://192.168.8.10/portal/login.jsp?Flag=0");
	  List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
	  //nameValuePairs.add(new BasicNameValuePair("wd", "value"));
	  nameValuePairs.add(new BasicNameValuePair("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)"));

		nameValuePairs.add(new BasicNameValuePair("username", usernameString));
		nameValuePairs.add(new BasicNameValuePair("password", passwordString));
		nameValuePairs.add(new BasicNameValuePair("isSavePass","on"));
		nameValuePairs.add(new BasicNameValuePair("Language","Chinese"));
		nameValuePairs.add(new BasicNameValuePair("timeoutvalue","45"));
		nameValuePairs.add(new BasicNameValuePair("heartbeat","240"));
		nameValuePairs.add(new BasicNameValuePair("fastwebornot","false"));
		nameValuePairs.add(new BasicNameValuePair("shkOvertime","7200"));
		nameValuePairs.add(new BasicNameValuePair("iIPCONFIG","0"));
		nameValuePairs.add(new BasicNameValuePair("title","CAMS+Portal"));
		//nameValuePairs.add(new BasicNameValuePair("sessionID","9182157776730962812"));
		nameValuePairs.add(new BasicNameValuePair("strOldPrivateIP",ipaddressString));
		nameValuePairs.add(new BasicNameValuePair("strOldPublicIP",ipaddressString));
		nameValuePairs.add(new BasicNameValuePair("strPrivateIP",ipaddressString));
		nameValuePairs.add(new BasicNameValuePair("PublicIP",ipaddressString));
		nameValuePairs.add(new BasicNameValuePair("sHttpPrefix","http%3A%2F%2F192.168.8.10"));
		Date d = new Date();
		long time1 = d.getTime();
		String time=time1+"";
		nameValuePairs.add(new BasicNameValuePair("StartTime", time));
	  try {
		qlsc_stuHttpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  HttpResponse qlsc_stuHttpResponse;
	  try {
		qlsc_stuHttpResponse=qlsc_stuhttpclient.execute(qlsc_stuHttpPost);
		int qlsc_stuString=qlsc_stuHttpResponse.getStatusLine().getStatusCode();
		//获取返回网页
		
				HttpEntity entity = qlsc_stuHttpResponse.getEntity();
				InputStream is = entity.getContent();
				String result="";
				try{
			        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"GB2312"));
			        StringBuilder sb = new StringBuilder();
			        String line = null;
			        while ((line = reader.readLine()) != null) {
			            sb.append(line + "\n");
			        }
			        is.close();

			         result=sb.toString();
			    }catch(Exception e){
			    	 result="Fail to convert net stream!";
			    }
				
				 Log.i("res", result);
					Log.i("post",qlsc_stuString+"");
					resulttest(result);
			 

       
	} catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
   }
   

	public void qlsc_stu_tryit(String username,String password,String ipaddress,Handler handler){
		usernameString=username;
		passwordString=password;
		ipaddressString=ipaddress;
		Date nowDate=new Date();
		nowtime=nowDate.getTime();
		submitmessage=new Thread(this);
		submitmessage.start();
		this.h=handler;
//		LayoutInflater inflater = getLayoutInflater();
//		View view = inflater.inflate(R.layout.activity_main, null);
//		zt=(TextView)view.findViewById(R.id.zhuangtai);
		
	}
	

		public void run(){
			try {
				submit_qlsc_stu();
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	
	
	public void resulttest(String result) {
		//boolean tf=wifiAdmin.pingtest();
		//boolean tf=true;
		//Log.i("1","1");
		//NotificationManager barManager1=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		//Log.i("1","1");
		//if(tf){
		//	Log.i("2","2");
			//Notification notification = new Notification();  
            //notification.icon = R.drawable.ic_launcher;  
            //notification.tickerText = "可以上网了~";  
           // barManager1.notify(2, notification); 
            //
		if(result.indexOf("用户上线成功")>0){
            Message msg = new Message();
            Bundle b = new Bundle();// 存放数据
            b.putString("color", "ok");
            msg.setData(b);
           // MainActivity.this.handler.sendMessage(msg); // 向Handler发送消息,更新UI
            h.sendMessage(msg);
		   }
            //
           // zt.setText("登录成功");
		//}
		//else {
		//	Log.i("3","3");
			//Notification notification = new Notification();  
            //notification.icon = R.drawable.ic_launcher;  
           // notification.tickerText = "认证失败";  
           // barManager1.notify(2, notification); 
           // zt.setText("登录成功");
			
		//}
	if(result.indexOf("您已经建立了连接")>0){
		Log.i("copy", "111");
		Message msg = new Message();
        Bundle b = new Bundle();// 存放数据
        b.putString("color", "ok");
        msg.setData(b);
       // MainActivity.this.handler.sendMessage(msg); // 向Handler发送消息,更新UI
        h.sendMessage(msg);
		//zt.setText("登录成功");
	}
	if(result.indexOf("在线用户")>0){
		Message msg = new Message();
        Bundle b = new Bundle();// 存放数据
        b.putString("color", "limit");
        msg.setData(b);
       // MainActivity.this.handler.sendMessage(msg); // 向Handler发送消息,更新UI
        h.sendMessage(msg);
		
		Log.i("copy", "222");
		//zt.setText("在线用户数量限制，登录失败");
	}
	if(result.indexOf("密码错误")>0){
		Message msg = new Message();
        Bundle b = new Bundle();// 存放数据
        b.putString("color", "wrong");
        msg.setData(b);
       // MainActivity.this.handler.sendMessage(msg); // 向Handler发送消息,更新UI
        h.sendMessage(msg);
		
		Log.i("copy", "333");
		//zt.setText("密码错误！");
	}
	if(result.indexOf("您已经建立了宽带上网的连接")>0){
		Message msg = new Message();
        Bundle b = new Bundle();// 存放数据
        b.putString("color", "ok");
        msg.setData(b);
       // MainActivity.this.handler.sendMessage(msg); // 向Handler发送消息,更新UI
        h.sendMessage(msg);
		Log.i("copy", "4444");
	}
		
	}
	
	
}

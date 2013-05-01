package com.example.online_sduwifiverify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


public class QLSCcannel extends Activity implements Runnable {
	
	Thread cannelmessage;
	String usernameString="";
	String passwordString="";
	String ipaddressString="";
	String sessionidString="";
	long nowtime;
	Handler h1;
	
	
   public void cannel_qlsc_stu(){
	  HttpClient qlsc_stuhttpclient=new DefaultHttpClient();
	  HttpPost qlsc_stuHttpPost=new HttpPost("http://192.168.8.10/portal/logout.jsp");
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
//		nameValuePairs.add(new BasicNameValuePair("shkOvertime","240"));
		nameValuePairs.add(new BasicNameValuePair("iIPCONFIG","0"));
		nameValuePairs.add(new BasicNameValuePair("title","CAMS+Portal"));
		nameValuePairs.add(new BasicNameValuePair("submit1","%B6%CF+%BF%AA"));
		nameValuePairs.add(new BasicNameValuePair("strUserPortNo","WLZX-H3CWX5002-0-vlan-01-0024@vlan-SSID-QLSC_STU@SSID"));
		nameValuePairs.add(new BasicNameValuePair("sessionID",sessionidString));
		nameValuePairs.add(new BasicNameValuePair("ClientIP",ipaddressString));
		nameValuePairs.add(new BasicNameValuePair("strOldPublicIP",ipaddressString));
		nameValuePairs.add(new BasicNameValuePair("strOldPrivateIP",ipaddressString));
		nameValuePairs.add(new BasicNameValuePair("strPrivateIP",ipaddressString));
		nameValuePairs.add(new BasicNameValuePair("PublicIP",ipaddressString));
		//nameValuePairs.add(new BasicNameValuePair("sHttpPrefix","http%3A%2F%2F192.168.8.10"));
		Date d = new Date();
		long time1 = d.getTime();
		String time=time1+"";
		nameValuePairs.add(new BasicNameValuePair("StartTime", time));
		nameValuePairs.add(new BasicNameValuePair("iTimeStamp", d.getTime()+""));
		nameValuePairs.add(new BasicNameValuePair("iUserStatus", "99"));
		nameValuePairs.add(new BasicNameValuePair("linkStatues", "1"));
		nameValuePairs.add(new BasicNameValuePair("myaction", "0"));
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
			resultcan(result);
        
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
		cannelmessage=new Thread(this);
		
		getsessionID getse=new getsessionID();
		getse.getstrat("http://192.168.8.10/portal/index_default.jsp?Language=Chinese");
		while(getse.seidString==""){}
		sessionidString=getse.seidString;
		getse.seidString="";
		Log.i("sessionwait", sessionidString);
		cannelmessage.start();
		this.h1=handler;
		
	}
	

		public void run(){
			try {
				cannel_qlsc_stu();
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	
	
	public void resultcan(String result){
		if(result.indexOf("您已经断开了宽带上网的连接")>0){
			Log.i("disconnect", "disconnect");
			Message msg1 = new Message();
	        Bundle b1 = new Bundle();// 存放数据
	        b1.putString("color", "disconnect");
	        msg1.setData(b1);
	       // MainActivity.this.handler.sendMessage(msg); // 向Handler发送消息,更新UI
	        h1.sendMessage(msg1);
			
			
		}
		else{
			Log.i("disconnect", "fault disconnect");
			Message msg1 = new Message();
	        Bundle b1 = new Bundle();// 存放数据
	        b1.putString("color", "fd");
	        msg1.setData(b1);
	       // MainActivity.this.handler.sendMessage(msg); // 向Handler发送消息,更新UI
	        h1.sendMessage(msg1);
			
					}
	}
	
	
}

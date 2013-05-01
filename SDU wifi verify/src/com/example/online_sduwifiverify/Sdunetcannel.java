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
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.os.Handler;
import android.util.Log;

public class Sdunetcannel implements Runnable {

	Thread cannelmessage;
	String usernameString="";
	String passwordString="";
	String ipaddressString="";
	String actionString="";
	Handler h;

	
	
   public void cannel_sdu_net(){
	  HttpClient sdu_nethttpclient=new DefaultHttpClient();
	  HttpPost sdu_netHttpPost=new HttpPost(actionString);
	  List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
	  //nameValuePairs.add(new BasicNameValuePair("wd", "value"));
	  nameValuePairs.add(new BasicNameValuePair("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)"));

		nameValuePairs.add(new BasicNameValuePair("userName", usernameString));
		nameValuePairs.add(new BasicNameValuePair("userPwd", passwordString));
		nameValuePairs.add(new BasicNameValuePair("isSavePaw","on"));
		nameValuePairs.add(new BasicNameValuePair("isQuickAuth","false"));
		//nameValuePairs.add(new BasicNameValuePair("language","Chinese"));
		//nameValuePairs.add(new BasicNameValuePair("userip",ipaddressString));
		nameValuePairs.add(new BasicNameValuePair("heartbeatCyc", "0"));
		nameValuePairs.add(new BasicNameValuePair("serialNo", "-17480"));
		nameValuePairs.add(new BasicNameValuePair("userStatus", "99"));
		nameValuePairs.add(new BasicNameValuePair("userDevPort", "AC-1-vlan-01-0721@vlan"));
		nameValuePairs.add(new BasicNameValuePair("heartBeatTimeoutMaxTime", "0"));

	  try {
		sdu_netHttpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  HttpResponse sdu_netHttpResponse;
	  try {
		sdu_netHttpResponse=sdu_nethttpclient.execute(sdu_netHttpPost);
		int sdu_netString=sdu_netHttpResponse.getStatusLine().getStatusCode();
		//获取返回网页
		
		HttpEntity entity = sdu_netHttpResponse.getEntity();
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
		
		 Log.i("res2", result+"222");
			Log.i("post",sdu_netString+"");
			
	} catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
   }
   

	public void sdu_net_tryit(String username,String password,String ipaddress,Handler handler){
		usernameString=username;
		//password处理
		byte[] result = Base64.encode(password.getBytes());
		//System.out.println(data);
		passwordString=new String(result);
		//System.out.println(new String(Base64.decode(new String(result))));
		//passwordString=password;
		ipaddressString=ipaddress;
		actionString ="http://202.194.15.87/portal/logout.jsp?language=Chinese&userip="+ipaddress;
		getsessionID getse=new getsessionID();
		getse.getstrat("http://202.194.15.87/portal/index_custom.jsp");
		
		this.h=handler;
		cannelmessage=new Thread(this);
		cannelmessage.start();
		
	}
	

		public void run(){
			try {
				cannel_sdu_net();
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	
	
	
}

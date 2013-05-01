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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.R.string;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Sdunetsubmit implements Runnable {

	Thread submitmessage;
	String usernameString="";
	String passwordString="";
	String ipaddressString="";
	Handler h;
	String  url1="";

	
	
   public void submit_sdu_net(){
	  HttpClient sdu_nethttpclient=new DefaultHttpClient();
	  HttpPost sdu_netHttpPost=new HttpPost("http://202.194.15.87/portal/login.jsp");
	  List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
	  //nameValuePairs.add(new BasicNameValuePair("wd", "value"));
	  nameValuePairs.add(new BasicNameValuePair("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)"));

		nameValuePairs.add(new BasicNameValuePair("userName", usernameString));
		nameValuePairs.add(new BasicNameValuePair("userPwd", passwordString));
		nameValuePairs.add(new BasicNameValuePair("isSavePaw","on"));
		nameValuePairs.add(new BasicNameValuePair("isQuickAuth","false"));
		nameValuePairs.add(new BasicNameValuePair("language","Chinese"));
		nameValuePairs.add(new BasicNameValuePair("userip",ipaddressString));

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
		//��ȡ������ҳ
		
		HttpEntity entity = sdu_netHttpResponse.getEntity();
		InputStream is = entity.getContent();
		String result="";
		try{
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
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
		    
		    Log.i("res1", result+"111");
			Log.i("post",sdu_netString+"");
			resulttest(result);
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
		//password����
		//Base64 Basepaw=new Base64();
		byte[] result1 = Base64.encode(password.getBytes());
		//System.out.println(data);
		passwordString=new String(result1);
		Log.i("pwd", passwordString);
		//System.out.println(new String(Base64.decode(new String(result))));
		//passwordString=password;
		ipaddressString=ipaddress;
		this.h=handler;
		//String sduurl="http://202.194.15.87/portal/login.jsp?userName="+usernameString+"&userPwd="+passwordString+"&userip="+ipaddressString+"&language=chinese&isQuickauth=false";
		//url1=sduurl;
		//sdunetget sduget=new sdunetget();
		//sduget.getstrat(sduurl);
		//String answerString=sduget.seidString;
		//Log.i("answer", sduurl);
		getsessionID getse=new getsessionID();
		getse.getstrat("http://202.194.15.87/portal/index_custom.jsp");
		
		submitmessage=new Thread(this);
		submitmessage.start();
		
	}
	

		public void run(){
			try {
				submit_sdu_net();
				//sduhttpget(url1);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	
		public void resulttest(String result) {
			
			if(result.indexOf("�û����߳ɹ�")>0){
	            Message msg = new Message();
	            Bundle b = new Bundle();// �������
	            b.putString("color", "ok");
	            msg.setData(b);
	           // MainActivity.this.handler.sendMessage(msg); // ��Handler������Ϣ,����UI
	            h.sendMessage(msg);
			   }
	          
		if(result.indexOf("���Ѿ�����������")>0){
			Log.i("copy", "111");
			Message msg = new Message();
	        Bundle b = new Bundle();// �������
	        b.putString("color", "ok");
	        msg.setData(b);
	       // MainActivity.this.handler.sendMessage(msg); // ��Handler������Ϣ,����UI
	        h.sendMessage(msg);
			//zt.setText("��¼�ɹ�");
		}
		if(result.indexOf("�����û�")>0){
			Message msg = new Message();
	        Bundle b = new Bundle();// �������
	        b.putString("color", "limit");
	        msg.setData(b);
	       // MainActivity.this.handler.sendMessage(msg); // ��Handler������Ϣ,����UI
	        h.sendMessage(msg);
			
			Log.i("copy", "222");
			//zt.setText("�����û��������ƣ���¼ʧ��");
		}
		if(result.indexOf("����")>0){
			Message msg = new Message();
	        Bundle b = new Bundle();// �������
	        b.putString("color", "wrong");
	        msg.setData(b);
	        h.sendMessage(msg);
			
			Log.i("copy", "333");
			//zt.setText("�������");
		}
		if(result.indexOf("���Ѿ������˿������������")>0){
			Message msg = new Message();
	        Bundle b = new Bundle();// �������
	        b.putString("color", "ok");
	        msg.setData(b);
	        h.sendMessage(msg);
			Log.i("copy", "4444");
		}
			
		}
		
		
            

		
	
}

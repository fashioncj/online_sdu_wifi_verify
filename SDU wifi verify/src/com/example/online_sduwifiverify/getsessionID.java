package com.example.online_sduwifiverify;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpPost;

import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;


public class getsessionID implements Runnable  {
	Thread geturl;
	String url1="";
	static String seidString="";
	public String posturl(String url){
	    InputStream is = null;
	    String result = "";

	    try{
	        HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost(url);
	        HttpResponse response = httpclient.execute(httppost);
	        HttpEntity entity = response.getEntity();
	        is = entity.getContent();
	    }catch(Exception e){
	        return "Fail to establish http connection!"+e.toString();
	    }

	    try{
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
	        StringBuilder sb = new StringBuilder();
	        String line = null;
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	        is.close();

	        result=sb.toString();
	    }catch(Exception e){
	        return "Fail to convert net stream!";
	    }
          result=result.substring(result.indexOf("document.form1.sessionID.value=\""), result.indexOf("document.form1.sessionID.value=\"")+53);
	      result=result.replace("document.form1.sessionID.value=\"", "");
	      result=result.replace("\"", "");
	      result=result.replace(";", "");
	      Log.i("session111", result);
          return result;
	}
	
	public void getstrat(String str) {
		url1=str;
		geturl=new Thread(this);
		geturl.start();
		//return seidString;
	}
	
	
	
	public void run(){
		try {
			seidString=posturl(url1);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	




}
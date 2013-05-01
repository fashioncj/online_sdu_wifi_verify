package com.example.online_sduwifiverify;





import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener; 
 

public class MainActivity extends Activity {
	
	QLSCsubmit formsubmit=new QLSCsubmit();
	
	String user="";
	String paw="";
	Boolean isrem=true;
	//�����˵�
	private static final String[] m={"QLSC_STU","QLSC_IPV6","QLSC","sdu_net","ipv6_only","google","4over6"};  
	int lastsel=0;
  //  private TextView view ;   
    private Spinner spinner;   
    private ArrayAdapter<String> adapter;   
    static TextView showzt;
    Handler handler;
    TextView zTextView;

//wifi
    private WifiAdmin mWifi; 
    String wifichoose="";
   // private WifiManager mWifiManager;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//��һ�μ���ʱ�����Ƿ���������
		if(isrem){
			EditText username = (EditText) findViewById(R.id.snum);        //�༭�����
			EditText password = (EditText) findViewById(R.id.spaw);

	        SharedPreferences preference0 = getSharedPreferences("person",Context.MODE_PRIVATE);

	        username.setText(preference0.getString("user",""));            //preference.getString(��ʾ��,Ĭ��ֵ<����Ϊ��>��
	        password.setText(preference0.getString("paw", ""));
	        
	        SharedPreferences preference4 = getSharedPreferences("wifi",Context.MODE_PRIVATE);
	        lastsel=preference4.getInt("wifikey",0);
		}
		//��������
		
		//����
		//view = (TextView) findViewById(R.id.spinnerText);   
        spinner = (Spinner) findViewById(R.id.spinner1);   
        //����ѡ������ArrayAdapter��������   
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);   
           
        //���������б�ķ��   
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   
           
        //��adapter ��ӵ�spinner��   
        spinner.setAdapter(adapter);  
        spinner.setSelection(lastsel);
           
        //����¼�Spinner�¼�����     
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());   
           
        //����Ĭ��ֵ   
        spinner.setVisibility(View.VISIBLE); 
		//��ȡCheckBoxʵ��
		CheckBox rpaw = (CheckBox)this.findViewById(R.id.rememberpaw);
		rpaw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { 
            
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { 
               // TODO Auto-generated method stub 
               if(isChecked){ 
                   Toast.makeText(MainActivity.this,"��ס����", Toast.LENGTH_LONG).show();
                   isrem=true;
                   rememberinput();
               } 
               else{
            	   Toast.makeText(MainActivity.this,"ȡ����ס����", Toast.LENGTH_LONG).show(); 
            	   isrem=false;
            	   SharedPreferences preference2 = getSharedPreferences("person",Context.MODE_PRIVATE);
                   Editor edit = preference2.edit();
                   edit.putString("user","");      
                   edit.putString("paw","");
                   edit.commit();
            	   
               }
               
           } 
       });
		mWifi = new WifiAdmin(MainActivity.this);  
		//mWifiManager =new WifiManager();
		init();
		//service
		Intent intent=new Intent(MainActivity.this,pushmessage.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startService(intent);
	}

	private void init() {
		Button submit=(Button)findViewById(R.id.connect);
		submit.setOnClickListener(sub);
		Button cancle=(Button)findViewById(R.id.logout);
		cancle.setOnClickListener(can);
		TextView showzt=(TextView) findViewById(R.id.zhuangtai);
		handler = new MyHandler();
		pingtest();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case R.id.item1:
			openOptionsDialog();
			break;
		case R.id.item2:
			finish();
			break;
//		case R.id.accept:
//			weibo();
//			break;
		default:
			//openOptionsDialog();
			break;
		}
		return super.onOptionsItemSelected(item);
	}	 
	
	private void openOptionsDialog(){
		 new AlertDialog.Builder(MainActivity.this)
		 .setTitle("����ɽ����ѧ���ߵ�¼��")
		 .setMessage("ѧ�����߳�Ʒ\n" +
		 		"�ڲ�汾0.9.5").show();
	 }
	
	public void pingtest() {
		boolean tf=mWifi.pingtest();
		TextView tw=(TextView) findViewById(R.id.zhuangtai);
		if(tf){
			tw.setText("��������");
		}
		else{
			tw.setText("����ʧ��");
		}
	}

	public void rememberinput() {
		EditText username = (EditText) findViewById(R.id.snum);        //�༭�����
		EditText password = (EditText) findViewById(R.id.spaw);

         user = username.getText().toString();              
         paw = password.getText().toString();

        SharedPreferences preference = getSharedPreferences("person",Context.MODE_PRIVATE);

        Editor edit = preference.edit();

        edit.putString("user",user);      

        edit.putString("paw",paw);

        edit.commit();
	}
	
	class SpinnerSelectedListener implements OnItemSelectedListener{   
		  
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,   
                long arg3) {   
         
        	Toast.makeText(MainActivity.this,"��ѡ�����������Ϊ"+m[arg2], Toast.LENGTH_SHORT).show();
        	wifichoose=m[arg2];
        	SharedPreferences preference3 = getSharedPreferences("wifi",Context.MODE_PRIVATE);
            Editor edit = preference3.edit();
            edit.putInt("wifikey", arg2)    ; 
            
            edit.commit();
        }   
  
        public void onNothingSelected(AdapterView<?> arg0) {   
        }   
    } 
	
	
     public boolean wifitf(){
    	 int state=mWifi.checkState();
     	//Toast.makeText(MainActivity.this,"��ѡ�����������Ϊ"+mWifi.checkState(), Toast.LENGTH_SHORT).show();
    	 switch (state) {
		case 0:{
			Toast.makeText(MainActivity.this,"disable ing", Toast.LENGTH_SHORT).show();
			break;
		}
		case 1:{
			Toast.makeText(MainActivity.this,"disable ", Toast.LENGTH_SHORT).show();
			break;
		}
		case 2:{
			Toast.makeText(MainActivity.this,"enable ing", Toast.LENGTH_SHORT).show();
			break;
		}
		case 3:{
			//Toast.makeText(MainActivity.this,"enable", Toast.LENGTH_SHORT).show();
			boolean tf=wificheck();
			if(tf==true)return true;
			
			break;
		}
		case  4:{
			Toast.makeText(MainActivity.this,"unknown", Toast.LENGTH_SHORT).show();
			break;
		}
		default:
			break;
		}
    	 return false;
     }
     private String intToIp(int i)  {
		   
   	   return (i & 0xFF)+ "." + ((i >> 8 ) & 0xFF)+ "." + ((i >> 16 ) & 0xFF) +"."+((i >> 24 ) & 0xFF );
   		}  
     
     public boolean wificheck() {
    	String bssid= mWifi.getBSSID();
    	//String ssid=mWifiManager.
    	String ssid=mWifi.getWifiInfo();
    	//Toast.makeText(MainActivity.this, ssid,Toast.LENGTH_LONG).show();
    	int networdid=mWifi.getNetWordId();
    	String ipaddress=intToIp(mWifi.getIpAddress());
    	String macaddress= mWifi.getMacAddress();
    	int xx;
    	xx=ssid.indexOf(wifichoose);
    	Log.i("wificheck", bssid+"\n"+wifichoose+"\n"+networdid+"\n"+ipaddress+"\n"+macaddress+"\n"+ssid);
    	Log.i("wifi", xx+"");
    	if(xx!=-1)return true;
    	else{
    		String[] ary = ssid.split(",");
    		CharSequence worry =ary[0].subSequence(6, ary[0].length());
    		Log.i("wifi", worry+"");
    		comparewifissid(worry);
    		
    	}
    	 
    	return false;
	}
     
     private void comparewifissid(CharSequence worry) {
		String com=worry.toString();
		if(com.compareTo(m[0])==0||com.compareTo(m[1])==0||com.compareTo(m[2])==0||com.compareTo(m[3])==0||com.compareTo(m[4])==0||com.compareTo(m[5])==0){
			Toast.makeText(MainActivity.this, "��ѡ��WiFiѡ��:"+com,Toast.LENGTH_LONG).show();
		}
		else if (com.compareTo("<none>")==0) {
			Toast.makeText(MainActivity.this, "wifi�źŲ��ѻ���û������Wi-Fi"+com,Toast.LENGTH_LONG).show();
		}
		else{
			Toast.makeText(MainActivity.this, "������У԰����Χ�ڣ�����wifi����ѡ��Ϊ��"+com,Toast.LENGTH_LONG).show();
		}
		
	}

	private OnClickListener sub =new OnClickListener(){
 	    public void onClick(View v){
 	    	boolean isok=wifitf();
 	    	if(isrem)rememberinput();
	 	    	else{
	 	    		EditText username = (EditText) findViewById(R.id.snum);        //�༭�����
	 	   		    EditText password = (EditText) findViewById(R.id.spaw);

	 	            user = username.getText().toString();              
	 	            paw = password.getText().toString();
	 	    	}
 	    	if(user.length()==0){
 	    		Toast.makeText(MainActivity.this, "������ѧ��",Toast.LENGTH_SHORT).show();
 	    		isok=false;
 	    	}
 	    	if(paw.length()==0){
 	    		Toast.makeText(MainActivity.this, "����������",Toast.LENGTH_SHORT).show();
 	    		isok=false;
 	    	}
 	    	if(isok){//wifi״̬ok��ssidok.
 	    		String ipaddress=intToIp(mWifi.getIpAddress());
 	    		Log.i("ip?", ipaddress);
 	    		if(ipaddress.substring(0, 2)=="169"||ipaddress.indexOf("255")>=0){
 	    			Toast.makeText(MainActivity.this, "�õ���wifi��������ȴ���", Toast.LENGTH_LONG).show();
 	    		}
 	    		else{
	 	    		Log.i("login", user+" "+paw+" "+wifichoose+ipaddress);
	 	    		int way=-1;
	 	    		if(wifichoose=="QLSC_STU"||wifichoose=="QLSC")way=0;
	 	    		if(wifichoose=="QLSC_IPV6")way=1;
	 	    		if(wifichoose=="sdu_net")way=2;
	 	    		if(wifichoose=="ipv6_only"||wifichoose=="4over6")way=3;
	 	    		switch (way) {
					case 0:
						{
							ztshow(1);
							formsubmit.qlsc_stu_tryit(user,paw,ipaddress,handler);
						break;}
					case 1:{
						Toast.makeText(MainActivity.this, "�����粻��Ҫ��֤��������", Toast.LENGTH_SHORT).show();
						break;
					}
					case 2:{
						ztshow(1);
						Log.i("sdu-net", "pass");
						Sdunetsubmit sdunetsubmit=new Sdunetsubmit();
						sdunetsubmit.sdu_net_tryit(user, paw, ipaddress,handler);
						break;
					}
					case 3:{
						Toast.makeText(MainActivity.this, "������Ŀ�⻹����������", Toast.LENGTH_SHORT).show();
						break;
					}
					default:
						break;
					}
	 	    		}
	 	    	
 	    	}
 	    	
 	    	
 	    	
 	    }};
 	   
 	  
 	    
 	   private OnClickListener can =new OnClickListener(){
 	 	    public void onClick(View v){
 	 	    	boolean isok=wifitf();
 	 	    	
 	 	    	if(isrem)rememberinput();
 	 	    	else{
 	 	    		EditText username = (EditText) findViewById(R.id.snum);        //�༭�����
	 	   		    EditText password = (EditText) findViewById(R.id.spaw);

	 	            user = username.getText().toString();              
	 	            paw = password.getText().toString();
 	 	    	}
 	 	    	if(user.length()==0){
 	 	    		Toast.makeText(MainActivity.this, "������ѧ��",Toast.LENGTH_SHORT).show();
 	 	    		isok=false;
 	 	    	}
 	 	    	if(paw.length()==0){
 	 	    		Toast.makeText(MainActivity.this, "����������",Toast.LENGTH_SHORT).show();
 	 	    		isok=false;
 	 	    	}
 	 	    	if(isok){//wifi״̬ok��ssidok.
 	 	    		
 	 	    		String ipaddress=intToIp(mWifi.getIpAddress());
 	 	    		if(ipaddress.substring(0, 2)=="169"||ipaddress.indexOf("255")>=0){
 	 	    			Toast.makeText(MainActivity.this, "�õ���wifi��������ȴ���", Toast.LENGTH_LONG).show();
 	 	    		}
 	 	    		else{
 		 	    		Log.i("login", user+" "+paw+" "+wifichoose+ipaddress);
 		 	    		int way=-1;
 		 	    		if(wifichoose=="QLSC_STU"||wifichoose=="QLSC")way=0;
 		 	    		if(wifichoose=="QLSC_IPV6")way=1;
 		 	    		if(wifichoose=="sdu_net")way=2;
 		 	    		if(wifichoose=="ipv6_only"||wifichoose=="4over6")way=3;
 		 	    		switch (way) {
 						case 0:
 							{   
 								ztshow(2);
 								QLSCcannel qlsccannel=new QLSCcannel();
 								qlsccannel.qlsc_stu_tryit(user,paw,ipaddress,handler);
 							
 							break;}
 						case 1:{
 							Toast.makeText(MainActivity.this, "�����粻��Ҫ��֤��������", Toast.LENGTH_SHORT).show();
 							break;
 						}
 						case 2:{
 							ztshow(2);
 							//Sdunetsubmit sdunetsubmit=new Sdunetsubmit();
 							//sdunetsubmit.sdu_net_tryit(user, paw, ipaddress);
 							Sdunetcannel sdunetcannel=new Sdunetcannel();
 							sdunetcannel.sdu_net_tryit(user, paw, ipaddress, handler);
 							break;
 							
 						}
 						case 3:{
 							Toast.makeText(MainActivity.this, "������Ŀ�⻹����������", Toast.LENGTH_SHORT).show();
 							break;
 						}
 						default:
 							break;
 						}
 		 	    		}
 	 	    	
 	 	    	}
 	 	    	
 	 	    }};
 	 	    
 	 	    void ztshow(int i){
 	 	    	 zTextView=(TextView)findViewById(R.id.zhuangtai);
 	 	    	if(i==1){
 	 	    	zTextView.setText("������֤----");
 	 	    	}
 	 	    	if(i==2){
 	 	    		zTextView.setText("��������----");
 	 	    	}
 	 	    }
 	 	    
 	 	  class MyHandler extends Handler{

 	 		@Override
 	 		public void handleMessage(Message msg) {
 	 			// TODO Auto-generated method stub
 	 			super.handleMessage(msg);
 	 			Log.i("MyHandler", "handleMessage......");

 	 	        //super.handleMessage(msg);

 	 	        // �˴����Ը���UI

 	 	        Bundle b = msg.getData();
            
 	 	        String color = b.getString("color");
 	 	        Log.i("handle", color);
 	 	        if(color=="ok"){
 	 	          zTextView.setText("���ӳɹ�");	
 	 	        }
 	 	        if(color=="wrong"){
 	 	        	zTextView.setText("�������");	
 	 	        }
 	 	        if(color=="limit"){
 	 	        	zTextView.setText("���˺��û����ڱ�ĵص��½");
 	 	        }
 	 	        if(color=="disconnect"){
 	 	        	zTextView.setText("���߳ɹ�");
 	 	        }
 	 	      
 	 	        if(color=="fd"){
	 	        	zTextView.setText("����ʧ�ܣ�");
	 	        }
 	 	        
 	 	        
 	 	        //MainActivity.this.zTextView.setText(color);

 	 	        //MyHandlerActivity.this.button.append(color);


 	 		}
 	 			 
 	 		}
 	 	  
}




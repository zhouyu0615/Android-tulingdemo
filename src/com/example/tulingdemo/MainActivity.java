package com.example.tulingdemo;

import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

//api key f7f0654e45e5fb1638e7e89d5d310c3c
public class MainActivity extends Activity implements HttpGetDataListener,
		android.view.View.OnClickListener {

	private static final OnClickListener OnClickListener = null;

	private String infoString = "http://www.tuling123.com/openapi/api?"
			+ "key=f7f0654e45e5fb1638e7e89d5d310c3c&info=你好";

	private List<ListData> lists;
	private Button btnSend;
	private EditText editSend;
	private String contentString;
	private ListView listView;

	private TextAdapter textAdapter;
	
	private long currentTime, oldTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();

	}

	public void initView() {
		lists = new ArrayList<ListData>();

		listView = (ListView) findViewById(R.id.listView1);
		editSend = (EditText) findViewById(R.id.sendText);
		btnSend = (Button) findViewById(R.id.send_btn);
		btnSend.setOnClickListener(this);

		textAdapter = new TextAdapter(lists, this);

		listView.setAdapter(textAdapter);

		ListData listData = new ListData(GetWelcomeSentence(),
				ListData.RECEIVER,getTime());
		lists.add(listData);
		
		if (isNetConnected(this)==false) {
			String netFailedstring=getResources().getString(R.string.NetConnectFailed);
			lists.add(new ListData(netFailedstring, ListData.RECEIVER,getTime()));
			
		}

		// HttpData httpData = (HttpData) new HttpData(infoString,
		// this).execute();
	}

	public String GetWelcomeSentence() {
		String[] welcomeArray = null;
		String welcome;

		welcomeArray = this.getResources().getStringArray(
				R.array.WelcomeSentence);

		int index = (int) ((Math.random() * 1000) % (welcomeArray.length));

		welcome = welcomeArray[index];
		return welcome;
	}

	@Override
	public void GetDataUrl(String data) {
		// TODO Auto-generated method stub
		 System.out.println(data);
		 ParseText(data);
	}

	public void ParseText(String str) {
		JSONObject jsonObject;
		
		try {	
			if(str!="")
			{
			jsonObject = new JSONObject(str);
			System.out.println(jsonObject.getString("code"));
			System.out.println(jsonObject.getString("text"));
			ListData  lData = new ListData(jsonObject.getString("text"),
					ListData.RECEIVER,getTime());
			
			lists.add(lData);
			}else {
			ListData	lData=new ListData(getResources().getString(R.string.Servefailed), 
						ListData.RECEIVER, getTime());
			lists.add(lData);
			}
			 
			textAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String contentString = editSend.getText().toString();
		editSend.setText("");
		String sendString = contentString.replace(" ", "");
		sendString = sendString.replace("\n", "");

		ListData sendData = new ListData(contentString, ListData.SEND,getTime());
		lists.add(sendData);
		
		if (lists.size()>=100) {
			for (int i = 0; i < lists.size()-30; i++) {
				lists.remove(i);
			}
			
		}
		textAdapter.notifyDataSetChanged();
		HttpData httpData = (HttpData) new HttpData(
				"http://www.tuling123.com/openapi/api?"
						+ "key=f7f0654e45e5fb1638e7e89d5d310c3c&info="
						+ sendString, this).execute();

	}

	public boolean isNetConnected(Context context) {

		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	@SuppressLint("SimpleDateFormat") public String getTime() {
		String timeString=null;
		
		currentTime=System.currentTimeMillis();
		
		if (currentTime-oldTime>=3*50*1000) {	
		    oldTime=currentTime;
		SimpleDateFormat format= new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");		
		Date curdate=new Date(currentTime);
		timeString=format.format(curdate);
		
		System.out.println("------->"+timeString);
			
		return timeString;
		}
		else 
		{
			return "";
			
		}
		
	}



}

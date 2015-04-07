package com.example.tulingdemo;

import java.io.IOException;

import java.lang.annotation.Retention;
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.rtp.AudioStream;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.speechsynthesizer.SpeechSynthesizer;
import com.baidu.speechsynthesizer.SpeechSynthesizerListener;
import com.baidu.speechsynthesizer.d.c;
import com.baidu.speechsynthesizer.publicutility.SpeechError;

//api key f7f0654e45e5fb1638e7e89d5d310c3c
public class MainActivity extends Activity implements HttpGetDataListener,
		android.view.View.OnClickListener {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);

		MenuInflater mInflater = getMenuInflater();
		mInflater.inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int item_id = item.getItemId();

		System.out.println(item);

		switch (item_id) {
		case R.id.action_settings:
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, action_settings.class);
			startActivity(intent);

			break;
		case R.id.quit:
			MainActivity.this.finish();
			break;
		case R.id.about:
			Intent i = new Intent();
			i.setClass(MainActivity.this, about.class);
			startActivity(i);
			break;

		default:
			break;
		}

		// return super.onOptionsItemSelected(item);
		return true;
	}

	private String infoString = "http://www.tuling123.com/openapi/api?"
			+ "key=f7f0654e45e5fb1638e7e89d5d310c3c&info=你好";

	private List<ListData> lists;
	private Button btnSend;
	private EditText editSend;
	private String contentString;
	private ListView listView;

	private TextAdapter textAdapter;

	private long currentTime, oldTime;

	public SettingParams sParams;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		GetSettingFormFile();

		initView();
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}

	protected void GetSettingFormFile() {
		SharedPreferences settingPreferences = getSharedPreferences(
				action_settings.SETTINGS, Activity.MODE_PRIVATE);

		SharedPreferences.Editor editor = settingPreferences.edit();
		sParams = new SettingParams();

		if (settingPreferences.getBoolean("first", true)) {
			// Toast.makeText(getApplicationContext(), "第一次使用，不存在Settingfile",
			// Toast.LENGTH_LONG).show();
			editor.putBoolean("first", false);
			editor.putBoolean(action_settings.VOICE_ON, true); // 默认开启语音朗读
			editor.putBoolean(action_settings.VOICE_MAN, false);// 默认女声
			editor.commit();
			sParams.setVoice_on_flag(true);
			sParams.setMan_voice(false);

		} else {
			// Toast.makeText(getApplicationContext(),
			// " 不是第一次使用，存在setting file",
			// Toast.LENGTH_LONG).show();

			sParams.setMan_voice(settingPreferences.getBoolean(
					action_settings.VOICE_MAN, false));
			sParams.setVoice_on_flag(settingPreferences.getBoolean(
					action_settings.VOICE_ON, true));

		}
	}

	public void initView() {
		lists = new ArrayList<ListData>();

		listView = (ListView) findViewById(R.id.listView1);
		editSend = (EditText) findViewById(R.id.sendText);
		btnSend = (Button) findViewById(R.id.send_btn);
		btnSend.setOnClickListener(this);

		textAdapter = new TextAdapter(lists, this);

		listView.setAdapter(textAdapter);

		String welString = GetWelcomeSentence();

		SpeechVoice(welString, sParams);

		ListData listData = new ListData(welString, ListData.RECEIVER,
				getTime());
		lists.add(listData);

		if (isNetConnected(this) == false) {
			String netFailedstring = getResources().getString(
					R.string.NetConnectFailed);
			lists.add(new ListData(netFailedstring, ListData.RECEIVER,
					getTime()));

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

	private SpeechSynthesizerThread speechThread = null;

	protected void SpeechVoice(String speechString, SettingParams sParams) {
		if (sParams.isVoice_on_flag()) {
			if (speechThread == null) {
				speechThread = SpeechSynthesizerThread.getInstance(
						getApplicationContext(), speechString, sParams);
				speechThread.start();
			} else {
				speechThread.Cancel();
				speechThread.setSpeechString(speechString);

			}

		}

	}

	@Override
	public void GetDataUrl(final String data) {
		// TODO Auto-generated method stub
		System.out.println(data);

		ParseText(data);
	}

	public void ParseText(String str) {

		try {
			if (str != "") {
				String getString = getStringFormJson(str);

				SpeechVoice(getString, sParams);

				ListData lData = new ListData(getString, ListData.RECEIVER,
						getTime());

				lists.add(lData);
			} else {
				ListData lData = new ListData(getResources().getString(
						R.string.Servefailed), ListData.RECEIVER, getTime());
				lists.add(lData);

			}
			// speechThread.setSpeechString(getStringFormJson(str));
			textAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static final int TEXT_INFO = 100000; // 文本类数据
	private static final int URL_INFO = 200000; // 网址类数据
	private static final int NOVEL_INFO = 301000; // 小说
	private static final int NEWS_INFO = 302000; // 新闻
	private static final int DOWNLOAD_INFO = 304000; // 应用、软件、下载
	private static final int TRAIN_INFO = 305000; // 列车
	private static final int FLIGHT_INFO = 306000; // 航班
	private static final int GROUPBUY_INFO = 307000; // 团购
	private static final int DISCOUNT_INFO = 308000; // 优惠
	private static final int HOTEL_INFO = 309000; // 酒店
	private static final int LOTTERY_INFO = 310000; // 彩票
	private static final int PRICE_INFO = 311000; // 价格
	private static final int RESTAURANT_INFO = 312000; // 餐厅
	private static final int LENGTHERROR_INFO = 40001; // key的长度错误（32位）
	private static final int CONTENTNULL_INFO = 40002; // 请求内容为空
	private static final int KEYERROR_INFO = 40003; // key错误或帐号未激活
	private static final int REQUESTTIMESOUT_INFO = 40004; // 当天请求次数已用完
	private static final int NOTSUPPORT_INFO = 40005; // 暂不支持该功能
	private static final int SERVERICEUPGRADE_INFO = 40006; // 服务器升级中
	private static final int DATAEXCEPTION_INFO = 40007; // 服务器数据格式异常
	private static final int DEFAULT_INFO = 50000; // 机器人设定的“学用户说话”或者“默认回答”

	private String getStringFormJson(String str) throws JSONException {
		StringBuffer resultString = null;
		JSONObject jsonObject;

		jsonObject = new JSONObject(str);
		System.out.println(jsonObject.getString("code"));

		resultString = new StringBuffer(jsonObject.getString("text"));
		resultString.append(" ");

		int code = Integer.parseInt(jsonObject.getString("code"));

		switch (code) {
		case URL_INFO:
			resultString.append(jsonObject.getString("url"));
			System.out.println("------->" + jsonObject.getString("url"));
			break;
		case NEWS_INFO:
			resultString.append(jsonObject.getString("url"));
			resultString.append(jsonObject.getString("list"));
			break;

		default:
			break;
		}

		System.out.println("resultString" + resultString);
		return resultString.toString();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String contentString = editSend.getText().toString();
		editSend.setText("");
		String sendString = contentString.replace(" ", "");
		sendString = sendString.replace("\n", "");

		ListData sendData = new ListData(contentString, ListData.SEND,
				getTime());
		lists.add(sendData);

		if (lists.size() >= 100) {
			for (int i = 0; i < lists.size() - 30; i++) {
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

	@SuppressLint("SimpleDateFormat")
	public String getTime() {
		String timeString = null;

		currentTime = System.currentTimeMillis();

		if (currentTime - oldTime >= 3 * 50 * 1000) {
			oldTime = currentTime;
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy年MM月dd日  HH:mm:ss");
			Date curdate = new Date(currentTime);
			timeString = format.format(curdate);

			System.out.println("------->" + timeString);

			return timeString;
		} else {
			return "";

		}

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		GetSettingFormFile();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		System.out.println("----->main activity onDestory!");
		super.onDestroy();
	}

}

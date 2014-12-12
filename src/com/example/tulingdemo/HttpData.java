package com.example.tulingdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.R.anim;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;

public class HttpData extends AsyncTask<String, Void, String> {

	private HttpGet hGet;
	private HttpResponse hResponse;
	private HttpClient hClient;;

	private String url;

	private HttpGetDataListener listener;

	public HttpData(String url, HttpGetDataListener listener) {
		this.url = url;
		this.listener = listener;

	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub

		try {
			hGet = new HttpGet(url);
			hClient = new DefaultHttpClient();
			// hClient=AndroidHttpClient.newInstance("");
//			hClient.getParams()
//					.setIntParameter("http.connection.timeout", 1000);

			hResponse = hClient.execute(hGet);
			HttpEntity hEntity = hResponse.getEntity();
			InputStream inStream = hEntity.getContent();
			BufferedReader bReader = new BufferedReader(new InputStreamReader(
					inStream));

			String line;
			StringBuffer sBuffer = new StringBuffer();
			while ((line = bReader.readLine()) != null) {
				sBuffer.append(line);
			}
			// System.out.println("--------->"+sBuffer.toString());
			return sBuffer.toString();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "".toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "".toString();
		}

	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		listener.GetDataUrl(result);
		super.onPostExecute(result);
	}

}

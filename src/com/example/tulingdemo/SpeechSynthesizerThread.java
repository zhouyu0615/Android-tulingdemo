package com.example.tulingdemo;

import android.content.Context;
import android.media.AudioManager;

import com.baidu.speechsynthesizer.SpeechSynthesizer;
import com.baidu.speechsynthesizer.publicutility.SpeechError;

public class SpeechSynthesizerThread extends Thread implements
		com.baidu.speechsynthesizer.SpeechSynthesizerListener {
	
	private int mPlayCount=0;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		while (true) {
			if (mPlayCount==0) {				
				int ret = speechSynthesizer.speak(this.speechString);
				mPlayCount=1;
			}
			else {
				yield();
			}
		}

	}

	public void Cancel() {		
		speechSynthesizer.cancel();
	}


	private static SpeechSynthesizerThread speechSynthesizerThread = null;

	public static synchronized SpeechSynthesizerThread getInstance(Context context,
			String speechString, SettingParams sParams) {
		if (speechSynthesizerThread == null) {
			speechSynthesizerThread = new SpeechSynthesizerThread(context,
					speechString, sParams);
		}

		return speechSynthesizerThread;
	}

	private String speechString;

	public void setSpeechString(String speechString) {
		this.speechString = speechString;
		mPlayCount=0;
	}

	private Context mContext;
	private SettingParams sParams;

	public SpeechSynthesizerThread(Context context, String speechString,
			SettingParams sParams) {
		this.speechString = speechString;
		this.mContext = context;
		this.sParams = sParams;
		this.initSpeechSynthesizer();

	}

	public SpeechSynthesizerThread(Context context, SettingParams sParams) {
		this.mContext = context;
		this.sParams = sParams;
		this.initSpeechSynthesizer();

	}

	private SpeechSynthesizer speechSynthesizer;

	public void initSpeechSynthesizer() {
		speechSynthesizer = new SpeechSynthesizer(mContext, "holder", this);
		speechSynthesizer.setApiKey("BcjRSEy8hCfbYwWmrdePzxGI",
				"lGhwmMNeeTGGtSpmhusLdryHhUECc1G2");
		speechSynthesizer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		setParams();
	}

	private void setParams() {
		if (sParams.isMan_voice()) {
			speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "1");
		} else {
			speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");

		}

		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "5");
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "6");
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_ENCODE,
				SpeechSynthesizer.AUDIO_ENCODE_AMR);
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_RATE,
				SpeechSynthesizer.AUDIO_BITRATE_AMR_15K85);
		// speechSynthesizer.setParam(SpeechSynthesizer.PARAM_LANGUAGE,
		// SpeechSynthesizer.LANGUAGE_ZH);
		// speechSynthesizer.setParam(SpeechSynthesizer.PARAM_NUM_PRON, "0");
		// speechSynthesizer.setParam(SpeechSynthesizer.PARAM_ENG_PRON, "0");
		// speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PUNC, "0");
		// speechSynthesizer.setParam(SpeechSynthesizer.PARAM_BACKGROUND, "0");
		// speechSynthesizer.setParam(SpeechSynthesizer.PARAM_STYLE, "0");
		// speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TERRITORY, "0");
	}

	@Override
	public void onBufferProgressChanged(SpeechSynthesizer arg0, int arg1) {
		// TODO Auto-generated method stub
		System.out.println("---------->onBufferProgressChanged!");
	}

	@Override
	public void onCancel(SpeechSynthesizer arg0) {
		// TODO Auto-generated method stub
		System.out.println("---------->onCancel!");
	}

	@Override
	public void onError(SpeechSynthesizer arg0, SpeechError arg1) {
		// TODO Auto-generated method stub
		System.out.println("---------->onError!");
		System.out.println(arg1);
	}

	@Override
	public void onNewDataArrive(SpeechSynthesizer arg0, byte[] arg1,
			boolean arg2) {
		// TODO Auto-generated method stub
		System.out.println("---------->onNewDataArrive!");
	}

	@Override
	public void onSpeechFinish(SpeechSynthesizer arg0) {
		// TODO Auto-generated method stub
		System.out.println("---------->onSpeechFinish!");
	}

	@Override
	public void onSpeechPause(SpeechSynthesizer arg0) {
		// TODO Auto-generated method stub
		System.out.println("---------->onSpeechPause!");
	}

	@Override
	public void onSpeechProgressChanged(SpeechSynthesizer arg0, int arg1) {
		// TODO Auto-generated method stub
		System.out.println("---------->onSpeechProgressChanged!");
	}

	@Override
	public void onSpeechResume(SpeechSynthesizer arg0) {
		// TODO Auto-generated method stub
		System.out.println("---------->onSpeechResume!");
	}

	@Override
	public void onSpeechStart(SpeechSynthesizer arg0) {
		// TODO Auto-generated method stub
		System.out.println("---------->onSpeechStart!");
	}

	@Override
	public void onStartWorking(SpeechSynthesizer arg0) {
		// TODO Auto-generated method stub
		System.out.println("---------->onStartWorking!");
	}


}

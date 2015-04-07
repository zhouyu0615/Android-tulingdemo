package com.example.tulingdemo;

import android.app.Activity;
import android.app.PendingIntent.OnFinished;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;

public class action_settings extends Activity implements OnClickListener {

	public Switch voiceSwitch;
	public Switch voicechooseSwitch;
	public SharedPreferences settingPreferences;

	public static final String SETTINGS = "settings";
	public static final String VOICE_ON = "Voice_on";
	public static final String VOICE_MAN = "Voice_man";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.action_setting);

		voiceSwitch = (Switch) findViewById(R.id.voiceSwitcher);
		voicechooseSwitch = (Switch) findViewById(R.id.voicechooseSwitcher);

		voiceSwitch.setOnClickListener(this);
		voicechooseSwitch.setOnClickListener(this);

		settingPreferences = getSharedPreferences(SETTINGS,
				Activity.MODE_PRIVATE);

		if (settingPreferences.getBoolean(VOICE_ON, true)) {
			voiceSwitch.setChecked(true);
		} else {
			voiceSwitch.setChecked(false);
		}
		if (settingPreferences.getBoolean(VOICE_MAN, false)) {
			voicechooseSwitch.setChecked(true);
		} else {
			voicechooseSwitch.setChecked(false);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		SharedPreferences.Editor editor = settingPreferences.edit();
		switch (id) {
		case R.id.voiceSwitcher:
			if (voiceSwitch.isChecked()) {
				editor.putBoolean(VOICE_ON, true);
			} else {
				editor.putBoolean(VOICE_ON, false);
			}
			break;
		case R.id.voicechooseSwitcher:
			if (voicechooseSwitch.isChecked()) {
				editor.putBoolean(VOICE_MAN, true);
			} else {
				editor.putBoolean(VOICE_MAN, false);
			}
			break;

		default:
			break;
		}
		editor.commit();

	}
}
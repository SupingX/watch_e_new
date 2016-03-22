package com.suping.i2_watch.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suping.i2_watch.BaseActivity;
import com.suping.i2_watch.R;
import com.suping.i2_watch.entity.I2WatchProtocolDataForWrite;
import com.suping.i2_watch.entity.SleepMonitorProtocol;
import com.suping.i2_watch.service.XplBluetoothService;
import com.suping.i2_watch.service.xblue.XBlueService;
import com.suping.i2_watch.util.SharedPreferenceUtil;

public class SleepMonitorActivity extends BaseActivity implements OnClickListener {

	private final static int REQ_TARGET = 4;
	private final static int REQ_STARTTIME = 5;
	private final static int REQ_ENDTIME = 6;
	private final static int RESULT_TARGET = 44;
	private final static int RESULT_STARTTIME = 55;
	private final static int RESULT_ENDTIME = 66;
	private ImageView imgBack;
	private TextView tvTitle;
	/** TextView 目标睡眠时间/开始时刻/结束时刻  **/
	private TextView tvTargetValue, tvStartValue, tvEndValue;
	/** RelativeLayout 目标睡眠时间/开始时刻/结束时刻  **/
	private RelativeLayout rlStart, rlEnd, rlTarget;

	private long exitTime = 0;
	private XBlueService xBlueService;
//	private XplBluetoothService xplBluetoothService;
	/** 蓝牙  **/ 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sleep_monitor);
		initViews();
		setClick();
		tvTitle.setText(getResources().getString(R.string.sleep_monitor));
	}
	
	@Override
	protected void onStart() {
		super.onStart();
//		xplBluetoothService = getXplBluetoothService();
		
		xBlueService = getXBlueService();
	}
	
	@Override
	protected void onDestroy() {

		super.onDestroy();
	}
	
	private void doWriteToWatch() {

		if (xBlueService!=null && xBlueService.isAllConnected()) {
//			xplBluetoothService.writeCharacteristic(I2WatchProtocolDataForWrite.protocolDataForSleepPeriodSync(this).toByte());
			xBlueService.write(I2WatchProtocolDataForWrite.protocolDataForSleepPeriodSync(this).toByte());
		}else{
			showShortToast(getString(R.string.no_binded_device));
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		// from SleepMonitorTargetActivity
		case REQ_TARGET:
			switch (resultCode) {
			case RESULT_TARGET:
				Bundle b = data.getExtras();
				String min = b.getString("min");
				String sec = b.getString("sec");
				String value = min + getString(R.string.hour) + sec+getString(R.string.min);
				tvTargetValue.setText(value);
				SharedPreferenceUtil.put(getApplicationContext(), I2WatchProtocolDataForWrite.SHARE_MONITOR_TARGET_HOUR, min);
				SharedPreferenceUtil.put(getApplicationContext(), I2WatchProtocolDataForWrite.SHARE_MONITOR_TARGET_MIN, sec);
				break;
			default:
				break;
			}
			break;
		// from SleepMonitorStartActivity
		case REQ_STARTTIME:
			switch (resultCode) {
			case RESULT_STARTTIME:
				Bundle b = data.getExtras();
				String min = b.getString("min");
				String sec = b.getString("sec");
				String value = min + ":" + sec;
				tvStartValue.setText(value);
				// SharedPreferenceUtil.put(getApplicationContext(),
				// "sleep_start", value);
				SharedPreferenceUtil.put(getApplicationContext(), I2WatchProtocolDataForWrite.SHARE_MONITOR_START_HOUR, min);
				SharedPreferenceUtil.put(getApplicationContext(), I2WatchProtocolDataForWrite.SHARE_MONITOR_START_MIN, sec);
				break;
			default:
				break;
			}
			break;
		// from SleepMonitorEndActivity
		case REQ_ENDTIME:
			switch (resultCode) {
			case RESULT_ENDTIME:
				Bundle b = data.getExtras();
				String min = b.getString("min");
				String sec = b.getString("sec");
				String value = min + ":" + sec;
				tvEndValue.setText(value);
				// SharedPreferenceUtil.put(getApplicationContext(),
				// "sleep_end", value);
				SharedPreferenceUtil.put(getApplicationContext(), I2WatchProtocolDataForWrite.SHARE_MONITOR_END_HOUR, min);
				SharedPreferenceUtil.put(getApplicationContext(), I2WatchProtocolDataForWrite.SHARE_MONITOR_END_MIN, sec);
				break;
			default:
				break;
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		doWriteToWatch();
		super.onBackPressed();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			doWriteToWatch();
//			Intent retrunToMain = new Intent(SleepMonitorActivity.this, MenuActivity.class);
//			startActivity(retrunToMain);
			this.finish();
			//
			SleepMonitorProtocol sp = I2WatchProtocolDataForWrite.protocolDataForSleepPeriodSync(this);
			//m
			
//			overridePendingTransition(R.anim.activity_from_left_to_right_enter, R.anim.activity_from_left_to_right_exit);
			break;

		case R.id.rl_target:
			Intent targelIntent = new Intent(SleepMonitorActivity.this, SleepMonitorTargetActivity.class);
			startActivityForResult(targelIntent, REQ_TARGET);
			break;
		case R.id.rl_start:
			Intent startIntent = new Intent(SleepMonitorActivity.this, SleepMonitorStartActivity.class);
			startActivityForResult(startIntent, REQ_STARTTIME);
			break;
		case R.id.rl_end:
			Intent endIntent = new Intent(SleepMonitorActivity.this, SleepMonitorEndActivity.class);
			startActivityForResult(endIntent, REQ_ENDTIME);
			break;
		}
	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//			if ((System.currentTimeMillis() - exitTime) > 2000) {
//				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
//				exitTime = System.currentTimeMillis();
//			} else {
//				finish();
//				System.exit(0);
//			}
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}

	private void initViews() {
		imgBack = (ImageView) findViewById(R.id.img_back);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTargetValue = (TextView) findViewById(R.id.tv_target_value);
		tvStartValue = (TextView) findViewById(R.id.tv_start_value);
		tvEndValue = (TextView) findViewById(R.id.tv_end_value);
		rlStart = (RelativeLayout) findViewById(R.id.rl_start);
		rlEnd = (RelativeLayout) findViewById(R.id.rl_end);
		rlTarget = (RelativeLayout) findViewById(R.id.rl_target);

		// String sleep_start = (String)
		// SharedPreferenceUtil.get(getApplicationContext(), "sleep_start",
		// "7:00");
		// tvStartValue.setText(sleep_start);
		// String sleep_end = (String)
		// SharedPreferenceUtil.get(getApplicationContext(), "sleep_end",
		// "7:00");
		// tvEndValue.setText(sleep_end);
		// String sleep_target = (String)
		// SharedPreferenceUtil.get(getApplicationContext(), "sleep_target",
		// "7:00");
		// tvTargetValue.setText(sleep_target);
		initSetting();
	}

	/**
	 * 初始化设置
	 */
	private void initSetting() {
		// target
		String targetHour = (String) SharedPreferenceUtil.get(getApplicationContext(), I2WatchProtocolDataForWrite.SHARE_MONITOR_TARGET_HOUR, I2WatchProtocolDataForWrite.DEFAULT_START_HOUR);
		String targetMin = (String) SharedPreferenceUtil.get(getApplicationContext(), I2WatchProtocolDataForWrite.SHARE_MONITOR_TARGET_MIN, I2WatchProtocolDataForWrite.DEFAULT_START_MIN);
		tvTargetValue.setText(targetHour + getString(R.string.hour) + targetMin +getString(R.string.min));
		// start
		String startHour = (String) SharedPreferenceUtil.get(getApplicationContext(), I2WatchProtocolDataForWrite.SHARE_MONITOR_START_HOUR, I2WatchProtocolDataForWrite.DEFAULT_START_HOUR);
		String startMin = (String) SharedPreferenceUtil.get(getApplicationContext(), I2WatchProtocolDataForWrite.SHARE_MONITOR_START_MIN, I2WatchProtocolDataForWrite.DEFAULT_START_MIN);
		tvStartValue.setText(startHour + ":" + startMin);
		// end
		String endHour = (String) SharedPreferenceUtil.get(getApplicationContext(), I2WatchProtocolDataForWrite.SHARE_MONITOR_END_HOUR, I2WatchProtocolDataForWrite.DEFAULT_END_HOUR);
		String endMin = (String) SharedPreferenceUtil.get(getApplicationContext(), I2WatchProtocolDataForWrite.SHARE_MONITOR_END_MIN, I2WatchProtocolDataForWrite.DEFAULT_END_MIN);
		tvEndValue.setText(endHour + ":" + endMin);

	}

	private void setClick() {
		imgBack.setOnClickListener(this);
		tvTitle.setOnClickListener(this);
		rlStart.setOnClickListener(this);
		rlTarget.setOnClickListener(this);
		rlEnd.setOnClickListener(this);
	}
}

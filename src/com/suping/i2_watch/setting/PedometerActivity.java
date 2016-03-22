package com.suping.i2_watch.setting;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.suping.i2_watch.R;
import com.suping.i2_watch.util.SharedPreferenceUtil;
import com.suping.i2_watch.view.CircleSeekBar;

public class PedometerActivity extends Activity implements OnClickListener,CircleSeekBar.OnCircleSeekBarChangedListener{
	
	private ImageView imgBack;
	private TextView textViewTitle,textViewStep,textViewKm,textViewKcal;
	private com.suping.i2_watch.view.CircleSeekBar seekbar;
	private TextView textViewLight,textViewMedium,textViewHeavy;
	private RadioGroup rgPedo;
	private int goal;
	public static final String SHARE_GOAL = "share_goal";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedometer);
		initViews();
		setClick();
		
		textViewTitle.setText(getResources().getString(R.string.pedometer_target));
		seekbar.setProgress(goal);
		check(goal);
	}
	
	@Override
	protected void onDestroy() {
	
		super.onDestroy();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			SharedPreferenceUtil.put(getApplicationContext(), SHARE_GOAL, goal);
//			Intent returnSetting = new Intent(PedometerActivity.this,SettingActivity.class);
//			startActivity(returnSetting);
			this.finish();
			overridePendingTransition(
					//左右是指手机屏幕的左右
					R.anim.activity_from_left_to_right_enter,
					R.anim.activity_from_left_to_right_exit);
			break;
			
		case R.id.tv_light_pedo:
			rgPedo.check(0);
			seekbar.setProgress(5000);
			goal = 5000;
		
			break;
		case R.id.tv_monitor_pedo:
			rgPedo.check(1);
			seekbar.setProgress(10000);
			goal = 10000;
		
			break;
		case R.id.tv_heavy_pedo:
			rgPedo.check(2);
			seekbar.setProgress(15000);
			goal = 15000;
			break;
		default:
			break;
		}
	}

	@Override
	public void onProgressChanged(CircleSeekBar seekBar, int progress,
			boolean fromUser) {
		goal = progress;
		//Log.d("OB", progress+"");
		check(goal);
		textViewStep.setText(goal+"");
		
	}
	
	private void setDrawable(TextView tv , int resourceid){
		 Resources res = getResources();
		 Drawable img = res.getDrawable(resourceid);
		 // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		 img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
		 tv.setCompoundDrawables(null, img, null, null); //
	}
	
	private void initViews() {
		imgBack = (ImageView) findViewById(R.id.img_back);
		rgPedo = (RadioGroup) findViewById(R.id.rg_pedo);
		textViewTitle = (TextView) findViewById(R.id.tv_title);
		textViewStep = (TextView) findViewById(R.id.tv_step_value);
		textViewKm = (TextView) findViewById(R.id.tv_kg_value);
		textViewKcal = (TextView) findViewById(R.id.tv_kal_value);
		textViewLight = (TextView) findViewById(R.id.tv_light_pedo);
		textViewMedium = (TextView) findViewById(R.id.tv_monitor_pedo);
		textViewHeavy = (TextView) findViewById(R.id.tv_heavy_pedo);
		
		seekbar = (com.suping.i2_watch.view.CircleSeekBar) findViewById(R.id.pedo_circle);
		goal = (int) SharedPreferenceUtil.get(getApplicationContext(),SHARE_GOAL, 5000);
		seekbar.setProgress(goal);
		check(goal);
		seekbar.setOnSeekBarChangedListener(this);
	}
	
	private void setClick() {
		imgBack.setOnClickListener(this);
		textViewLight.setOnClickListener(this);
		textViewMedium.setOnClickListener(this);
		textViewHeavy.setOnClickListener(this);
		rgPedo.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.tv_light_pedo:
					updateUI(1);
					break;
				case R.id.tv_monitor_pedo:
					updateUI(2);
					break;
				case R.id.tv_heavy_pedo:
					updateUI(3);
					break;

				default:
					break;
				}
			}
		});
	}
	
	/**
	 * 更新UI
	 */
	private void updateUI(int i){
		switch (i) {
		case 1:
			//Log.d("OB", "case1");
			textViewLight.setTextColor(getResources().getColor(R.color.color_top_bg));
			textViewMedium.setTextColor(getResources().getColor(R.color.settings_goal_text_color));
			textViewHeavy.setTextColor(getResources().getColor(R.color.settings_goal_text_color));
			setDrawable(textViewLight,R.drawable.ic_targetlight);
			setDrawable(textViewMedium,R.drawable.ic_targetmedium_unless);
			setDrawable(textViewHeavy,R.drawable.ic_targetheavy_unless);
			break;
		case 2:
			//Log.d("OB", "case2");
			textViewLight.setTextColor(getResources().getColor(R.color.settings_goal_text_color));
			textViewMedium.setTextColor(getResources().getColor(R.color.color_top_bg));
			textViewHeavy.setTextColor(getResources().getColor(R.color.settings_goal_text_color));
			setDrawable(textViewLight,R.drawable.ic_targetlight_unless);
			setDrawable(textViewMedium,R.drawable.ic_targetmedium);
			setDrawable(textViewHeavy,R.drawable.ic_targetheavy_unless);
			break;
		case 3:
			//Log.d("OB", "case3");
			textViewLight.setTextColor(getResources().getColor(R.color.settings_goal_text_color));
			textViewMedium.setTextColor(getResources().getColor(R.color.settings_goal_text_color));
			textViewHeavy.setTextColor(getResources().getColor(R.color.color_top_bg));
			setDrawable(textViewLight,R.drawable.ic_targetlight_unless);
			setDrawable(textViewMedium,R.drawable.ic_targetmedium_unless);
			setDrawable(textViewHeavy,R.drawable.ic_targetheavy);
			break;

		default:
			break;
		}
		DecimalFormat df = new DecimalFormat("0.00");
		String kael = df.format(((goal/1000)*18.842));
		String km = df.format((goal/1000)*0.416);
		textViewStep.setText(goal+"");
		textViewKcal.setText(kael);
		textViewKm.setText(km);
	}
	
	/**
	 * 检查goal的级数 少量运动/适量运动/高强度运动
	 * @param goal
	 */
	private void check(int goal){
		//Log.d("OB", "check-->"+goal);
		if(goal<6400&&goal>=1000){
			updateUI(1);
		}else if(goal<12800&&goal>=6400){
			updateUI(2);
		}else if(goal<20000&&goal>=12800){
			updateUI(3);
		}
	}
}

package com.suping.i2_watch.menu;

import com.suping.i2_watch.util.SharedPreferenceUtil;
import android.content.Intent;

public class SleepMonitorEndActivity extends AbstractSetTimeActivity {
	public final static int RESULT_ENDTIME = 66;

	public void confirm() {
		Intent intent = new Intent(SleepMonitorEndActivity.this, SleepMonitorActivity.class);
		intent.putExtras(b);
		setResult(RESULT_ENDTIME, intent);
		finish();
	}

	@Override
	public void initValue() {
		String hour = (String) SharedPreferenceUtil.get(SleepMonitorEndActivity.this, SleepMonitorActivity.SHARE_MONITOR_END_HOUR,
				"07");
		npHour.setValue(Integer.valueOf(hour));

		String min = (String) SharedPreferenceUtil.get(SleepMonitorEndActivity.this, SleepMonitorActivity.SHARE_MONITOR_END_MIN,
				"00");
		if (min.equals(values[0])) {
			npMin.setValue(0);
		} else if (min.equals(values[1])) {
			npMin.setValue(1);
		}
	}

}

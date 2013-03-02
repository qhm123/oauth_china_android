package org.mfactory.oauth_china_android.utils;

import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class Utils {

	public static String getDeviceId(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		if (!TextUtils.isEmpty(imei)) {
			return imei;
		} else {
			String androidId = Secure.getString(context.getContentResolver(),
					Secure.ANDROID_ID);
			return androidId;
		}
	}
}

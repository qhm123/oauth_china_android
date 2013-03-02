package org.mfactory.oauth_china_android;

import org.mfactory.oauth_china_android.token.Token;
import org.mfactory.oauth_china_android.utils.DesEncrypt;
import org.mfactory.oauth_china_android.utils.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

class AccessTokenKeeper {

	private static String OAUTH_PREFS = "oauth";
	private String OAUTH_UID = "uid";
	private String OAUTH_TOKEN = "access_token";
	private String OAUTH_REFRESH_TOKEN = "refresh_token";
	private String OAUTH_EXPIRES_TIME = "expires_time";

	public AccessTokenKeeper(String name) {
		OAUTH_UID += ("-" + name);
		OAUTH_TOKEN += ("-" + name);
		OAUTH_REFRESH_TOKEN += ("-" + name);
		OAUTH_EXPIRES_TIME += ("-" + name);
	}

	public void keepAccessToken(Context context, Token token) {
		SharedPreferences pref = context.getSharedPreferences(OAUTH_PREFS,
				Context.MODE_PRIVATE);
		Editor editor = pref.edit();

		String imei = Utils.getDeviceId(context);
		DesEncrypt des = new DesEncrypt(imei);

		editor.putString(OAUTH_TOKEN, des.getEncString(token.getAccessToken()));
		editor.putLong(OAUTH_EXPIRES_TIME, token.getExpiresTime());
		editor.putString(OAUTH_REFRESH_TOKEN,
				des.getEncString(token.getRefreshToken()));
		editor.putString(OAUTH_UID, des.getEncString(token.getUid()));

		editor.commit();
	}

	public void clear(Context context) {
		SharedPreferences pref = context.getSharedPreferences(OAUTH_PREFS,
				Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.clear();
		editor.commit();
	}

	public Token readAccessToken(Context context) {
		Token token = new Token();
		DesEncrypt des = new DesEncrypt(Utils.getDeviceId(context));
		SharedPreferences pref = context.getSharedPreferences(OAUTH_PREFS,
				Context.MODE_PRIVATE);

		token.setAccessToken(des.getDesString(pref.getString(OAUTH_TOKEN, "")));
		token.setExpiresTime(pref.getLong(OAUTH_EXPIRES_TIME, 0));
		token.setRefreshToken(des.getDesString(pref.getString(
				OAUTH_REFRESH_TOKEN, "")));
		token.setUid(des.getDesString(pref.getString(OAUTH_UID, "")));

		if (TextUtils.isEmpty(token.getAccessToken())) {
			return null;
		}

		return token;
	}

	public String getUid(Context context) {
		String phone_imei = Utils.getDeviceId(context);
		DesEncrypt des = new DesEncrypt(phone_imei);
		SharedPreferences prefs = context.getSharedPreferences(OAUTH_PREFS,
				Context.MODE_PRIVATE);
		String uid = des.getDesString(prefs.getString(OAUTH_UID, ""));
		return uid;
	}
}

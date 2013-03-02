package org.mfactory.oauth_china_android.token;

import org.json.JSONException;
import org.json.JSONObject;
import org.mfactory.oauth_china_android.config.DoubanConfig;
import org.mfactory.oauth_china_android.config.OAuthConfig;
import org.mfactory.oauth_china_android.config.QQConfig;
import org.mfactory.oauth_china_android.config.RenrenConfig;
import org.mfactory.oauth_china_android.config.SinaConfig;
import org.mfactory.oauth_china_android.config.TweiboConfig;

import android.text.TextUtils;
import android.util.Log;

public class Token {

	private static final String TAG = Token.class.getSimpleName();

	private long mExpiresTime = 0;
	private String mAccessToken = "";
	private String mRefreshToken = "";
	private long mExpiresIn;
	private String mUid = "";

	public Token() {
	}

	public static Token make(String response, OAuthConfig config) {
		Log.d(TAG, "token make response: " + response);

		if (config instanceof SinaConfig) {
			try {
				return new WeiboToken(new JSONObject(response));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (config instanceof DoubanConfig) {
			try {
				return new DoubanToken(new JSONObject(response));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (config instanceof TweiboConfig) {
			return new TWeiboToken(response);
		} else if (config instanceof RenrenConfig) {
			try {
				return new RenrenToken(new JSONObject(response));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (config instanceof QQConfig) {
			return new QQToken(response);
		}

		return null;
	}

	public void setAccessToken(String mAccessToken) {
		this.mAccessToken = mAccessToken;
	}

	public String getRefreshToken() {
		return mRefreshToken;
	}

	public void setRefreshToken(String mRefreshToken) {
		this.mRefreshToken = mRefreshToken;
	}

	public String getUid() {
		return mUid;
	}

	public void setUid(String mUid) {
		this.mUid = mUid;
	}

	public String getAccessToken() {
		return mAccessToken;
	}

	public void setExpiresIn(long expiresIn) {
		if (expiresIn != 0) {
			setExpiresTime(System.currentTimeMillis() + expiresIn * 1000);
			this.mExpiresIn = expiresIn;
		}
	}

	public void setExpiresTime(long mExpiresTime) {
		this.mExpiresTime = mExpiresTime;
	}

	public long getExpiresTime() {
		return mExpiresTime;
	}

	public long getExpiresIn() {
		return mExpiresIn;
	}

	public boolean isSessionValid() {
		return (!TextUtils.isEmpty(mAccessToken) && (mExpiresTime == 0 || (System
				.currentTimeMillis() < mExpiresTime)));
	}

	@Override
	public String toString() {
		String date = new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
				.format(new java.util.Date(mExpiresTime));
		return "mAccessToken:" + mAccessToken + ";mExpiresTime:" + date
				+ ";mRefreshToken:" + mRefreshToken + ";mUid:" + mUid;
	}

}

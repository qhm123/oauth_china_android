package org.mfactory.oauth_china_android_demo;

import org.mfactory.oauth_china_android.OAuthChina;
import org.mfactory.oauth_china_android.OAuthListener;
import org.mfactory.oauth_china_android.config.OAuthConfig;
import org.mfactory.oauth_china_android.config.SinaConfig;
import org.mfactory.oauth_china_android.token.Token;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class SimpleActivity extends Activity {

	private static final String TAG = SimpleActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		OAuthConfig config = new SinaConfig("2489004023",
				"990329a88e14d1ab133ac006e7e018f9", "http://www.sina.com");
		OAuthChina oauthChina = new OAuthChina(this, config);

		// 获取token信息
		Token token = oauthChina.getToken();
		if (token != null) {
			// token存在
			if (token.isSessionValid()) {
				// token有效
				Log.d(TAG, "token: " + token.toString());
			} else {
				// token过期
				oauthChina
						.refreshToken(token.getRefreshToken(), mOAuthListener);
			}
		} else {
			// token不存在，开启OAuth验证
			oauthChina.startOAuth(mOAuthListener);
		}

		// 清空token信息
		// oauthChina.clearToken();

		// 使用refresh_token刷新access_token，部分公司提供的oauth验证不支持refresh_token
		// oauthChina.refreshToken(token.getRefreshToken(), mOAuthListener);
	}

	OAuthListener mOAuthListener = new OAuthListener() {

		@Override
		public void onSuccess(Token token) {
			Log.d(TAG, "token: " + token.toString());
		}

		@Override
		public void onError(String error) {
			Log.d(TAG, "error: " + error);
		}

		@Override
		public void onCancel() {
			Log.d(TAG, "cancel");

		}
	};
}

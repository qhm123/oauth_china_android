package org.mfactory.oauth_china_android.config;

import java.net.URLEncoder;

import org.mfactory.oauth_china_android.OAuthListener;
import org.mfactory.oauth_china_android.token.Token;

import android.net.Uri;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public abstract class OAuthConfig {

	protected static final String TAG = OAuthConfig.class.getSimpleName();

	public String appKey = "";
	public String appSecret = "";
	public String redirectUrl = "";

	public OAuthConfig(String appKey, String appSecret, String redirectUrl) {
		this.appKey = appKey;
		this.appSecret = appSecret;
		this.redirectUrl = redirectUrl;
	}

	@SuppressWarnings("deprecation")
	public String encodedRedirectUrl() {
		return URLEncoder.encode(redirectUrl);
	}

	public abstract String getRefreshTokenUrl();

	public abstract RequestParams getRefreshTokenParams(String refreshToken);

	public abstract String getCodeUrl();

	public abstract String getAccessCodeUrl(String code);

	public abstract RequestParams getAccessTokenParams(String code);

	public void getAccessCode(Uri uri, final OAuthListener l) {
		final String code = uri.getQueryParameter("code");
		Log.d(TAG, "code: " + code);

		AsyncHttpClient client = new AsyncHttpClient();
		String newUrl = getAccessCodeUrl(code);
		Log.d(TAG, "newUrl: " + newUrl);
		RequestParams requestParams = getAccessTokenParams(code);
		getAccessCodeRequest(client, newUrl, requestParams,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						if (l != null) {
							Token token = Token
									.make(response, OAuthConfig.this);
							l.onSuccess(token);
						}
					}
				});
	}

	protected void getAccessCodeRequest(AsyncHttpClient client, String url,
			RequestParams requestParams, AsyncHttpResponseHandler l) {
		client.post(url, requestParams, l);
	}
}

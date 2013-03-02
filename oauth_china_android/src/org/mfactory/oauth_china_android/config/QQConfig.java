package org.mfactory.oauth_china_android.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mfactory.oauth_china_android.OAuthListener;
import org.mfactory.oauth_china_android.token.Token;

import android.net.Uri;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class QQConfig extends OAuthConfig {

	public QQConfig(String appKey, String appSecret, String redirectUrl) {
		super(appKey, appSecret, redirectUrl);
	}

	@Override
	public String getRefreshTokenUrl() {
		return null;
	}

	@Override
	public RequestParams getRefreshTokenParams(String refreshToken) {
		RequestParams params = new RequestParams();
		params.put("client_id", appKey);
		params.put("grant_type", "refresh_token");
		params.put("refresh_token", refreshToken);

		return params;
	}

	@Override
	public String getCodeUrl() {
		return "https://graph.qq.com/oauth2.0/authorize?client_id=" + appKey
				+ "&response_type=code&redirect_uri=" + encodedRedirectUrl()
				+ "&state=test&display=mobile";
	}

	@Override
	public String getAccessCodeUrl(String code) {
		return "https://graph.qq.com/oauth2.0/token?client_id=" + appKey
				+ "&client_secret=" + appSecret + "&redirect_uri="
				+ encodedRedirectUrl() + "&grant_type=authorization_code&code="
				+ code;
	}

	@Override
	public RequestParams getAccessTokenParams(String code) {
		RequestParams params = new RequestParams();

		return params;
	}

	public void getAccessCode(Uri uri, final OAuthListener l) {
		final String code = uri.getQueryParameter("code");
		Log.d(TAG, "code: " + code);

		final AsyncHttpClient client = new AsyncHttpClient();
		String newUrl = getAccessCodeUrl(code);
		Log.d(TAG, "newUrl: " + newUrl);
		RequestParams requestParams = getAccessTokenParams(code);
		getAccessCodeRequest(client, newUrl, requestParams,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						if (l != null) {
							final Token token = Token.make(response,
									QQConfig.this);
							client.get(
									"https://graph.z.qq.com/moc2/me?access_token="
											+ token.getAccessToken(),
									new AsyncHttpResponseHandler() {
										public void onSuccess(String response) {
											Log.d(TAG, "openid response: "
													+ response);
											// client_id=100222222&openid=1704************************878C
											Pattern pattern = Pattern
													.compile("client_id=(.*)&openid=(.*)");
											Matcher m = pattern
													.matcher(response);
											if (m.matches()
													&& m.groupCount() == 2) {
												token.setUid(m.group(2));
												l.onSuccess(token);
											}

										};
									});

						}
					}
				});
	}

	@Override
	protected void getAccessCodeRequest(AsyncHttpClient client, String url,
			RequestParams requestParams, AsyncHttpResponseHandler l) {
		client.get(url, requestParams, l);
	}

}

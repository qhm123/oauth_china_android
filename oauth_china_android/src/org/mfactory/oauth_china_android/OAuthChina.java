package org.mfactory.oauth_china_android;

import org.mfactory.oauth_china_android.config.OAuthConfig;
import org.mfactory.oauth_china_android.token.Token;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class OAuthChina {

	private static final String TAG = OAuthChina.class.getSimpleName();

	private Context mContext;
	private OAuthConfig mConfig;
	private AccessTokenKeeper mKeeper;

	public OAuthChina(Context context, OAuthConfig config) {
		mContext = context;
		mConfig = config;
		mKeeper = new AccessTokenKeeper(config.getClass().getSimpleName());
	}

	public OAuthConfig getConfig() {
		return mConfig;
	}

	public Token getToken() {
		return mKeeper.readAccessToken(mContext);
	}

	public void clearToken() {
		mKeeper.clear(mContext);
	}

	public void refreshToken(String refreshToken, final OAuthListener l) {
		AsyncHttpClient client = new AsyncHttpClient();
		String url = mConfig.getRefreshTokenUrl();
		if (url == null) {
			throw new UnsupportedOperationException();
		}
		RequestParams params = mConfig.getRefreshTokenParams(refreshToken);
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				Token token = Token.make(response, mConfig);
				if (token.isSessionValid()) {
					mKeeper.keepAccessToken(mContext, token);
				}
				if (l != null) {
					l.onSuccess(token);
				}
			}

			@Override
			public void onFailure(Throwable e, String response) {
				if (l != null) {
					l.onError(response);
				}
			}
		});
	}

	public void startOAuth(final OAuthListener l) {
		OAuthDialog dialog = new OAuthDialog(mContext, mConfig);
		dialog.setOAuthListener(new OAuthListener() {
			@Override
			public void onSuccess(Token token) {
				Log.d(TAG, "token: " + token.toString());
				if (token.isSessionValid()) {
					mKeeper.keepAccessToken(mContext, token);
				}
				if (l != null) {
					l.onSuccess(token);
				}
			}

			@Override
			public void onError(String error) {
				Log.d(TAG, "error: " + error);
				if (l != null) {
					l.onError(error);
				}
			}

			@Override
			public void onCancel() {
				Log.d(TAG, "cancel");
				if (l != null) {
					l.onCancel();
				}
			}
		});
		dialog.show();
	}
}

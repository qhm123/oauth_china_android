package org.mfactory.oauth_china_android;

import org.json.JSONException;
import org.json.JSONObject;
import org.mfactory.oauth_china_android.config.OAuthConfig;
import org.mfactory.oauth_china_android.token.Token;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint("SetJavaScriptEnabled")
class OAuthDialog extends Dialog {

	private static final String TAG = OAuthDialog.class.getSimpleName();

	private WebView mWebview;
	private String mUrl;
	private OAuthConfig mConfig;

	private OAuthListener mOAuthListener;

	public void setOAuthListener(OAuthListener l) {
		mOAuthListener = l;
	}

	public OAuthDialog(Context context, OAuthConfig config) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		mConfig = config;
		mUrl = mConfig.getCodeUrl();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oauth);

		mWebview = (WebView) findViewById(R.id.webview);
		mWebview.getSettings().setJavaScriptEnabled(true);
		mWebview.setWebViewClient(new OAuthWebViewClient());

		mWebview.loadUrl(mUrl);
	}

	@Override
	public void onBackPressed() {
		mOAuthListener.onCancel();
		super.onBackPressed();
	}

	public class OAuthWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			Log.d(TAG, "url: " + url);
			if (url.startsWith(mConfig.redirectUrl)) {
				view.stopLoading();
				Uri uri = Uri.parse(url);
				String error = uri.getQueryParameter("error");
				if (error != null) {
					Log.d(TAG, "error: " + error);
					if (error.equals("access_denied")) {
						mOAuthListener.onCancel();
					} else {
						mOAuthListener.onError(error);
					}
				} else {
					mConfig.getAccessCode(uri, new OAuthListener() {
						@Override
						public void onSuccess(Token token) {
							OAuthDialog.this.dismiss();
							mOAuthListener.onSuccess(token);
						}

						@Override
						public void onError(String error) {
						}

						@Override
						public void onCancel() {
						}
					});
				}
			}
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}
	}

}

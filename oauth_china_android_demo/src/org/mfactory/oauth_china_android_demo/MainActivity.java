package org.mfactory.oauth_china_android_demo;

import org.mfactory.oauth_china_android.OAuthChina;
import org.mfactory.oauth_china_android.OAuthListener;
import org.mfactory.oauth_china_android.config.DoubanConfig;
import org.mfactory.oauth_china_android.config.OAuthConfig;
import org.mfactory.oauth_china_android.config.QQConfig;
import org.mfactory.oauth_china_android.config.RenrenConfig;
import org.mfactory.oauth_china_android.config.SinaConfig;
import org.mfactory.oauth_china_android.config.TweiboConfig;
import org.mfactory.oauth_china_android.token.Token;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	protected static final String TAG = MainActivity.class.getSimpleName();

	private ListView mList;

	private OAuthConfig[] mConfigs = {
			new SinaConfig("2489004023", "990329a88e14d1ab133ac006e7e018f9",
					"http://www.sina.com"),
			new TweiboConfig("801321234", "a973690d0637d2b6ac64bc264bbb0e0d",
					"http://oauth_china_android.com"),
			new DoubanConfig(
					"0d61b79b8a334d3d282526d29e99e7b7",
					"572dff1e9323b6e3",
					"http://oauth_china_android",
					"movie_basic,movie_basic_r,movie_advance_r,book_basic_r,book_basic_w,music_basic_r,music_basic_w,douban_basic_common"),
			new RenrenConfig("a2ad8f23968d4fbc92c470bd1f7ba71e",
					"fc331b658ffb45de8c0eecef84990c66",
					"http://oauth_china_android.com", null),
			new QQConfig("100385103", "3322c4e134eeff6f5c36893edce7cd30",
					"http://oauth_china_android.com") };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mList = (ListView) findViewById(R.id.list);
		mList.setAdapter(new BaseAdapter() {

			class MyOAuthListener implements OAuthListener {

				TextView mTextView;

				public MyOAuthListener(TextView textView) {
					mTextView = textView;
				}

				@Override
				public void onSuccess(Token token) {
					Log.d(TAG, "token: " + token.toString());
					mTextView.setText("token: " + token.getAccessToken()
							+ ", uid: " + token.getUid());
				}

				@Override
				public void onError(String error) {
					mTextView.setText("error: " + error);
				}

				@Override
				public void onCancel() {
					mTextView.setText("cancel");

				}
			};

			class MyClickListener implements OnClickListener {

				int mPosition;
				TextView mTextView;
				MyOAuthListener mOAuthListener;

				public MyClickListener(int position, TextView textView) {
					mPosition = position;
					mTextView = textView;
					mOAuthListener = new MyOAuthListener(mTextView);
				}

				@Override
				public void onClick(View v) {
					OAuthChina oauthChina = getItem(mPosition);

					switch (v.getId()) {
					case R.id.login: {
						Token token = oauthChina.getToken();
						if (token != null) {
							// token存在
							if (token.isSessionValid()) {
								// token有效
								Log.d(TAG, "token: " + token.toString());
								mTextView.setText("token: "
										+ token.getAccessToken());
							} else {
								// token过期
								oauthChina.refreshToken(
										token.getRefreshToken(), mOAuthListener);
							}
						} else {
							// token不存在，开启OAuth验证
							oauthChina.startOAuth(mOAuthListener);
						}
					}
						break;
					case R.id.clear_token: {
						oauthChina.clearToken();
						mTextView.setText("clear");
					}
						break;
					case R.id.refresh_token: {
						Token token = oauthChina.getToken();
						if (token != null) {
							oauthChina.refreshToken(token.getRefreshToken(),
									mOAuthListener);
						}
					}
						break;
					}
				}
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = LayoutInflater.from(MainActivity.this)
							.inflate(R.layout.item_oauth, null);
				}

				TextView title = (TextView) convertView
						.findViewById(R.id.title);
				title.setText(getItem(position).getConfig().getClass()
						.getSimpleName());
				TextView tokenView = (TextView) convertView
						.findViewById(R.id.token);
				MyClickListener l = new MyClickListener(position, tokenView);
				Button login = (Button) convertView.findViewById(R.id.login);
				login.setOnClickListener(l);
				Button clearToken = (Button) convertView
						.findViewById(R.id.clear_token);
				clearToken.setOnClickListener(l);
				Button refreshToken = (Button) convertView
						.findViewById(R.id.refresh_token);
				refreshToken.setOnClickListener(l);

				return convertView;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public OAuthChina getItem(int position) {
				return new OAuthChina(MainActivity.this, mConfigs[position]);
			}

			@Override
			public int getCount() {
				return mConfigs.length;
			}
		});

	}
}

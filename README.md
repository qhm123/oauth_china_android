# oauth_china_android

## 简介
* 实现了新浪微博，腾讯微博，QQ，豆瓣，人人等平台的OAuth2验证
* 提供了统一的接口，简单方便的对接上述平台
* 采用加密方式持久化了OAuth2验证得到的access_token，expires_in，refresh_token，uid等信息

* ![image](http://ww4.sinaimg.cn/bmiddle/6414b943jw1e2bpysqf9nj.jpg)


## 说明
* oauth_china_android是android的library project，添加到android的project里面即可。
* oauth_china_android_demo是实现的demo。MainActivity中是所有平台的示例。SimpleActivity中演示了使用一个平台的简单的使用方法。demo中所有申请的appkey都是未发布的，所以没有被添加测试账号的人无法使用。

## 演示
<pre><code>// 实例化对应平台的OAuthConfig对象
OAuthConfig config = new SinaConfig("2489004023",
				"990329a88e14d1ab133ac006e7e018f9", "http://www.sina.com");
				
// 实例化OAuthChina对象，OAuthChina类是主要使用的类
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

OAuthListener mOAuthListener = new OAuthListener() {

	@Override
	public void onSuccess(Token token) {
		// 获取token成功
		Log.d(TAG, "token: " + token.toString());
	}

	@Override
	public void onError(String error) {
		// 获取token失败
		Log.d(TAG, "error: " + error);
	}

	@Override
	public void onCancel() {
		// 用户取消了操作
		Log.d(TAG, "cancel");

	}
};
</code></pre>

## 详细说明

<pre><code>OAuthConfig(String appKey, String appSecret, String redirectUrl)
</code></pre>

* appKey 对应平台的appkey
* appSecret 对应平台的appSecret
* redirectUrl 对应平台的回调地址

<pre><code>OAuthChina(Context context, OAuthConfig config)
</code></pre>

* context
* config 对应平台的OAuthConfig实例

<pre><code>public interface OAuthListener {

	void onSuccess(Token token);
	
	void onCancel();
	
	void onError(String error);
	
}
</code></pre>

* onSuccess OAuth验证成功后回调，token是返回的token信息
* onCancel 用户取消OAuth验证
* onError OAuth出现错误

<pre><code>class Token
</code></pre>

* 详见Token类代码

## API文档

* 新浪微博API文档: http://open.weibo.com/wiki/API%E6%96%87%E6%A1%A3_V2
* 腾讯微博API文档: http://wiki.open.t.qq.com/index.php/API%E6%96%87%E6%A1%A3
* QQ API文档: http://wiki.opensns.qq.com/wiki/%E3%80%90QQ%E7%99%BB%E5%BD%95%E3%80%91API%E6%96%87%E6%A1%A3
* 豆瓣API文档: http://developers.douban.com/wiki/?title=api_v2
* 人人API文档: http://wiki.dev.renren.com/wiki/API

## 依赖

[https://github.com/loopj/android-async-http](https://github.com/loopj/android-async-http) An asynchronous, callback-based Http client for Android built on top of Apache's HttpClient libraries.


## License

This program is free softwareyou can redistribute it and /or modify it under the terms of the GNU General Public License as published by the Free Software Foundataioneither version 2 of the License,or (at your option) any later version.

You should have read the GNU General Public License before start "RTFSC".

If not,see [http://www.gnu.org/licenses/](http://www.gnu.org/licenses/)
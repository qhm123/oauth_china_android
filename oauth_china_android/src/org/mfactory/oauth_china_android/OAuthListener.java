package org.mfactory.oauth_china_android;

import org.mfactory.oauth_china_android.token.Token;

public interface OAuthListener {
	void onSuccess(Token token);

	void onCancel();

	void onError(String error);
}
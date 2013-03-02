package org.mfactory.oauth_china_android.token;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QQToken extends Token {

	public QQToken(String response) {
		// access_token=FDEC0457B146137F5712F98420176C58&expires_in=7776000
		Pattern pattern = Pattern.compile("access_token=(.*)&expires_in=(.*)");
		Matcher m = pattern.matcher(response);
		if (m.matches() && m.groupCount() == 2) {
			setAccessToken(m.group(1));
			setExpiresIn(Long.valueOf(m.group(2)));
		}
	}
}

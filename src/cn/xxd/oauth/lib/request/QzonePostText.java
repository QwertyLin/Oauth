package cn.xxd.oauth.lib.request;

import java.io.IOException;
import java.net.URLEncoder;

import cn.xxd.oauth.lib.Token;
import cn.xxd.oauth.lib.UnAuthException;


public class QzonePostText extends QzoneHandle {

	public static String postText(Token token, String text) throws UnAuthException, IOException {
		if(token == null || isTokenExpire(token)){
			throw new UnAuthException();
		}
		String param = 
				"oauth_consumer_key=" + CLIENT_ID
				+ "&access_token=" + token.getToken()
				+ "&openid=" + token.getId()
				+ "&con=" + URLEncoder.encode(text, "utf-8")
				+ "&third_source=1"
				;
		return httpPost("https://graph.qq.com/shuoshuo/add_topic", param);
	}
}

package cn.xxd.oauth.lib.request;

import java.io.IOException;
import java.net.URLEncoder;

import cn.xxd.oauth.lib.Token;
import cn.xxd.oauth.lib.UnAuthException;


public class RenrenPostText extends RenrenHandle {
	
	public static String postText(Token token, String text) throws UnAuthException, IOException {
		if(token == null || isTokenExpire(token)){
			throw new UnAuthException();
		}
		String md5 = md5(
				"access_token="+token.getToken()
				+ "format=JSON"
				+ "method=status.set"
				+ "status="+text
				+ "v=1.0"
				+ RENREN_CLIENT_SECRET);
		String param = "&access_token=" + token.getToken()
				+ "&format=JSON"
				+ "&method=status.set"
				+ "&status="+URLEncoder.encode(text, "utf-8")
				+ "&v=1.0"
				+ "&sig="+md5;
		return httpPost("http://api.renren.com/restserver.do", param);
	}

}

package cn.xxd.oauth.lib.request;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Random;

import cn.xxd.oauth.lib.Token;
import cn.xxd.oauth.lib.UnAuthException;


public class SinaPostText extends SinaHandle {
	
	public static String postText(Token token, String text, String lat, String lng) throws UnAuthException, IOException {
		if(token == null || isTokenExpire(token)){
			throw new UnAuthException();
		}
		//space repeat
		int space = new Random().nextInt(50);
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < space; i++){
			sb.append(" ");
		}
		String param = "access_token=" + token.getToken() + "&status=" + URLEncoder.encode(text + sb, "utf-8");
		//
		if(lat != null && lng != null){
			param += "&lat=" + lat + "&long=" + lng;
		}
		//
		return httpPost("https://api.weibo.com/2/statuses/update.json", param);
	}

}

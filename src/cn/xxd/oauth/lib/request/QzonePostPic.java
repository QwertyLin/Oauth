package cn.xxd.oauth.lib.request;

import java.io.IOException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.xxd.oauth.lib.Token;
import cn.xxd.oauth.lib.UnAuthException;


public class QzonePostPic extends QzoneHandle {

	public static void postPic(Token token, String album, String text, String pic) throws UnAuthException, Exception{
		if(token == null || isTokenExpire(token)){
			throw new UnAuthException();
		}
		//check album
		String albumId = null;
		String pictureId = null;
		String pictureIdSmall = null;
		//
		String result = httpGet(
				"https://graph.qq.com/photo/list_album?oauth_consumer_key=" + CLIENT_ID 
				+ "&access_token=" + token.getToken() 
				+ "&openid=" + token.getId());
		//System.out.println("result:" + result);
		if(!result.contains("ret\" : 0")){
			throw new IOException();
		}
		JSONObject jsonO = new JSONObject(result);
		if(jsonO.getInt("albumnum") != 0 ){
			JSONArray jsonA = jsonO.getJSONArray("album");
			for(int i = 0, size = jsonA.length(); i < size; i++){
				jsonO = jsonA.getJSONObject(i);
				if(jsonO.getString("name").equals(album)){
					albumId = jsonO.getString("albumid");
					break;
				}
			}
		}
		if(albumId == null){
			//System.out.println("create album");
			String albumName = URLEncoder.encode(album, "utf-8");
			String param = 
					"oauth_consumer_key=" + CLIENT_ID
					+ "&access_token=" + token.getToken()
					+ "&openid=" + token.getId()
					+ "&albumname=" + albumName
					+ "&albumdesc=" + albumName
					;
			//System.out.println(param);
			result = httpPost("https://graph.qq.com/photo/add_album", param);
			//System.out.println(result);
			if(!result.contains("ret\" : 0")){
				throw new IOException();
			}
			jsonO = new JSONObject("result:" + result);
			albumId = jsonO.getString("albumid");
		}
		if(albumId == null){
			throw new IOException();
		}
		//post pic
		String boundary = "-----114975832116442893661388290519";
		StringBuffer params = new StringBuffer();
		boundary = "\r\n" + "--" + boundary + "\r\n";
		//
		params.append(boundary);
		params.append("Content-Disposition: form-data; name=\"" + "access_token" + "\"\r\n\r\n");
        params.append(token.getToken());
        //
        params.append(boundary);
		params.append("Content-Disposition: form-data; name=\"" + "oauth_consumer_key" + "\"\r\n\r\n");
        params.append(CLIENT_ID);
        //
        params.append(boundary);
		params.append("Content-Disposition: form-data; name=\"" + "openid" + "\"\r\n\r\n");
        params.append(token.getId());
        //
        params.append(boundary);
		params.append("Content-Disposition: form-data; name=\"" + "photodesc" + "\"\r\n\r\n");
        params.append(text);
        //
        params.append(boundary);
		params.append("Content-Disposition: form-data; name=\"" + "title" + "\"\r\n\r\n");
        params.append(System.currentTimeMillis() + ".png");
        //
        params.append(boundary);
		params.append("Content-Disposition: form-data; name=\"" + "picture" + "\"; filename=\"" + "pic.png" + "\"\r\n");
        params.append("Content-Type: " + "image/x-png" + "\r\n\r\n");
        //
		result = httpPost("https://graph.qq.com/photo/upload_pic", params.toString(), "-----114975832116442893661388290519", pic);
		//post text
		//System.out.println("result:" + result);
		if(result == null || !result.contains("ret\" : 0,")){
			throw new IOException();
		}
		jsonO = new JSONObject(result);
		pictureId = jsonO.getString("lloc");
		pictureIdSmall = jsonO.getString("sloc");
		if(pictureId == null || pictureIdSmall == null){
			throw new IOException();
		}
		//
		String param = 
				"oauth_consumer_key=" + CLIENT_ID
				+ "&access_token=" + token.getToken()
				+ "&openid=" + token.getId()
				+ "&con=" + URLEncoder.encode(text, "utf-8")
				+ "&third_source=1"
				+ "&richtype=1"
				+ "&richval=" + albumId + "," + pictureId + "," + pictureIdSmall
				;
		result = httpPost("https://graph.qq.com/shuoshuo/add_topic", param);
		//System.out.println("result:" + result);
		if(result == null || !result.contains("<ret>0</ret>")){
			throw new IOException();
		}
    }
}

package a;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.xxd.oauth.lib.OauthHandle;
import cn.xxd.oauth.lib.OnOauthListener;
import cn.xxd.oauth.lib.Token;
import cn.xxd.oauth.lib.TokenSqilte;
import cn.xxd.oauth.lib.UnAuthException;
import cn.xxd.oauth.lib.request.QweiboPostPic;
import cn.xxd.oauth.lib.request.QweiboPostText;
import cn.xxd.oauth.lib.request.QzonePostPic;
import cn.xxd.oauth.lib.request.QzonePostText;
import cn.xxd.oauth.lib.request.RenrenPostPic;
import cn.xxd.oauth.lib.request.RenrenPostText;
import cn.xxd.oauth.lib.request.SinaPostPic;
import cn.xxd.oauth.lib.request.SinaPostText;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class OauthHelper {
	
	private static final int 
	TYPE_SINA = OauthHandle.TYPE_SINA, //http://open.weibo.com/
	TYPE_QWEIBO = OauthHandle.TYPE_QWEIBO, //http://dev.t.qq.com/
	TYPE_QZONE = OauthHandle.TYPE_QZONE, //http://opensns.qq.com/
	TYPE_RENREN = OauthHandle.TYPE_RENREN;
	
	public OauthHelper(final Context ctx, final OnOauthListener listener, final OauthHandle handle, final WebView webView){
		WebSettings set = webView.getSettings();
		set.setJavaScriptEnabled(true);
		set.setSupportZoom(true);
		set.setBuiltInZoomControls(true);
		set.setCacheMode(WebSettings.LOAD_NO_CACHE);
		//
		webView.setWebChromeClient(new WebChromeClient(){
			private boolean isLoadingFinish;
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (!isLoadingFinish && newProgress > 30) {
					isLoadingFinish = true;
					listener.onOauthLoadingFinish();
				}
			}
		});
		//get auth
		WebViewClient wvc = new WebViewClient() {
			int index = 0;
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				//QLog.log(this, "url:" + url);
				Pattern p = Pattern.compile(handle.getUrlParsePattern());
				Matcher m = p.matcher(url);
				if (m.find() && index == 0) {
					index++;
					listener.onOauthAuthing();
					CookieManager.getInstance().removeAllCookie();//clean cookie
					webView.setVisibility(View.GONE);
					handle.parseUrl(ctx, m, listener);
				}
			}
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				//QLog.log(this, "shouldOverrideUrlLoading url=" + url);
				if(url.contains("error_uri")  //sina
					|| url.contains("checkType=error") //QQ
					|| url.contains("error=login_denied") //renren
				){
					return true;
				}
				return super.shouldOverrideUrlLoading(view, url);
			}
		};
		webView.setWebViewClient(wvc);
		webView.loadUrl(handle.getAuthUrl());
	}
	
	public static List<Token> getTokens(Context ctx){
		TokenSqilte sqlite = new TokenSqilte(ctx);
		sqlite.open(false);
		List<Token> list = sqlite.queryAll();
		sqlite.close();
		return list;
	}
	
	public static Token getToken(Context ctx, int type){
		TokenSqilte sqlite = new TokenSqilte(ctx);
		sqlite.open(false);
		Token token = sqlite.queryOne(type);
		sqlite.close();
		return token;
	}
	
	public static void deleteTokenByType(Context ctx, int type){
		TokenSqilte sqlite = new TokenSqilte(ctx);
		sqlite.open(true);
		sqlite.deleteByType(type);
		sqlite.close();
	}
	
	public static void postText(int type, Token token, String text, String lat, String lng) throws UnAuthException, IOException{
		switch(type){
		case TYPE_SINA:
			SinaPostText.postText(token, text, lat, lng);
			break;
		case TYPE_QWEIBO:
			QweiboPostText.postText(token, text);
			break;
		case TYPE_QZONE:
			QzonePostText.postText(token, text);
			break;
		case TYPE_RENREN:
			RenrenPostText.postText(token, text);
			break;
		}
	}
	
	public static void postPic(int type, Token token, String text, String pic, String lat, String lng) throws UnAuthException, Exception{
		switch(type){
		case TYPE_SINA:
			SinaPostPic.postPic(token, text, pic, lat, lng);
			break;
		case TYPE_QWEIBO:
			QweiboPostPic.postPic(token, text, pic);
			break;
		case TYPE_QZONE:
			QzonePostPic.postPic(token, "相册名", text, pic);
			break;
		case TYPE_RENREN:
			RenrenPostPic.postPic(token, text, pic);
			break;
		}
	}
	
	

}

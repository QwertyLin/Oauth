package cn.xxd.oauth.lib;

public interface OnOauthListener {
	
	void onWvOauthLoadingFinish();
	void onWvOauthAuthing();
	void onWvOauthSuccess(Token token);
	void onWvOauthError();

}
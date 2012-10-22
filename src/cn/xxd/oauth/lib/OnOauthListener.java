package cn.xxd.oauth.lib;

public interface OnOauthListener {
	
	void onOauthLoadingFinish();
	void onOauthAuthing();
	void onOauthSuccess(Token token);
	void onOauthError();

}
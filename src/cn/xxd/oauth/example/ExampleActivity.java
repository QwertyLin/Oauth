package cn.xxd.oauth.example;

import cn.xxd.oauth.R;
import cn.xxd.oauth.lib.OnOauthListener;
import cn.xxd.oauth.lib.Token;
import cn.xxd.oauth.lib.request.SinaHandle;
import a1.OauthHelper;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ExampleActivity extends Activity implements OnOauthListener {
	
	private ProgressBar pb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example);
        pb = (ProgressBar)findViewById(R.id.oauth_pb);
        new OauthHelper(this, this, new SinaHandle(), (WebView)findViewById(R.id.oauth_web));
    }

	@Override
	public void onWvOauthLoadingFinish() {
		pb.setVisibility(View.GONE);
	}

	@Override
	public void onWvOauthAuthing() {
		pb.setVisibility(View.VISIBLE);
	}

	@Override
	public void onWvOauthSuccess(Token token) {
		Toast.makeText(this, "绑定成功", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onWvOauthError() {
		// TODO Auto-generated method stub
		
	}

}
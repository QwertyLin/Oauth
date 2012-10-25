package cn.xxd.oauth.example;

import cn.xxd.oauth.R;
import cn.xxd.oauth.lib.OnOauthListener;
import cn.xxd.oauth.lib.Token;
import cn.xxd.oauth.lib.request.SinaHandle;
import a.OauthHelper;
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
	public void onOauthLoadingFinish() {
		pb.setVisibility(View.GONE);
	}

	@Override
	public void onOauthAuthing() {
		pb.setVisibility(View.VISIBLE);
	}

	@Override
	public void onOauthSuccess(Token token) {
		Toast.makeText(this, "绑定成功", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onOauthError() {
		// TODO Auto-generated method stub
		
	}

}

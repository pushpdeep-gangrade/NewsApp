package mad.uncc.homework5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        setTitle(R.string.web_view);
        if(getIntent()!=null && getIntent().getExtras()!=null) {
            String URL = getIntent().getStringExtra("URL");
            WebView browser = (WebView) findViewById(R.id.webView);
            browser.loadUrl(URL);
        }
        else
            Toast.makeText(this, "No url found", Toast.LENGTH_SHORT).show();
    }
}

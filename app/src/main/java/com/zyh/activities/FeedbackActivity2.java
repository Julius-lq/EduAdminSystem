package com.zyh.activities;

//import static com.zyh.utills.Utils.LoadingDialog;
//import static com.zyh.utills.Utils.loadDialog;;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.material.tabs.TabItem;
import com.zyh.R;

import java.util.Objects;

//这个feedbackActivity接入兔小巢，可以看用户反馈，若是反响不太好则回退到第一个feedbackActivity
public class FeedbackActivity2 extends AppCompatActivity {

    private Toolbar toolbar;

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback2);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("长理教务");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        LoadingDialog(this);
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);       // 这个要加上

        /* 获得 webview url，请注意url单词是product而不是products，products是旧版本的参数，用错地址将不能成功提交 */
        String url = "https://support.qq.com/product/400072";

        /* WebView 内嵌 Client 可以在APP内打开网页而不是跳出到浏览器 */
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                super.shouldOverrideUrlLoading(view, url);
                view.loadUrl(url);
                return true;
            }
        };
        webView.setWebViewClient(webViewClient);

        webView.loadUrl(url);

        TabItem refresh =(TabItem)findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl(url);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
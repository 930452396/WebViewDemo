package com.example.administrator.webviewdemo;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private WebView mainWebview;
    private FrameLayout mainVideoFullView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebChromeClient wvcc = new WebChromeClient();
        mainWebview = (WebView) findViewById(R.id.main_webview);
        mainVideoFullView = (FrameLayout) findViewById(R.id.main_video_fullView);


        WebSettings webSettings = mainWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true); // 关键点
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); // 不加载缓存内容

        mainWebview.setWebChromeClient(wvcc);
        //设置不用系统浏览器打开,直接显示在当前Webview
        mainWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        mainWebview.setWebChromeClient(new WebChromeClient() {

            /*** 视频播放相关的方法 **/

            @Override
            public View getVideoLoadingProgressView() {
//                FrameLayout frameLayout = new FrameLayout(WebVideoActivity.this);
//                frameLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                return null;
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
//                showCustomView(view, callback);
            }

            @Override
            public void onHideCustomView() {
//                hideCustomView();
            }
        });
        mainWebview.loadUrl("http://www.iqiyi.com/v_19rrht3jow.html");

    }

    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mainWebview.canGoBack()) {
            mainWebview.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainVideoFullView.removeAllViews();
        mainWebview.loadUrl("about:blank");
        mainWebview.stopLoading();
        mainWebview.setWebChromeClient(null);
        mainWebview.setWebViewClient(null);
        mainWebview.destroy();
        mainWebview = null;
    }



//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (inCustomView()) {
//                // webViewDetails.loadUrl("about:blank");
//                hideCustomView();
//                return true;
//            } else {
//                webView.loadUrl("about:blank");
//                MainActivity.this.finish();
//            }
//        }
//        return false;
//    }
}

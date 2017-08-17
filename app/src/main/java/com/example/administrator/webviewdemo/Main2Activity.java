package com.example.administrator.webviewdemo;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "Main2Activity";

    private WebView webview_content;
    private FrameLayout fl_full_video;
    private View xCustomView;
    private WebChromeClient.CustomViewCallback xCustomViewCallback;
    private Main2Activity.myWebChromeClient myWebChromeClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        webview_content = (WebView) findViewById(R.id.webview_content);
        fl_full_video = (FrameLayout) findViewById(R.id.fl_full_video);

        WebSettings webSettings = webview_content.getSettings();

        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//设置缓存模式

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setJavaScriptEnabled(true);


        //设置不用系统浏览器打开,直接显示在当前Webview
        webview_content.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e(TAG, "shouldOverrideUrlLoading: ==》"+url );
                view.loadUrl(url);
                return true;
            }
        });

        //设置WebChromeClient类,浏览器的监听。就想js里面可以监听window什么时候加载完一样。这里面的回调方法都是可以获取到html里面的元素或者状态的回调
        myWebChromeClient = new myWebChromeClient();
        webview_content.setWebChromeClient(myWebChromeClient);


        //设置WebViewClient类
        webview_content.setWebViewClient(new WebViewClient() {
            //设置加载前的函数
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e(TAG, "shouldOverrideUrlLoading: ==》"+url );
                view.loadUrl(url);
                return true;
            }

            //设置结束加载函数
            @Override
            public void onPageFinished(WebView view, String url) {

            }
        });
        webview_content.loadUrl("http://www.iqiyi.com/v_19rrht3jow.html");

    }

    @Override
    protected void onResume() {
        super.onResume();
        webview_content.onResume();
        webview_content.resumeTimers();
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        webview_content.onPause();
        webview_content.pauseTimers();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        webview_content.removeAllViews();
        webview_content.loadUrl("about:blank");
        webview_content.stopLoading();
        webview_content.setWebChromeClient(null);
        webview_content.setWebViewClient(null);
        webview_content.destroy();
        webview_content = null;
    }

//    点击返回上一页面而不是退出浏览器
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && webview_content.canGoBack()) {
//            webview_content.goBack();
//            return true;
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }

    public boolean inCustomView() {
        return (xCustomView != null);
    }
    public void hideCustomView() {
        myWebChromeClient.onHideCustomView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (inCustomView()) {
                // webViewDetails.loadUrl("about:blank");
                hideCustomView();
                return true;
            } else {
                webview_content.loadUrl("about:blank");
            }
        }
        return false;
    }

    public class myWebChromeClient extends WebChromeClient {
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            //这个view，估计就是点击全屏后显示的那个view。只要找一个容器去接收它。就能拿到该view的视图
//            goneBar();
//
//            mWindowAttrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//            getWindow().setAttributes(mWindowAttrs);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//			webView.setVisibility(View.INVISIBLE);
            //如果一个视图已经存在，那么立刻终止并新建一个
            if (xCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            fl_full_video.addView(view);
            xCustomView = view;
            xCustomViewCallback = callback;
            fl_full_video.setVisibility(View.VISIBLE);
        }
        @Override
        public void onHideCustomView() {
            if (xCustomView == null)
                return;
//            showBar();
//
//            mWindowAttrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            getWindow().setAttributes(mWindowAttrs);
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            xCustomView.setVisibility(View.GONE);
            fl_full_video.removeView(xCustomView);
            xCustomView = null;
            fl_full_video.setVisibility(View.GONE);
            //新建一个视图（感觉就是webview）
            xCustomViewCallback.onCustomViewHidden();
//			webView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(TAG, "onConfigurationChanged");
    }


}

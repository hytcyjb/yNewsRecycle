package yjbo.yy.ynewsrecycle.mainutil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yjbo.yy.ynewsrecycle.R;

/**
 * webview的activity基类
 * 必须要内部有下面这2个布局
 * <include layout="@layout/layout_title_public_common" />
 * <include layout="@layout/layout_web_public_common" />
 * Created by yjbo on 2016/9/18.
 */
public class debugWebActivity extends AppCompatActivity {
    private final int handleMsg_12 = 12;
    public String mUrl = "http://www.baidu.com";//原始url
    @Bind(R.id.back_public_txt)
    TextView backPublicTxt;
    @Bind(R.id.finish_public_txt)
    TextView finishPublicTxt;
    @Bind(R.id.title_public)
    TextView titlePublic;
    private ArrayList<String> loadHistoryUrls = new ArrayList<String>();
    public WebView webView;
    public ProgressBar webview_progress;
    public TextView tx_refresh;

    private int count = 0;
    private String mTitle = "";//上个页面传的标题
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
//        System.out.println(1/0);
        initView();
    }

    @SuppressLint({"AddJavascriptInterface", "JavascriptInterface"})
    private void initView() {
        initWidgetView();
        initData();
    }


    //初始化控件
    private void initWidgetView() {
        webView = (WebView) findViewById(R.id.swipe_target);
        webview_progress = (ProgressBar) findViewById(R.id.webview_progress);
        tx_refresh = (TextView) findViewById(R.id.tx_refresh);

        Drawable drawable = getResources().getDrawable(R.drawable.color_blue_progressbar);
        webview_progress.setProgressDrawable(drawable);
    }


    private void inittopView() {
        titlePublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRefresh();
            }
        });
        tx_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRefresh();
            }
        });
    }
    /**
     * 刷新事件
     * @author yjbo  @time 2017/4/9 20:26
     */
    private void clickRefresh() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            exitTime = System.currentTimeMillis();
            count = 0;
        } else {
            count++;
            if (count == 2) {
                count = 0;
                onRefresh();
            }
        }
    }

    @OnClick({R.id.back_public_txt, R.id.finish_public_txt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_public_txt:
                back(0);
                break;
            case R.id.finish_public_txt:
                back(1);
                break;
        }
    }

    class InJavaScriptLocalObj {
        /**
         * 刷新，返回功能
         * kind： 1 仅返回
         * 2 仅刷新
         * 3 返回加刷新
         *
         * @author yjbo  @time 2017/1/10 15:31
         */
        @JavascriptInterface
        public void goback(String kind) {
            LogUtils.d("-h5返回--" + kind);
            Message msg = new Message();
            msg.what = handleMsg_12;
            msg.obj = kind;
            mhandler.sendMessage(msg);
        }
    }


    /**
     * 初始化webview
     */
    private void initData() {
        Intent intent = this.getIntent();
        String url = intent.getStringExtra("URL");
        mTitle = intent.getStringExtra("Title");

        mUrl = url;

        inittopView();

        try {
            clearCache1();
            webView.loadUrl(mUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        settings.setJavaScriptCanOpenWindowsAutomatically(true);//允许js弹出窗口
        settings.setUseWideViewPort(true);
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        //引用http://blog.csdn.net/sensky_yuan/article/details/17356843
        //关闭硬件加速;sdk19以下（不包含19）不设置这个就会出现不手动点击第一个页面不出现的情况；
        if (Build.VERSION.SDK_INT >= 19) {
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        //设置 缓存模式
        //settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {//设置是否自动加载图片
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url == null || url.length() == 0) {
                    return false;
                }
                if (url.contains("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    startActivity(intent);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                try {
                    Log.d("onReceivedError=webView", "errorCode =" + errorCode + ";description" + description);
                    view.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                Message msg = mhandler.obtainMessage();
                msg.what = 0;
                mhandler.sendMessage(msg);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (webView != null && !webView.getSettings().getLoadsImagesAutomatically()) {
                    webView.getSettings().setLoadsImagesAutomatically(true);
                }
                if (url != null && url.startsWith("http") && loadHistoryUrls != null && !loadHistoryUrls.contains(url)) {
                    loadHistoryUrls.add(url);
                }

                Message msg = mhandler.obtainMessage();
                msg.what = 1;
                mhandler.sendMessage(msg);
                super.onPageFinished(view, url);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url + "", message + "", result);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    Message msg = mhandler.obtainMessage();
                    msg.what = 1;
                    mhandler.sendMessage(msg);
                } else {
                    Message msg = mhandler.obtainMessage();
                    msg.what = 3;
                    msg.obj = newProgress + "";
                    mhandler.sendMessage(msg);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                titlePublic.setText(title);
            }


        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    WeakHandler mhandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String result = (String) msg.obj;
            switch (msg.what) {
                case 0:
                    webview_progress.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    //点击一下，获取焦点
                    webview_progress.setVisibility(View.GONE);
                    break;
                case 3:
                    webview_progress.setVisibility(View.VISIBLE);
                    webview_progress.setProgress(Integer.valueOf(result));
                    break;
                case 6:
                    webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByTagName('head')[0].innerHTML);");
                    break;
                case handleMsg_12://返回和刷新的接口定义
                    if ("1".equals(result)) {//仅返回
                        back(0);
                    } else if ("2".equals(result)) {//仅刷新
                        webView.reload();
                    } else if ("3".equals(result)) {//返回加刷新
                        if (webView.canGoBack()) {
                            webView.goBack();
                        } else {
                            finish();
                        }
                    }
                    break;
            }
            return false;
        }
    });


    //一步一步地返回
    private void back(int closeInt) {
        if (closeInt == 0) {//返回上一级
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                this.finish();
            }
        } else if (closeInt == 1) {//直接关闭
            finish();
        }
    }


    public void onRefresh() {
        inittopView();
        clearCache1();
        webView.reload();
    }

    /**
     * 清理缓存 因为有点问题-
     *
     * @author yjbo
     * @time 2017/1/5 18:53
     * @mail 1457521527@qq.com
     */
    private void clearCache1() {
        deleteDatabase("webview.db");
        deleteDatabase("webviewCache.db");
    }

    /**
     * 返回键监听
     * @author yjbo  @time 2017/4/9 20:28
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back(0);
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        mhandler.removeCallbacksAndMessages(null);
        //关闭声音的http://www.bubuko.com/infodetail-1110920.html
        try {
            webView.stopLoading();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            clearCache1();
            webView.removeAllViews();
            webView.destroy();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        super.onDestroy();
    }
}

package yjbo.yy.ynewsrecycle.splash;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatReportStrategy;
import com.tencent.stat.StatService;

import net.youmi.android.AdManager;
import net.youmi.android.normal.common.ErrorCode;
import net.youmi.android.normal.spot.SplashViewSettings;
import net.youmi.android.normal.spot.SpotListener;
import net.youmi.android.normal.spot.SpotManager;

import yjbo.yy.ynewsrecycle.R;
import yjbo.yy.ynewsrecycle.main.MainActivity;
import yjbo.yy.ynewsrecycle.mainutil.LogUtils;
import yjbo.yy.ynewsrecycle.mainutil.PermissionHelper;

/**
 * 启动页面--同时也是为了尝试接入有米sdk；
 *
 * @author yjbo
 * @time 2017/4/4 11:00
 */
public class SplashActivity extends AppCompatActivity {

    private PermissionHelper mPermissionHelper;
    private String TAG = "yjbo";
    private String flag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 移除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_splash);

        Intent intent = this.getIntent();
        flag = intent.getStringExtra("otherFlag");

        // 当系统为6.0以上时，需要申请权限
        mPermissionHelper = new PermissionHelper(this);
        mPermissionHelper.setOnApplyPermissionListener(new PermissionHelper.OnApplyPermissionListener() {
            @Override
            public void onAfterApplyAllPermission() {
                Log.i(TAG, "All of requested permissions has been granted, so run app logic.");
                runApp();
            }
        });
        if (Build.VERSION.SDK_INT < 23) {
            // 如果系统版本低于23，直接跑应用的逻辑
            Log.d(TAG, "The api level of system is lower than 23, so run app logic directly.");
            runApp();
        } else {
            // 如果权限全部申请了，那就直接跑应用逻辑
            if (mPermissionHelper.isAllRequestedPermissionGranted()) {
                Log.d(TAG, "All of requested permissions has been granted, so run app logic directly.");
                runApp();
            } else {
                // 如果还有权限为申请，而且系统版本大于23，执行申请权限逻辑
                Log.i(TAG, "Some of requested permissions hasn't been granted, so apply permissions first.");
                mPermissionHelper.applyPermissions();
            }
        }
        // androidManifest.xml指定本activity最先启动
        // 因此，MTA的初始化工作需要在本onCreate中进行
        // 在startStatService之前调用StatConfig配置类接口，使得MTA配置及时生效
//        initMTAConfig(true);
        String appkey = "AL575X6QFZCN";
        // 初始化并启动MTA
        // 第三方SDK必须按以下代码初始化MTA，其中appkey为规定的格式或MTA分配的代码。
        // 其它普通的app可自行选择是否调用
        StatConfig.setAutoExceptionCaught(false); // 禁止捕获app未处理的异常
        StatConfig.setEnableSmartReporting(true); // 禁止WIFI网络实时上报
        StatConfig.setSendPeriodMinutes(24 * 60); // PERIOD间隔周期，24小时
        StatConfig.setStatSendStrategy(StatReportStrategy.PERIOD); //

        try {         // 第三个参数必须为：com.tencent.stat.common.StatConstants.VERSION
             StatService.startStatService(this, appkey, com.tencent.stat.common.StatConstants.VERSION);
            LogUtils.e("MTA start success.yjbo");
        } catch (MtaSDkException e) {
            // MTA初始化失败
            LogUtils.e("MTA start failed.");
            LogUtils.e("e");
        }
        StatService.trackCustomEvent(this, "launch_activity", "SplashActivity");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPermissionHelper.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 跑应用的逻辑
     */
    private void runApp() {
        //初始化SDK
        AdManager.getInstance(SplashActivity.this).init("71ec846a13123737", "fe3bf3bf0a7f1a74",
                true, true);//boolean isTestModel 是否开启测试模式
        //设置开屏
        setupSplashAd();
    }

    /**
     * 设置开屏广告
     */
    private void setupSplashAd() {
        // 创建开屏容器
        final RelativeLayout splashLayout = (RelativeLayout) findViewById(R.id.rl_splash);
//        RelativeLayout.LayoutParams params =
//                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        params.addRule(RelativeLayout.ABOVE, R.id.view_divider);

        // 对开屏进行设置
        SplashViewSettings splashViewSettings = new SplashViewSettings();
        // 设置是否展示失败自动跳转，默认自动跳转
        splashViewSettings.setAutoJumpToTargetWhenShowFailed(true);
        // 设置跳转的窗口类
        splashViewSettings.setTargetClass(MainActivity.class);
        // 设置开屏的容器
        splashViewSettings.setSplashViewContainer(splashLayout);

        // 展示开屏广告
        SpotManager.getInstance(SplashActivity.this)
                .showSplash(SplashActivity.this, splashViewSettings, new SpotListener() {

                    @Override
                    public void onShowSuccess() {
                        logInfo("开屏展示成功");
                    }

                    @Override
                    public void onShowFailed(int errorCode) {
                        logError("开屏展示失败" + errorCode);
                        switch (errorCode) {
                            case ErrorCode.NON_NETWORK:
                                logError("网络异常");
                                break;
                            case ErrorCode.NON_AD:
                                logError("暂无开屏广告");
                                break;
                            case ErrorCode.RESOURCE_NOT_READY:
                                logError("开屏资源还没准备好");
                                break;
                            case ErrorCode.SHOW_INTERVAL_LIMITED:
                                logError("开屏展示间隔限制");
                                break;
                            case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
                                logError("开屏控件处在不可见状态");
                                break;
                            default:
                                logError("errorCode: " + errorCode);
                                break;
                        }
                    }

                    @Override
                    public void onSpotClosed() {
                        logDebug("开屏被关闭");
                    }

                    @Override
                    public void onSpotClicked(boolean isWebPage) {
                        logDebug("开屏被点击");
                        String str = "是否是网页广告？" + (isWebPage ? "是" : "不是");
                        logInfo(str);
                    }
                });
    }

    private void logDebug(String str) {
        logInfo(str);
    }

    private void logError(String str) {
        logInfo(str);
    }

    private void logInfo(String str) {
        Log.d("yjbo", str);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 页面开始
        StatService.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // 页面结束
        StatService.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 开屏展示界面的 onDestroy() 回调方法中调用
        SpotManager.getInstance(SplashActivity.this).onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if ("HomeFragment".equals(flag)) {
                    setResult(1001);
                }
                finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}

package yjbo.yy.ynewsrecycle.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.youmi.android.normal.banner.BannerManager;
import net.youmi.android.normal.spot.SpotManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yjbo.yy.ynewsrecycle.R;
import yjbo.yy.ynewsrecycle.mainApplication;
import yjbo.yy.ynewsrecycle.mainutil.WeakHandler;

/**
 * 项目主要完成内容：
 * 1.完成数据在csdn上的接口展示；（或者GitHub）
 * 2.完成recycleview的封装在app中的使用；
 * 3.完成新闻的滑动，动态添加的功能；
 * 4.技术上需要引入databading
 * 5.加入了有米广告配置：https://app.youmi.net//sdk/android/17/doc/701/2/cn/有米AndroidSDK通用基本配置文档.html
 * 6.文字生成图片：参考：http://www.qt86.com/ai.php；我的logo也是这样生成的
 * 7.12生肖的图片：http://www.gkstk.com/article/1356407209171.html#touch
 * @author yjbo
 * @time 2017/4/3 10:45
 */
public class MainActivity extends AppCompatActivity implements BackHandledInterface {

    private BackHandledFragment mBackHandedFragment;
    @Bind(R.id.home_image)
    ImageView homeImage;
    @Bind(R.id.home_txt)
    TextView homeTxt;
    @Bind(R.id.bag_image)
    ImageView bagImage;
    @Bind(R.id.bag_txt)
    TextView bagTxt;
    @Bind(R.id.biaogan_image)
    ImageView biaoganImage;
    @Bind(R.id.biaogan_txt)
    TextView biaoganTxt;
    @Bind(R.id.my_image)
    ImageView myImage;
    @Bind(R.id.my_txt)
    TextView myTxt;
    @Bind(R.id.hui_image)
    ImageView huiImage;
    mainApplication bagApplication;
    private FragmentManager fragmentManager;
    private HomeFragment homePageFragment;
    private OtherFragment newModelFragment;
    private OtherFragment navigationFragment;
    private OtherFragment applyFragment;
    private MyFragment myFragment;
    private int selectCheckedId = 0;
    private LocalBroadcastManager broadcastManager;
    private RefreshListReceiver receiver;
    NetStateRec connectionReceiver = null;
    boolean isNet = false;
    /**
     * @param resultState
     * -1 注销之后
     * 0  正常情况
     * 1  无账号情况下进行登录
     **/
    private int resultState = 0;


    /**
     * 监听是否有网的广播
     *
     * @author yjbo  @time 2017/2/10 14:18
     */
    class NetStateRec extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //暂时只给首页做网络的判断
            if (homePageFragment != null && homePageFragment.isAdded()) {//selectCheckedId == 0 &&
                chooseMode();
            }
        }
    }

    //是否有网时进行判断
    private void chooseMode() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getSupportActionBar().hide();

        fragmentManager = getSupportFragmentManager();

        broadcastManager = LocalBroadcastManager.getInstance(MainActivity.this);
        receiver = new RefreshListReceiver();
        //判断有无网络
        //监听网络事件
        connectionReceiver = new NetStateRec();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, intentFilter);


        reLogin();

        checkedChangeLister(0);

        //或者异步加载2016年12月25日17:24:40
        checkForUpdate();

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
    /**
     * 登录
     * 如果没有账号，或者没有密码就不登录了；
     *
     * @author yjbo  @time 2017/3/28 10:24
     */
    private void reLogin() {
    }

    /**
     * 登录openfire服务器
     */
    private void login2OpenFire() throws Exception {
    }


    //这里是为了判断注销获取重新登录账号后是否点击过了首页面。不让其频繁点击
    private boolean isClickHome = true;

    public void checkedChangeLister(int checkedId) {
        checkedChangeListerUpdate(checkedId, resultState);
    }

    /**
     * 为了是否有登录信息做的升级版
     *
     * @param state -1 注销之后
     *              0  正常情况
     *              1  无账号情况下进行登录
     * @author yjbo  @time 2017/3/9 19:43
     */
    public void checkedChangeListerUpdate(int checkedId, int state) {
        this.selectCheckedId = checkedId;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hind(transaction);
        hideBj(checkedId);
        switch (checkedId) {
            //0主页
            case 0:
                if (homePageFragment != null && homePageFragment.isAdded()) {
                    transaction.show(homePageFragment);
                    //不能重复请求，因为他不能总是请求，数据不能总初始化；此处是为了进行用户是否注销的验证
                    if (!isClickHome) {
                        homePageFragment.againRequestData(state);
                        isClickHome = true;
                    }
                } else {
                    homePageFragment = new HomeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("state", "0");
                    homePageFragment.setArguments(bundle);
                    transaction.add(R.id.module, homePageFragment);
                }
                break;
            //1页面
            case 1:
                if (newModelFragment != null && newModelFragment.isAdded()) {
                    transaction.show(newModelFragment);
                    newModelFragment.againRequestData(state);
                } else {
                    newModelFragment = new OtherFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("content", "1");
                    newModelFragment.setArguments(bundle);
                    transaction.add(R.id.module, newModelFragment);
                }
                break;
            //2页面
            case 2:
                if (navigationFragment != null && navigationFragment.isAdded()) {
                    transaction.show(navigationFragment);
                    navigationFragment.againRequestData(state);
                } else {
                    navigationFragment = new OtherFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("content", "2");
                    navigationFragment.setArguments(bundle);
                    transaction.add(R.id.module, navigationFragment);
                }
                break;
            //3页面
            case 3:
                if (applyFragment != null && applyFragment.isAdded()) {
                    transaction.show(applyFragment);
                    applyFragment.againRequestData(state);
                } else {
                    applyFragment = new OtherFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("content", "3");
                    applyFragment.setArguments(bundle);
                    transaction.add(R.id.module, applyFragment);
                }
                break;


            //4页面
            case 4:
                if (myFragment != null && myFragment.isAdded()) {
                    transaction.show(myFragment);
                    myFragment.againRequestData(state);
                } else {
                    myFragment = new MyFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("content", "4");
                    myFragment.setArguments(bundle);
                    transaction.add(R.id.module, myFragment);
                }
                break;
        }
        transaction.commit();

    }

    //恢复底部所有的布局
    private void hideBj(int checkedId) {
        homeImage.setBackgroundResource(R.drawable.home_defult);
        homeTxt.setTextColor(getResources().getColor(R.color.text_color_1));
        bagImage.setBackgroundResource(R.drawable.app_2_0);
        bagTxt.setTextColor(getResources().getColor(R.color.text_color_1));
        huiImage.setBackgroundResource(R.drawable.app_1_0);
        biaoganImage.setBackgroundResource(R.drawable.app_3_0);
        biaoganTxt.setTextColor(getResources().getColor(R.color.text_color_1));
        myImage.setBackgroundResource(R.drawable.icon_mine_defult);
        myTxt.setTextColor(getResources().getColor(R.color.text_color_1));

        switch (checkedId) {
            case 0:
                homeImage.setBackgroundResource(R.drawable.home_pressed);
                homeTxt.setTextColor(getResources().getColor(R.color.bg_blue));
                break;
            case 1:
                bagImage.setBackgroundResource(R.drawable.app_2_1);
                bagTxt.setTextColor(getResources().getColor(R.color.bg_blue));
                break;
            case 2:
                huiImage.setBackgroundResource(R.drawable.app_1_1);
                break;
            case 3:
                biaoganImage.setBackgroundResource(R.drawable.app_3_1);
                biaoganTxt.setTextColor(getResources().getColor(R.color.bg_blue));
                break;
            case 4:
                myImage.setBackgroundResource(R.drawable.icon_mine_pressed);
                myTxt.setTextColor(getResources().getColor(R.color.bg_blue));
                break;
        }
    }


    public void hind(FragmentTransaction transaction) {
        if (homePageFragment != null) {
            transaction.hide(homePageFragment);
        }
        if (newModelFragment != null) {
            transaction.hide(newModelFragment);
        }
        if (navigationFragment != null) {
            transaction.hide(navigationFragment);
        }
        if (applyFragment != null) {
            transaction.hide(applyFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        mHandler.removeCallbacksAndMessages(null);
        if (connectionReceiver != null) {
            unregisterReceiver(connectionReceiver);
        }
        // 展示广告条窗口的 onDestroy() 回调方法中调用
        BannerManager.getInstance(MainActivity.this).onDestroy();
    }

    @OnClick({R.id.home_layout, R.id.bag_layout, R.id.biaogan_layout, R.id.my_layout, R.id.hui_layout})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.home_layout:
                checkedChangeLister(0);
                break;
            case R.id.bag_layout:
                checkedChangeLister(1);
                break;
            case R.id.hui_layout:
                checkedChangeLister(2);
                break;
            case R.id.biaogan_layout:
                checkedChangeLister(3);
                break;
            case R.id.my_layout:
                checkedChangeLister(4);
                break;
        }

    }

    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }

    /**
     * 检查更新
     */
    private void checkForUpdate() {
    }

    WeakHandler mHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });

    /**
     * 显示消息来了的时候的小红点
     *
     * @author yjbo
     * @time 2016/12/11 21:25
     */
    public void showRedPoint(int checkedId, int dataNum) {
        switch (checkedId) {
            //新主页
            case 0:
                if (homePageFragment != null && homePageFragment.isAdded()) {
                    if (dataNum > 0) {
                        homePageFragment.showRedPoint(View.VISIBLE);
                    } else {
                        homePageFragment.showRedPoint(View.GONE);
                    }
                }
                break;
            //书包页面
            case 1:
                if (newModelFragment != null && newModelFragment.isAdded()) {
                    if (dataNum > 0) {
                        newModelFragment.showRedPoint(View.VISIBLE);
                    } else {
                        newModelFragment.showRedPoint(View.GONE);
                    }
                }
                break;
            //汇页面
            case 2:
                if (navigationFragment != null && navigationFragment.isAdded()) {
                    if (dataNum > 0) {
                        navigationFragment.showRedPoint(View.VISIBLE);
                    } else {
                        navigationFragment.showRedPoint(View.GONE);
                    }
                }
                break;
            //标杆页面
            case 3:
                if (applyFragment != null && applyFragment.isAdded()) {
//                    transaction.show(applyFragment);
                }
                break;
            //我的页面
            case 4:
                if (myFragment != null && myFragment.isAdded()) {
                    if (dataNum > 0) {
                        myFragment.showRedPoint(View.VISIBLE);
                    } else {
                        myFragment.showRedPoint(View.GONE);
                    }
                }
                break;
        }
    }

    /**
     * @author lixiaofeng
     * @description 接收广播刷新列表
     * @date 2014-10-11下午3:37:55
     */
    class RefreshListReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context mContext, Intent intent) {
            String action = intent.getAction();
        }

    }

    @Override
    public void onBackPressed() {
        if (mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {//最终处理返回事件的地方
//                bagApplication.exitApp();//退出app
                SpotManager.getInstance(MainActivity.this).onAppExit();
                System.exit(0);
                finish();
                super.onBackPressed();
            } else {//将返回消息不处理，传给子页面
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

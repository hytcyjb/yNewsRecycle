package yjbo.yy.ynewsrecycle.main;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.youmi.android.normal.banner.BannerManager;
import net.youmi.android.normal.banner.BannerViewListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import butterknife.Bind;
import butterknife.ButterKnife;
import yjbo.yy.ynewsrecycle.R;
import yjbo.yy.ynewsrecycle.mainutil.WeakHandler;

/**
 * 我的界面
 *
 * @author yjbo
 * @time 2017/4/4 16:50
 */
public class MyFragment extends BackHandledFragment {


    @Bind(R.id.hint_tv)
    TextView hintTv;
    private View view = null;
    private Context mContext = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_my, container, false);
            ButterKnife.bind(this, view);
        }

        ButterKnife.bind(this, view);
        Bundle arguments = getArguments();
        String content = arguments.getString("content");
//        hintTv.setText(content);
        initData();

        return view;
    }

    private void initData() {
        //        initgg();
    }


    /**
     * 添加了有米的广告---4 广告条调用
     *
     * @author yjbo  @time 2017/4/4 14:49
     */
    private void initgg() {
        // 获取广告条
        View bannerView = BannerManager.getInstance(mContext)
                .getBannerView(mContext, new BannerViewListener() {
                    @Override
                    public void onRequestSuccess() {
                    }

                    @Override
                    public void onSwitchBanner() {
                    }

                    @Override
                    public void onRequestFailed() {
                    }
                });
        // 获取要嵌入广告条的布局
        LinearLayout bannerLayout = (LinearLayout) view.findViewById(R.id.ll_banner);
        // 将广告条加入到布局中
        bannerLayout.addView(bannerView);
    }


    private boolean hadIntercept;

    @Override
    protected boolean onBackPressed() {
        if (hadIntercept) {
            return false;
        } else {
            Toast.makeText(getActivity(), "再按一次退出", Toast.LENGTH_SHORT).show();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hadIntercept = false;
                }
            }, 2000);
            hadIntercept = true;
            return true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        mHandler.removeCallbacksAndMessages(null);
    }

    WeakHandler mHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            int what = message.what;
            switch (what) {
                case 0:

                    break;
            }
            return false;
        }
    });

    public void againRequestData(int state) {
    }

    public void showRedPoint(int visible) {
    }
}

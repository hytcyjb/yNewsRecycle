package yjbo.yy.ynewsrecycle.main;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.youmi.android.normal.banner.BannerManager;
import net.youmi.android.normal.banner.BannerViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import yjbo.yy.ynewsrecycle.R;
import yjbo.yy.ynewsrecycle.entity.newsClass;
import yjbo.yy.ynewsrecycle.home.CentreRecyclerAdapter;
import yjbo.yy.ynewsrecycle.home.HomeRecyclerAdapter;
import yjbo.yy.ynewsrecycle.home.WrapRecyclerAdapter;
import yjbo.yy.ynewsrecycle.mainutil.CommonUtil;
import yjbo.yy.ynewsrecycle.mainutil.WeakHandler;

/**
 * 中心页面
 * 顶部图片引用：http://news.163.com/photoview/3R710001/2223694.html?from=ph_kk#p=C995A7FU3R710001
 *
 * @author yjbo
 * @time 2017/4/5 9:59
 * @mail 1457521527@qq.com
 */
public class CentreFragment extends BackHandledFragment {


    @Bind(R.id.swipe_target_onekind)
    RecyclerView mRecyclerView;
    private View view = null;
    private Context mContext = null;
    private CentreRecyclerAdapter mAdapterDemo;
    private WrapRecyclerAdapter mWrapRecyclerAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_centre, container, false);
            ButterKnife.bind(this, view);
        }

        ButterKnife.bind(this, view);
        Bundle arguments = getArguments();
        String content = arguments.getString("content");
        initView();
        initData();
        return view;
    }

    protected void initView() {
        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
//        //添加布局管理器--列表
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        //添加布局管理器--网格
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));


    }

    private void initData() {
        new Thread(runnable).start();
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


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Document doc = Jsoup.connect("http://blog.csdn.net/yangjianbo456/article/details/68943488").get();
                Elements paragraphs = doc.select("p");
                for (Element p : paragraphs) {
                    String ptext = p.text();
                    if (ptext.contains("yjbointertitle")) {
                        String replaceStr = ptext.replace("yjbointertitle", "");
                        System.out.println(replaceStr);
                        Message msg = new Message();
                        msg.obj = replaceStr;
                        msg.what = 0;
                        mHandler.sendMessage(msg);
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            try {
                Document doc = Jsoup.connect("http://blog.csdn.net/yangjianbo456/article/details/68943488").get();
                Elements paragraphs = doc.select("p");
                for (Element p : paragraphs) {
                    String ptext = p.text();
                    if (ptext.contains("yjbointertopban")) {
                        String replaceStr = ptext.replace("yjbointertopban", "");
                        Message msg = new Message();
                        msg.obj = replaceStr;
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };


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
            String obj = (String) message.obj;
            JSONObject jsonObject = null;
            List<newsClass> datas = new ArrayList<>();
            switch (what) {
                case 0:
                    datas = new ArrayList<>();
                    try {
                        //请求处理的格式会出错，建议使用http://www.json.cn/网址校验；
                        String obj2 = CommonUtil.repalceCsdnStr(obj);
                        jsonObject = new JSONObject(obj2);
                        if ("success".equals(jsonObject.optString("result"))) {
                            JSONArray jsonArray = jsonObject.optJSONArray("list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.optJSONObject(i);
                                newsClass item = new newsClass(jsonObj.optString("title"), jsonObj.optString("abs_title"),
                                        jsonObj.optString("image"), jsonObj.optString("type"));
                                datas.add(item);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (datas.size() > 0) {
                        mAdapterDemo = new CentreRecyclerAdapter(mContext, datas);
                        mRecyclerView.setAdapter(mAdapterDemo);
//                        mWrapRecyclerAdapter = new WrapRecyclerAdapter(mAdapterDemo);
//                        mRecyclerView.setAdapter(mWrapRecyclerAdapter);
//
//                        final View headerView = LayoutInflater.from(mContext).inflate(R.layout.layout_header, mRecyclerView, false);
//                        final View footView = LayoutInflater.from(mContext).inflate(R.layout.layout_footer, mRecyclerView, false);
//                        mWrapRecyclerAdapter.addHeaderView(headerView);
//                        mWrapRecyclerAdapter.addFooterView(footView);
                    } else {
                    }

                    new Thread(runnable2).start();

                    break;
                case 1:
                    datas = new ArrayList<>();
                    try {
                        //请求处理的格式会出错，建议使用http://www.json.cn/网址校验；
                        String obj2 = CommonUtil.repalceCsdnStr(obj);
                        jsonObject = new JSONObject(obj2);
                        if ("success".equals(jsonObject.optString("result"))) {
                            JSONArray jsonArray = jsonObject.optJSONArray("list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.optJSONObject(i);
                                newsClass item = new newsClass(jsonObj.optString("title"), jsonObj.optString("abs_title"),
                                        jsonObj.optString("image"), jsonObj.optString("type"));
                                datas.add(item);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (datas.size() > 0) {
                        if (mAdapterDemo == null){
                            mAdapterDemo = new CentreRecyclerAdapter(mContext, datas);
                        }
                        mWrapRecyclerAdapter = new WrapRecyclerAdapter(mContext,mAdapterDemo);
                        mRecyclerView.setAdapter(mWrapRecyclerAdapter);


                        final View headerView = LayoutInflater.from(mContext).inflate(R.layout.layout_header, mRecyclerView, false);
//                        final View footView = LayoutInflater.from(mContext).inflate(R.layout.layout_footer, mRecyclerView, false);
                        mWrapRecyclerAdapter.addHeaderView(headerView);
//                        mWrapRecyclerAdapter.addFooterView(footView);

                        mWrapRecyclerAdapter.updateHeaderView(headerView,datas.get(1).getImage(),datas.get(1).getTitle());

                    } else {
                    }
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

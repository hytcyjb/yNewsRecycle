package yjbo.yy.ynewsrecycle.main;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import yjbo.yy.ynewsrecycle.home.OtherRecyclerAdapter;
import yjbo.yy.ynewsrecycle.home.WrapRecyclerAdapter;
import yjbo.yy.ynewsrecycle.mainutil.CommonUtil;
import yjbo.yy.ynewsrecycle.mainutil.DividerGridItemDecorationCopy;
import yjbo.yy.ynewsrecycle.mainutil.WeakHandler;
/** 
 * 除了首页之外的页面
 * @author yjbo
 * @time 2017/4/3 16:22
 */
public class OtherFragment extends BackHandledFragment {

    @Bind(R.id.swipe_target_onekind)
    RecyclerView mRecyclerView;
    @Bind(R.id.hint_tv)
    TextView hintTv;
    private View view = null;
    private Context mContext =null;
    private int showCount = 3;//显示的数量
    private OtherRecyclerAdapter mAdapterDemo;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_other, container, false);
            ButterKnife.bind(this, view);
        }

        ButterKnife.bind(this, view);
        Bundle arguments = getArguments();
        String content = arguments.getString("content");
        hintTv.setText(content);
        initView();
        initData();
        initgg();
        return view;
    }



    /**
     * 添加了有米的广告---4 广告条调用
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



    protected void initView() {
        //添加分割线
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        //这个自定义的不能写上去，会报错，所以就不要写
//        mRecyclerView.addItemDecoration(new DividerGridItemDecorationCopy(mContext,0,0));
//        //添加布局管理器--列表
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        //添加布局管理器--网格
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, showCount));


    }

    private void initData() {
        new Thread(runnable).start();
    }




    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
//                Document doc = Jsoup.connect("http://blog.csdn.net/yangjianbo456/article/details/68943488").get();
//                Elements paragraphs = doc.select("p");
//                for (Element p : paragraphs) {
//                    String ptext = p.text();
//                    if (ptext.contains("yjbointertitle")) {
//                        String replaceStr = ptext.replace("yjbointertitle", "");
//                        System.out.println(replaceStr);
                        Message msg = new Message();
                        msg.obj = "";
                        msg.what = 0;
                        mHandler.sendMessage(msg);
//                    }
//                }
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
//            String obj = (String) message.obj;
//            JSONObject jsonObject = null;
            List<newsClass> datas = new ArrayList<>();
            switch (what) {
                case 0:
                    datas = new ArrayList<>();
                    try {
                        //请求处理的格式会出错，建议使用http://www.json.cn/网址校验；
//                        String obj2 = CommonUtil.repalceCsdnStr(obj);
//                        jsonObject = new JSONObject(obj2);
//                        if ("success".equals(jsonObject.optString("result"))) {
//                            JSONArray jsonArray = jsonObject.optJSONArray("list");
                            for (int i = 0; i < 30; i++) {
//                                JSONObject jsonObj = jsonArray.optJSONObject(i);
                                newsClass item = new newsClass(""+i,"abs_title"+i,
                                        "image"+i, ""+i);
                                datas.add(item);
                            }
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (datas.size() > 0) {
                        mAdapterDemo = new OtherRecyclerAdapter(mContext, datas);
                        mRecyclerView.addItemDecoration(new DividerGridItemDecorationCopy(mContext,0,0));
                        mRecyclerView.setAdapter(mAdapterDemo);
                    } else {
                    }

                    break;
                case 1:

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

package yjbo.yy.ynewsrecycle.main;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
import butterknife.OnClick;
import yjbo.yy.ynewsrecycle.R;
import yjbo.yy.ynewsrecycle.entity.newsClass;
import yjbo.yy.ynewsrecycle.home.NewHomepagerAdapter;
import yjbo.yy.ynewsrecycle.mainutil.CommonUtil;
import yjbo.yy.ynewsrecycle.mainutil.LogUtils;
import yjbo.yy.ynewsrecycle.mainutil.PSFirst;
import yjbo.yy.ynewsrecycle.mainutil.WeakHandler;
import yjbo.yy.ynewsrecycle.splash.SplashActivity;

/**
 * 首页的fragment
 *
 * @author yjbo
 * @time 2017/4/3 16:22
 */
public class HomeFragment extends BackHandledFragment {

    @Bind(R.id.home_tabLayout)
    PSFirst home_tabLayout;
    @Bind(R.id.home_viewpager)
    ViewPager viewPager;
    @Bind(R.id.refresh_btn)
    Button refreshBtn;
    private View view = null;
    private Context mContext;
    private List<Fragment> mNewsFragmentList = new ArrayList<>();
    private List<String> nodeIdList = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home, container, false);
            ButterKnife.bind(this, view);
        }

        ButterKnife.bind(this, view);
        Bundle arguments = getArguments();
        String state = arguments.getString("state");
        initView();
        initData();
        return view;
    }

    protected void initView() {
    }

    private void initData() {
        new Thread(runnable).start();
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Document doc = Jsoup.connect("http://blog.csdn.net/yangjianbo456/article/details/68943488").get();
                Elements paragraphs = doc.select("p");
                for (Element p : paragraphs) {
                    String ptext = p.text();
                    if (ptext.contains("yjbointerNStitle")) {
                        String replaceStr = ptext.replace("yjbointerNStitle", "");
                        LogUtils.d("返回值：" + replaceStr);
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

    @OnClick({R.id.add, R.id.add_rl,R.id.refresh_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
            case R.id.add_rl:
//                CommonUtil.toast(mContext, "添加模块还没添加");
                CommonUtil.toast(mContext, "还未完善，先看广告吧！！！", Gravity.CENTER);
                Intent intent = new Intent();
                intent.setClass(mContext, SplashActivity.class);
                intent.putExtra("otherFlag", "HomeFragment");
                startActivityForResult(intent, 101);
                break;
            case R.id.refresh_btn://获取不到数据，点击再次获取
                initData();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int currentItem = viewPager.getCurrentItem();
        System.out.println(requestCode + "====" + resultCode + "===" + currentItem);
//        viewPager.setCurrentItem(10, true);
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
            String obj = (String) message.obj;
            JSONObject jsonObject = null;
            List<newsClass> datas = new ArrayList<>();
            switch (what) {
                case 0:
                    try {
                        //请求处理的格式会出错，建议使用http://www.json.cn/网址校验；
                        String obj2 = obj.replaceAll("“", "\"")
                                .replaceAll("”", "\"")
                                .replaceAll(" ", "");
//                        System.out.println("-obj--" + obj);
//                        System.out.println("-obj2--" + obj2);
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
//                        mAdapterDemo = new RecyclerAdapterDemo(mContext, datas);
//                        mRecyclerView.setAdapter(mAdapterDemo);
                        dealnative(datas);
                        refreshBtn.setVisibility(View.INVISIBLE);
                    } else {
                        refreshBtn.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            return false;
        }
    });

    private void dealnative(List<newsClass> datas) {
        if (datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                newsClass newsClass = datas.get(i);
                String nodeId = newsClass.getTitle();
                String type = newsClass.getType();
                HomeItemFragment newsListFragment = createListFragments(nodeId, i + "", type);
                mNewsFragmentList.add(newsListFragment);
                nodeIdList.add(nodeId);
            }
            viewPager.setAdapter(new NewHomepagerAdapter(getActivity().getSupportFragmentManager(),
                    nodeIdList, mNewsFragmentList));
            //设置tabayout和viewpager相关联
            home_tabLayout.setViewPager(viewPager);
            /**
             * 默认初始化时先在第一个，延时2s之后跳转到想要的那个位置，不延时则报错；
             * @author yjbo  @time 2017/4/5 15:52
             */
//            CommonUtil.toast(mContext, "2s后跳转到“申猴”···", Gravity.CENTER);
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    viewPager.setCurrentItem(8, true);
//                }
//            }, 2 * 1000);
//            viewPager.setCurrentItem(0, true);
        } else {
        }
    }

    public HomeItemFragment createListFragments(String mNodeId, String mPosition, String type) {
        HomeItemFragment homeItemFrag = new HomeItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString("node_id", mNodeId);
        bundle.putString("type", type);
        bundle.putString("position", mPosition);
        homeItemFrag.setArguments(bundle);
        return homeItemFrag;
    }

    public void againRequestData(int state) {
    }

    public void showRedPoint(int visible) {
    }

}

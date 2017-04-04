package yjbo.yy.ynewsrecycle.main;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
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
import yjbo.yy.ynewsrecycle.R;
import yjbo.yy.ynewsrecycle.entity.newsClass;
import yjbo.yy.ynewsrecycle.home.HomeRecyclerAdapter;
import yjbo.yy.ynewsrecycle.mainutil.CommonUtil;
import yjbo.yy.ynewsrecycle.mainutil.WeakHandler;

/**
 * 首页的fragment的子布局
 *
 * @author yjbo
 * @time 2017/4/3 23:54
 */
public class HomeItemFragment extends BackHandledFragment {


    @Bind(R.id.swipe_target_onekind)
    RecyclerView mRecyclerView;
    private View view = null;
    private Context mContext;
    private HomeRecyclerAdapter mAdapterDemo;
    private String pageNo = "----0";
    private String type = "01";//用于请求数据的
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home_item, container, false);
            ButterKnife.bind(this, view);
        }

        ButterKnife.bind(this, view);
        Bundle arguments = getArguments();
        pageNo = "----"+arguments.getString("node_id");
        type = arguments.getString("type");
//        CommonUtil.toast(mContext, "数据请求成功==="+pageNo, Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
        initView();
        initData();
        return view;
    }

    protected void initView() {
        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        //添加布局管理器--列表
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
//        //添加布局管理器--网格
//        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
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
                    String typeName = "yjbointerpage"+type;
                    if (ptext.contains(typeName)) {
                        String replaceStr = ptext.replace(typeName, "");
//                        System.out.println("返回值：" + replaceStr);
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

    /**
     * js本地代码相互调用
     *
     * @author yjbo
     */
    class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            Log.d("HTML=====", html + "---");
            Message msg = new Message();
            msg.what = 2;
            msg.obj = html;
            mHandler.sendMessage(msg);
        }
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
//                        System.out.println("-obj--"+obj);
//                        System.out.println("-obj2--"+obj2);
                        jsonObject = new JSONObject(obj2);
                        if ("success".equals(jsonObject.optString("result"))) {
                            JSONArray jsonArray = jsonObject.optJSONArray("list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.optJSONObject(i);
                                newsClass item = new newsClass(jsonObj.optString("title")+pageNo, jsonObj.optString("abs_title"), jsonObj.optString("image"));
                                datas.add(item);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (datas.size() > 0) {
                        mAdapterDemo = new HomeRecyclerAdapter(mContext, datas);
                        mRecyclerView.setAdapter(mAdapterDemo);
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

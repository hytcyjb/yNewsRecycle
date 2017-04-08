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

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.okhttp.ResponseBody;

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
import retrofit2.Call;
import retrofit2.Callback;
import yjbo.yy.ynewsrecycle.R;
import yjbo.yy.ynewsrecycle.entity.NewApiAllClass;
import yjbo.yy.ynewsrecycle.entity.NewApiClass;
import yjbo.yy.ynewsrecycle.entity.newsClass;
import yjbo.yy.ynewsrecycle.home.HomeRecyclerAdapter;
import yjbo.yy.ynewsrecycle.mainutil.CommonUtil;
import yjbo.yy.ynewsrecycle.mainutil.LogUtils;
import yjbo.yy.ynewsrecycle.mainutil.WeakHandler;
import yjbo.yy.ynewsrecycle.mainutil.retrokhttp.HttpService;
import yjbo.yy.ynewsrecycle.mainutil.retrokhttp.RetrofitNetUtil;

/**
 * 首页的fragment的子布局
 *
 * @author yjbo
 * @time 2017/4/3 23:54
 */
public class HomeItemFragment extends BackHandledFragment {


    @Bind(R.id.swipe_target_onekind)
    XRecyclerView mRecyclerView;
    private View view = null;
    private Context mContext;
    private HomeRecyclerAdapter mAdapterDemo;
    private String type = "01";//用于请求数据的
    private int pageNoInt = 1;//记录当前页码
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
        type = arguments.getString("type");
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
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNoInt = 1;
                initGet();
            }

            @Override
            public void onLoadMore() {
                pageNoInt ++;
                initGet();
            }
        });
    }

    private void completeLoad() {
        mRecyclerView.refreshComplete();
        mRecyclerView.loadMoreComplete();

    }

    private void initData() {
        initGet();
    }

    /***
     * 获取服务器数据
     */
    private void initGet() {
        RetrofitNetUtil netUtil = new RetrofitNetUtil();

        netUtil.setOnrequestistener(new RetrofitNetUtil.OnrequestListener() {

            @Override
            public void onService(HttpService service) {
                Call<NewApiAllClass> call = service.getFirstBlog(type, pageNoInt, 10);
                call.enqueue(new Callback<NewApiAllClass>() {
                    @Override
                    public void onResponse(Call<NewApiAllClass> call, retrofit2.Response<NewApiAllClass> response) {
                        if (response.isSuccessful()) {
                            NewApiAllClass netWorkClass = response.body();
                            LogUtils.d("---"+netWorkClass.toString()+"");
//                            showResult.setText("时间：" +System.currentTimeMillis()
//                                    + "\n" + CommonUtil.getTipStr(netGet,nonetGet)+"\n"
//                                    +netWorkClass.toString());
//                            showKindAsk.setText(CommonUtil.getTipStr(netGet,nonetGet));
//                            String objStr = netWorkClass + "";
//                            Message msg = new Message();
//                            msg.obj = objStr;
//                            msg.what = 0;
//                            mHandler.sendMessage(msg);
                            List<NewApiClass> datas = new ArrayList<>();
                            datas.addAll(netWorkClass.getList());
                            if (datas.size() > 0) {
                                if (mAdapterDemo == null) {
                                    mAdapterDemo = new HomeRecyclerAdapter(getActivity(), datas);
                                    mRecyclerView.setAdapter(mAdapterDemo);
                                }else {
                                    mAdapterDemo.addMore(datas);
                                }
                            } else {
                                pageNoInt = pageNoInt == 1 ? 1 : (pageNoInt-1);
                            }
                        } else {
//                            showResult.setText(response.code() + "==onResponse--数据请求失败--");
                            pageNoInt = pageNoInt == 1 ? 1 : (pageNoInt-1);
                        }
                        completeLoad();
                    }


                    @Override
                    public void onFailure(Call<NewApiAllClass> call, Throwable t) {
//                        showResult.setText( "===onFailure--数据请求失败--");
                        pageNoInt = pageNoInt == 1 ? 1 : (pageNoInt-1);
                        completeLoad();
                    }
                });
            }
        });

        netUtil.requestData(mContext, HttpService.baseHttp, "", 0, 0);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Document doc = Jsoup.connect("http://blog.csdn.net/yangjianbo456/article/details/68943488").get();
                Elements paragraphs = doc.select("p");
                for (Element p : paragraphs) {
                    String ptext = p.text();
                    String typeName = "yjbointerpage" + type;
                    if (ptext.contains(typeName)) {
                        String replaceStr = ptext.replace(typeName, "");
                        Message msg = new Message();
                        msg.obj = replaceStr;
                        msg.what = 0;
                        mHandler.sendMessage(msg);
                    }
                }
            } catch (Exception e) {
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
            List<NewApiClass> datas = new ArrayList<>();
            switch (what) {
                case 0:
                    try {
                        //请求处理的格式会出错，建议使用http://www.json.cn/网址校验；
//                        String obj2 = obj.replaceAll("“", "\"")
//                                .replaceAll("”", "\"")
//                                .replaceAll(" ", "");
                        jsonObject = new JSONObject(obj);
                        if (!"".equals(jsonObject.optString("size"))) {
                            JSONArray jsonArray = jsonObject.optJSONArray("list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.optJSONObject(i);
                                NewApiClass item = new NewApiClass(jsonObj.optString("imgurl"),
                                        jsonObj.optBoolean("has_content"), jsonObj.optString("docurl"), jsonObj.optString("id"),
                                        jsonObj.optString("time"), jsonObj.optString("title"), jsonObj.optString("channelname"));
                                datas.add(item);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (datas.size() > 0) {
                        mAdapterDemo = new HomeRecyclerAdapter(getActivity(), datas);
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

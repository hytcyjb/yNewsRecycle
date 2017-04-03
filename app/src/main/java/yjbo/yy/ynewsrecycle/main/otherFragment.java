package yjbo.yy.ynewsrecycle.main;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import butterknife.Bind;
import butterknife.ButterKnife;
import yjbo.yy.ynewsrecycle.R;
import yjbo.yy.ynewsrecycle.mainutil.WeakHandler;
/** 
 * 除了首页之外的页面
 * @author yjbo
 * @time 2017/4/3 16:22
 */
public class otherFragment extends BackHandledFragment {


    @Bind(R.id.hint_tv)
    TextView hintTv;
    private View view = null;


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
        initData();
        return view;
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
                    if (p.text().contains("yjbointer")) {
                        System.out.println(p.text());
                        Message msg = new Message();
                        msg.obj = p.text();
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
        @android.webkit.JavascriptInterface
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

package yjbo.yy.ynewsrecycle.home;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import yjbo.yy.ynewsrecycle.R;
import yjbo.yy.ynewsrecycle.entity.NewApiClass;
import yjbo.yy.ynewsrecycle.entity.newsClass;
import yjbo.yy.ynewsrecycle.mainutil.CommonUtil;
import yjbo.yy.ynewsrecycle.mainutil.baseAdapter.RecyclerViewAdapter;
import yjbo.yy.ynewsrecycle.mainutil.baseAdapter.RecyclerViewHolder;
/** 
 * 首页主adapter
 * @author yjbo
 * @time 2017/4/4 0:03
 */
public class HomeRecyclerAdapter extends RecyclerViewAdapter<NewApiClass> {
    Activity mActivity;
    public HomeRecyclerAdapter(Activity context, List<NewApiClass> datas) {
        super(context, R.layout.homepager_item_pic, datas);
        this.mActivity = context;
    }
    /**
     * 加载更多的时候用到的
     * @author yjbo  @time 2017/4/6 11:50
     */
    public void addMore(List<NewApiClass> datas) {

        RecyclerAddMoreKindViewAdapter(datas);

    }
    @Override
    protected void bindData(RecyclerViewHolder holder, final NewApiClass item, final int position) {

        holder.setText(R.id.content_three, item.getTitle())
                .setText(R.id.paper_TX, item.getChannelname())
                .setOnClickListener(R.id.rl_home_pic, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommonUtil.skipWeb(mActivity,item.getDocurl());
                    }
                });
        //加载网络图片
        holder.setImagePath(R.id.img_three_mini, new RecyclerViewHolder.ImageLoder(item.getImgurl()) {
            @Override
            public void loadImage(ImageView imageView, String path) {
                Glide.with(mContext)
                        .load(path)
                        .placeholder(R.mipmap.ic_launcher_round) // 同样也可以是drawble
                        .error(R.mipmap.ic_launcher)// 当不能加载时载入
                        .into(imageView);
            }
        });
    }
}
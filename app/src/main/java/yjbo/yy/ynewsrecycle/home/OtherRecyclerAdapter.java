package yjbo.yy.ynewsrecycle.home;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import yjbo.yy.ynewsrecycle.R;
import yjbo.yy.ynewsrecycle.entity.newsClass;
import yjbo.yy.ynewsrecycle.mainutil.baseAdapter.RecyclerViewAdapter;
import yjbo.yy.ynewsrecycle.mainutil.baseAdapter.RecyclerViewHolder;

/**
 * 第2页adapter
 * @author yjbo
 * @time 2017/4/4 0:03
 */
public class OtherRecyclerAdapter extends RecyclerViewAdapter<newsClass> {

    public OtherRecyclerAdapter(Context context, List<newsClass> datas) {
        super(context, R.layout.homepager_item_other1, datas);
    }

    @Override
    protected void bindData(RecyclerViewHolder holder, final newsClass item, final int position) {

        holder.setText(R.id.content_three, item.getTitle())
                .setText(R.id.paper_TX, item.getAbs_title())
                .setOnClickListener(R.id.rl_home_pic, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, "点击了第" + position + "条信息", Toast.LENGTH_SHORT).show();
                    }
                });
        //加载网络图片
        holder.setImagePath(R.id.img_three_mini, new RecyclerViewHolder.ImageLoder(item.getImage()) {
            @Override
            public void loadImage(ImageView imageView, String path) {
                Glide.with(mContext)
                        .load(path)
                        .placeholder(R.mipmap.ic_launcher_round) // 同样也可以是drawble
                        .error(R.mipmap.ic_launcher_round)// 当不能加载时载入
                        .into(imageView);
            }
        });
    }
}
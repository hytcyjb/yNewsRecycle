package yjbo.yy.ynewsrecycle.home;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import yjbo.yy.ynewsrecycle.R;
import yjbo.yy.ynewsrecycle.mainutil.CommonUtil;


/**
 * 添加头尾布局的思路很好，也是和添加主体的思路一致，也是将布局放到SparseArray中;同时也加入了更新头部的方法：
 * @author yjbo
 * @time 2017/4/2 22:40
 */

public class WrapRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //不包含头部和底部
    private RecyclerView.Adapter mAdapter;

    //由于头部和底部可能有多个，需要用标识来识别
    private int BASE_HEADER_KEY = 5500000;
    private int BASE_Footer_KEY = 6600000;

    //头部和底部集合 必须要用map集合进行标识 key->int  value->object
    SparseArray<View> mHeaderViews;
    SparseArray<View> mFooterViews;

    private Context mContext;

    public WrapRecyclerAdapter(Context context,RecyclerView.Adapter adapter) {
        mContext = context;
        mAdapter = adapter;
        mHeaderViews = new SparseArray<>();
        mFooterViews = new SparseArray<>();
    }

    /**
     * onAttachedToRecyclerView
     *
     *  添加这个方法是为了在网格布局中能够任意在某个位置添加头部布局或者尾部布局占满一横行
     * @author yjbo  @time 2017/4/5 13:55
     */
    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
//                    if (isFooterViewType(getItemViewType(position)) ||
//                            isFooterViewType(getItemViewType(position))){
//                        return gridManager.getSpanCount();
//                    }
                    //这样有点繁琐，这里面有点值比较死，头部文件不能放中间
                    if (mHeaderViews.size() > position) {
                        return gridManager.getSpanCount();
                    } else if (mFooterViews.size() >= recyclerView.getAdapter().getItemCount() - position) {
                        return gridManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && holder.getLayoutPosition() == 0) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //区分头部和底部，根据ViewType来
        //ViewType可能有三部分  头部 底部 Adapter

          //判断是否为头部
        if (isHeaderViewType(viewType)) {
            View headerView = mHeaderViews.get(viewType);
            //header
            return createHeaderOrFooterViewHolder(headerView);
        }

          //判断是否为底部
        if (isFooterViewType(viewType)) {
            View footerView = mFooterViews.get(viewType);
            //footer
            return createHeaderOrFooterViewHolder(footerView);
        }

        //列表
        return mAdapter.onCreateViewHolder(parent, viewType);
    }


    /**
     * 创建头部和底部ViewHolder
     * @param view
     * @return
     */
    private RecyclerView.ViewHolder createHeaderOrFooterViewHolder(View view) {
        return new RecyclerView.ViewHolder(view) {};
    }

    @Override
    public int getItemViewType(int position) {
        //position -> viewtype  头部 底部 adapter  必须要用map集合进行标识
        //if(头部）return 头部 key
        //if(中间位置）return mAdapter.getItemViewType(position);
        //if(底部) return 底部 key

        //header
        if (isHeaderPosition(position)) {
            return mHeaderViews.keyAt(position);
        }

        // footer
        if (isFooterPosition(position)) {
            position = position - mHeaderViews.size() - mAdapter.getItemCount();
            return mFooterViews.keyAt(position);
        }

        //adapter
        position = position - mHeaderViews.size();
        return mAdapter.getItemViewType(position);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //头部，底部不需要绑定数据
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            return;
        }

        // Adapter
        position = position - mHeaderViews.size();
        mAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + mHeaderViews.size() + mFooterViews.size();
    }

    //添加头部，底部
    public void addHeaderView(View view) {
        //没有包含头部
        int position = mHeaderViews.indexOfValue(view);
        if (position < 0) {
            //集合没有就添加，不能重复添加
            mHeaderViews.put(BASE_HEADER_KEY++, view);
        }
        notifyDataSetChanged();
    }
    //更新头部，底部 2017年4月2日22:47:14 yjbo
    public void updateHeaderView(View view,String imageStr,String title) {
        //没有包含头部
        int position = mHeaderViews.indexOfValue(view);
        System.out.println("点击事件=="+position+"==="+title);
        if (position >= 0) {
            //集合有就更新，没有则不能更新
            ImageView img = (ImageView) view.findViewById(R.id.img_three_mini);
            CommonUtil.downImg(mContext,img,imageStr);
//            CommonUtil.PicassoUtil(mContext,img,imageStr);
            TextView tv1 = (TextView) view.findViewById(R.id.content_three);
            tv1.setText(title);
        }
        notifyDataSetChanged();
    }

    public void addFooterView(View view) {
        //没有包含头部
        int position = mFooterViews.indexOfValue(view);
        if (position < 0) {
            //集合没有就添加，不能重复添加
            mFooterViews.put(BASE_Footer_KEY++, view);
        }
        notifyDataSetChanged();
    }
    public void updateFooterView(View view,String abstitle) {
        //没有包含头部
        int position = mFooterViews.indexOfValue(view);
        if (position >= 0) {
            //集合有就更新，没有则不能更新
            TextView tv1 = (TextView) view.findViewById(R.id.content_three);
            tv1.setText(abstitle);
        }
        notifyDataSetChanged();
    }

    //移除头部,底部
    public void removeHeaderView(View view) {
        //没有包含头部
        int index = mHeaderViews.indexOfValue(view);
        if (index < 0) return;
        //集合没有就添加，不能重复添加
        mHeaderViews.removeAt(index);
        notifyDataSetChanged();
    }

    public void removeFooterView(View view) {
        //没有包含底部
        int index = mFooterViews.indexOfValue(view);
        if (index < 0) return;
        //集合没有就添加，不能重复添加
        mFooterViews.removeAt(index);
        notifyDataSetChanged();
    }

    //是否为底部
    private boolean isFooterViewType(int viewType) {
        int footerPosition = mFooterViews.indexOfKey(viewType);
        return footerPosition >= 0;
    }

    //是否为头部
    private boolean isHeaderViewType(int viewType) {
        int headerPosition = mHeaderViews.indexOfKey(viewType);
        return headerPosition >= 0;
    }

    //是否为底部位置
    private boolean isFooterPosition(int position) {
        return position >= (mHeaderViews.size() + mAdapter.getItemCount());
    }

    //是否为头部位置
    private boolean isHeaderPosition(int position) {
        return position < mHeaderViews.size();
    }
}
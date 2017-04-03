package yjbo.yy.ynewsrecycle.mainutil.baseAdapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 基本的ViewHolder
 * @author yjbo  @time 2017/4/3 22:40
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        this.mViews = new SparseArray<>();
    }

    /**
     * 从ItemView获取View
     *
     * @param id  ItemView里包含的ViewId
     * @param <V> 返回View
     * @return
     */
    public <V extends View> V getView(int id) {
        View view = mViews.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            mViews.put(id, view);
        }
        return (V) view;
    }

    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    public RecyclerViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * 设置ImageView的值
     *
     * @param viewId
     * @param resId
     * @return
     */
    public RecyclerViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    /**
     * 设置ImageView的值
     * 第三方  ImageLoder Glide Picasso
     * 不能直接写死第三方图片加载
     * 使用自己的一套规范  ImageLoder
     *
     * @param viewId
     * @return
     */
    public RecyclerViewHolder setImagePath(int viewId, ImageLoder imageLoder) {
        ImageView view = getView(viewId);
        imageLoder.loadImage(view, imageLoder.getPath());
        return this;
    }

    //图片加载 （对第三方库加载图片等封装）
    public abstract static class ImageLoder {
        private String path;

        public ImageLoder(String path) {
            this.path = path;
        }

        //需要复写该方法加载图片
        public abstract void loadImage(ImageView imageView, String path);

        public String getPath() {
            return path;
        }
    }

    /**
     * 设置是否可见
     *
     * @param viewId
     * @param visible
     * @return
     */
    public RecyclerViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 设置tag
     *
     * @param viewId
     * @param tag
     * @return
     */
    public RecyclerViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public RecyclerViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    /**
     * 设置Checkable
     *
     * @param viewId
     * @param checked
     * @return
     */
    public RecyclerViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = (Checkable) getView(viewId);
        view.setChecked(checked);
        return this;
    }

    //点击事件
    public RecyclerViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    //触摸事件
    public RecyclerViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    //长按事件
    public RecyclerViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }
}

package yjbo.yy.ynewsrecycle.home;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 新首页设置tablayout与viewpager相结合的适配器
 * Created by Coder-djw on 2016/7/13.
 */
public class NewHomepagerAdapter extends FragmentPagerAdapter {

    List<String> NodeIdList;
    private List<Fragment> mNewsFragmentList = new ArrayList<>();

    public NewHomepagerAdapter(FragmentManager fm,  List<String> NodeIdList, List<Fragment> newsFragmentList) {
        super(fm);
        this.NodeIdList = NodeIdList;
        this.mNewsFragmentList = newsFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
//        LogUtils.i("nodeid为=" + NodeIdList.get(position));
//        return HomePageChildFragment.newInstance(NodeIdList.get(position), position + "");
        return mNewsFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mNewsFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return NodeIdList.get(position);
    }

}

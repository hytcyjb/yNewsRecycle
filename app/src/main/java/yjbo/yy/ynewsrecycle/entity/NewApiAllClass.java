package yjbo.yy.ynewsrecycle.entity;

import java.util.List;

/**
 * 新闻接口
 * @author yjbo
 * @time 2017/4/8 1:10
 */
public class NewApiAllClass {
    private String size;
    private List<NewApiClass> list;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<NewApiClass> getList() {
        return list;
    }

    public void setList(List<NewApiClass> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "NewApiAllClass{" +
                "size='" + size + '\'' +
                ", list=" + list +
                '}';
    }
}

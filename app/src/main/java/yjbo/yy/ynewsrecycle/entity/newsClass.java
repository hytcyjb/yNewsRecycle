package yjbo.yy.ynewsrecycle.entity;

/** 
 * 新闻的类
 * @author yjbo
 * @time 2017/4/3 22:23
 */
public class newsClass {

    private String title;
    private String abs_title;
    private String image;

    public newsClass(String title, String abs_title, String image) {
        this.title = title;
        this.abs_title = abs_title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbs_title() {
        return abs_title;
    }

    public void setAbs_title(String abs_title) {
        this.abs_title = abs_title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}

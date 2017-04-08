package yjbo.yy.ynewsrecycle.entity;

//"imgurl":"http://cms-bucket.nosdn.127.net/ff87d5059ae44d56a5982778a78b408920170407110810.jpeg",
//"has_content":true,
//"docurl":"http://war.163.com/17/0407/11/CHDQIU2E000181KT.html",
//"id":30526,
//"time":"2017-04-07 11:09:22",
//"title":"美军改口称朝鲜试射飞毛腿导弹 韩持谨慎态度",
//"channelname":"war"

/** 
 * 新闻的类
 * @author yjbo
 * @time 2017/4/7 23:30
 */
public class NewApiClass {

    private String imgurl;
    private boolean has_content;
    private String docurl;
    private String id;
    private String time;
    private String title;
    private String channelname;

    public NewApiClass(String imgurl, boolean has_content, String docurl, String id, String time, String title, String channelname) {
        this.imgurl = imgurl;
        this.has_content = has_content;
        this.docurl = docurl;
        this.id = id;
        this.time = time;
        this.title = title;
        this.channelname = channelname;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public boolean isHas_content() {
        return has_content;
    }

    public void setHas_content(boolean has_content) {
        this.has_content = has_content;
    }

    public String getDocurl() {
        return docurl;
    }

    public void setDocurl(String docurl) {
        this.docurl = docurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChannelname() {
        return "频道："+channelname;
    }

    public void setChannelname(String channelname) {
        this.channelname = channelname;
    }

    @Override
    public String toString() {
        return "NewApiClass{" +
                "imgurl='" + imgurl + '\'' +
                ", has_content=" + has_content +
                ", docurl='" + docurl + '\'' +
                ", id='" + id + '\'' +
                ", time='" + time + '\'' +
                ", title='" + title + '\'' +
                ", channelname='" + channelname + '\'' +
                '}';
    }
}

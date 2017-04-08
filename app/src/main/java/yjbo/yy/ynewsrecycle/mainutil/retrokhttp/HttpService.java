package yjbo.yy.ynewsrecycle.mainutil.retrokhttp;

import com.squareup.okhttp.ResponseBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import yjbo.yy.ynewsrecycle.entity.NewApiAllClass;
import yjbo.yy.ynewsrecycle.entity.NewApiClass;

/**
 * @description: <描述当前版本功能>
 * <p>
 * 1.直接get就好 http://ip.taobao.com/service/getIpInfo.php?ip=202.202.33.33
 * </p>
 * @author: yjbo
 * @date: 2016-07-12 16:13
 */
public interface HttpService {

    //http://wangyi.butterfly.mopaasapp.com/news/api?type=war&page=1&limit=10
    public final static String baseHttp = "http://wangyi.butterfly.mopaasapp.com/news/";

    //    //这是我之前公司的接口，如果服务器不运行就没用了哦。其实只要是get的访问都可以请求，可以自己找个get请求就可以测试了
//    @GET("app.php?m=souguapp&c=appusers&a=network")
//    //这里的{id} 表示是一个变量
//    Call<NewApiClass> getFirstBlog();
//http://www.cnblogs.com/krislight1105/p/5452202.html
    @GET("api?")
    Call<NewApiAllClass> getFirstBlog(@Query("type") String type2, @Query("page") int page2, @Query("limit") int count2);

}

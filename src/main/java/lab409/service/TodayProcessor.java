package lab409.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TodayProcessor implements PageProcessor {

    private static String[] keys = {"扬州瓜洲音乐节",
            "镇江长江音乐节",
            "北京国际音乐节",
            "上海简单生活节",
            "杭州西湖音乐节",
            "成都热波音乐节",
            "长沙橘洲音乐节",
            "张北草原音乐节",
            "舟山东海音乐节",
            "草莓音乐节",
            "迷笛音乐节",
            "南京森林音乐节"};
    
    
    @Override
    public void process(Page page) {

    }

    @Override
    public Site getSite() {
        return null;
    }

    public static void main(String[] args) {
        System.out.println("开始爬取...");
        //List<String> urls = getUrls();
        String url = "https://www.toutiao.com/search_content/?offset="
                + 0 + "&format=json&keyword=" + "镇江长江音乐节"
                + "&autoload=true&count=20&cur_tab=1";
//        Spider spider = Spider.create(new TodayProcessor());
//        for(String url : urls){
//            spider.addUrl(url);
//        }
//        spider.thread(8).run();
//        System.out.println("爬取结束...");
    }


    /**
     * 生成起始urls
     * @return
     */
    public static List<String> getUrls(){
        List<String> urls = new ArrayList<String>();
        for(String key : keys){
            int index = 0;
            while(true){
                String url = "https://www.toutiao.com/search_content/?offset="
                        + index + "&format=json&keyword=" + key
                        + "&autoload=true&count=20&cur_tab=1";
                //获取目标
                List<String> list = getDetailUrls(url);
                if(list == null){
                    break;
                }else{
                    urls.addAll(list);
                }
                index += 20;
            }
        }
        return urls;
    }

    /**
     * 判断爬去是否结束
     * @param url
     * @return
     */
    public static List<String> getDetailUrls(String url){
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response;
        List<String> urls = new ArrayList<String>();
        try{
            response = httpClient.execute(httpGet);
            String temp;
            HttpEntity entity = response.getEntity();
            temp = EntityUtils.toString(entity,"UTF-8");
            JSONObject jsStr = JSONObject.parseObject(temp);
            if(jsStr.getIntValue("return_count") < 1){
                return null;
            }else{
                JSONArray data = jsStr.getJSONArray("data");
                for(Object item : data){
                    JSONObject jsonObject = JSONObject.parseObject(item.toString());
                    if(jsonObject.getString("display_url") != null){
                        urls.add(jsonObject.getString("display_url"));
                    }
                }
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return urls;
    }
}

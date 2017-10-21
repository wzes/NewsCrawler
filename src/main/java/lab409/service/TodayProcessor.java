package lab409.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lab409.dao.NewsMapper;
import lab409.model.News;
import lab409.model.Url;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TodayProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100)
            .addHeader("User-Agent",
                    "")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
            .addHeader("Accept-Encoding", "gzip, deflate, sdch")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8")
            .addHeader("Connection", "keep-alive");
    private static HashMap<String, String> referMap = new HashMap<String, String>();
    private static HashMap<String, Integer> indexMap = new HashMap<String, Integer>();
    private static String[] keys = KeyService.getKeys();

    /*
     * 无效
     */
    private final static String REGEX = "http://www.toutiao.com";
    private final static String title_xpath = "/html/body/div/div[2]/div[2]/div[1]/h1";
    private final static String source_xpath = "/html/body/div/div[2]/div[2]/div[1]/div[1]/span[1]";
    private final static String publish_date_xpath = "/html/body/div/div[2]/div[2]/div[1]/div[1]/span[2]";
    private final static String content_xpath = "/html/body/div/div[2]/div[2]/div[1]/div[2]";

    private static List<News> newsList = new ArrayList<News>();
    private static List<Url> urlsList = new ArrayList<Url>();

    @Override
    public void process(Page page) {
        if(page.getUrl().toString().startsWith(REGEX)){
            String url;
            String title = null;
            String source = null;
            String public_date = null;
            String content = null;
            String data_source = "今日头条";

            url = page.getUrl().get();
            String refer = referMap.get(url);
            //System.out.println(page.getHtml());
            Document doc = Jsoup.parse(page.getHtml().get());

            Elements article_title = doc.getElementsByClass("article-title");
            if(article_title.size() != 0){
                title = article_title.get(0).text();
            }
            Elements article_sub = doc.getElementsByClass("articleInfo");

            if(article_sub.size() != 0){
                source = article_sub.get(0).text().substring(0, article_sub.get(0).text().indexOf(" "));
                public_date = article_sub.get(0).text().substring(article_sub.get(0).text().indexOf(" ") + 1);
            }

            Elements article_content = doc.getElementsByClass("article-content");
            if(article_content.size() != 0){
                content = article_content.get(0).text();
            }
//            String title  = page.getHtml().xpath(title_xpath).get();
//            String source = page.getHtml().xpath(source_xpath).get();
//            String public_date = page.getHtml().xpath(publish_date_xpath).get();
//            String content = page.getHtml().xpath(content_xpath).get();

            int type = indexMap.get(refer);
            System.out.println("-------------------------------------------------------");
            News news = new News();
            news.setUrl(url);
            news.setTitle(title);
            news.setWriter(source);
            news.setPublish_time(public_date);
            news.setDatasource(refer);
            news.setBody(content);
            NewsService.setNewsRefer(news, type);

            if(news.getTitle() != null){
                newsList.add(news);
            }
            System.out.println(url);
            System.out.println(title);
            System.out.println(source);
            System.out.println(public_date);
            System.out.println(content);
            System.out.println(refer);
            System.out.println(data_source);
            System.out.println(type);
            System.out.println("-------------------------------------------------------");
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

//    public static void main(String[] args) {
//        System.out.println("开始爬取...");
//        for(int index = 0; index < 12; index++){
//            indexMap.put(keys[index], index);
//        }
//        List<String> urls = getUrls();
////        String url = "https://www.toutiao.com/search_content/?offset="
////                + 0 + "&format=json&keyword=" + "镇江长江音乐节"
////                + "&autoload=true&count=20&cur_tab=1";
//        Spider.create(new TodayProcessor())
//                .startUrls(urls)
//                .thread(8).run();
//        System.out.println("爬取结束...");
//
//
//    }

    /**
     *
     * @return
     */
    public static List<Url> getUrlsList(){
        for(int index = 0; index < 12; index++){
            indexMap.put(keys[index], index);
        }
        return getUrls();
    }

    /**
     *
     * @return
     */
    public static List<News> getNewsList(){
        for(int index = 0; index < 12; index++){
            indexMap.put(keys[index], index);
        }
        List<Url> urls = getUrls();
//        String url = "https://www.toutiao.com/search_content/?offset="
//                + 0 + "&format=json&keyword=" + "镇江长江音乐节"
//                + "&autoload=true&count=20&cur_tab=1";
        List<String> us = new ArrayList<String>();
        for (Url url : urls){
            us.add(url.getUrl());
        }
        Spider.create(new TodayProcessor())
                .startUrls(us)
                .thread(30).run();
        return newsList;
    }

    /**
     * 生成起始urls
     * @return
     */
    public static List<Url> getUrls(){
        List<Url> urls = new ArrayList<Url>();
        for(String key : keys){
            int index = 0;
            while(true){
                String url = "https://www.toutiao.com/search_content/?offset="
                        + index + "&format=json&keyword=" + key
                        + "&autoload=true&count=20&cur_tab=1";
                //获取目标
                List<Url> list = getDetailUrls(url, key);
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
    public static List<Url> getDetailUrls(String url, String key){
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response;
        List<Url> urls = new ArrayList<Url>();
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
                    //System.out.println(jsonObject.getString("group_id"));
                    if(jsonObject.getString("group_id") != null){
                        String init_url = "http://www.toutiao.com/a" + jsonObject.getString("group_id") + "/";
                        String writer = jsonObject.getString("source");
                        Url mUrl = new Url();
                        mUrl.setUrl(init_url);
                        mUrl.setWriter(writer);
                        mUrl.setSource("今日头条");
                        mUrl.setRefer(key);
                        urls.add(mUrl);
                        System.out.println(init_url + " " + writer + "　" + key);
                    }
                }
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return urls;
    }

    public static void main(String[] args) {
        getDetailUrls("https://www.toutiao.com/search_content/?offset=0&format=json&keywor" +
                "d=%E5%8D%81%E4%B9%9D%E5%A4%A7&autoload=true&count=20&cur_tab=1", "shijiuda");
    }
}

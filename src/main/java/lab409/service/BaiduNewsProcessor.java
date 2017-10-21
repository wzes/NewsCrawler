package lab409.service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lab409.model.News;
import lab409.model.Url;
import lab409.utils.ProxyUtil;
import lab409.utils.httpHelper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BaiduNewsProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100)
            .addHeader("User-Agent",
                    "")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
            .addHeader("Accept-Encoding", "gzip, deflate, sdch")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8")
            .addHeader("Connection", "keep-alive")
            .setCharset("utf-8");
    private static HashMap<String, String> referMap = new HashMap<String, String>();
    private static HashMap<String, Integer> indexMap = new HashMap<String, Integer>();
    private static String[] keys = KeyService.getKeys();
    private static List<News> newsList = new ArrayList<News>();
    private static List<Url> urlsList = new ArrayList<Url>();

    @Override
    public void process(Page page) {

//        WebClient webClient = new WebClient(BrowserVersion.CHROME);
//        webClient.getOptions().setJavaScriptEnabled(true);
//        webClient.getOptions().setCssEnabled(false);
//        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
//        webClient.getOptions().setTimeout(35000);
//        webClient.getOptions().setThrowExceptionOnScriptError(false);
//        webClient.addRequestHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3192.0 Safari/537.36");
//        webClient.waitForBackgroundJavaScript(20000);
        String url;
        String title = null;
        String source = null;
        String public_date = null;
        String content = null;
        String data_source = "百度新闻";

        url = page.getUrl().get();
        String refer = referMap.get(url);
        System.out.println(page.getHtml());
        try {
            Document doc = Jsoup.parse(page.getHtml().get());
            System.out.println(doc.text());
            Elements titles = doc.getElementsByClass("c-title");
            for (Element element : titles){
                Element href = element.getElementsByTag("a").get(0);
                String raw_url = href.text();
                System.out.println(raw_url);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



//            String title  = page.getHtml().xpath(title_xpath).get();
//            String source = page.getHtml().xpath(source_xpath).get();
//            String public_date = page.getHtml().xpath(publish_date_xpath).get();
//            String content = page.getHtml().xpath(content_xpath).get();

        //int type = indexMap.get(refer);
//        System.out.println("-------------------------------------------------------");
//        News news = new News();
//        news.setUrl(url);
//        news.setTitle(title);
//        news.setWriter(source);
//        news.setPublish_time(public_date);
//        news.setDatasource(refer);
//        news.setBody(content);
//        NewsService.setNewsRefer(news, type);
//
//        if(news.getTitle() != null){
//            newsList.add(news);
//        }
//        System.out.println(url);
//        System.out.println(title);
//        System.out.println(source);
//        System.out.println(public_date);
//        System.out.println(content);
//        System.out.println(refer);
//        System.out.println(data_source);
//        System.out.println(type);
//        System.out.println("-------------------------------------------------------");
    }

    @Override
    public Site getSite() {
        return site;
    }


    private static ProxyConfig getProxy(){
        ProxyConfig proxyConfig = new ProxyConfig(ProxyUtil.getOneRandom().split(":")[0],
                Integer.valueOf(ProxyUtil.getOneRandom().split(":")[1]));
        return proxyConfig;
    }

    public static List<Url> getUrlsList() {

        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setTimeout(35000);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.addRequestHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3192.0 Safari/537.36");
        webClient.waitForBackgroundJavaScript(20000);

        for(int key_index = 0; key_index < 12; key_index++){
            String key = httpHelper.URLEncode(keys[key_index], "utf-8");
            int index = 0;
            while(true){

                String url = "http://news.baidu.com/ns?word="+ key +"&pn="
                        + index + "&cl=2&ct=1&tn=news&rn=20&ie=utf-8&bt=0&et=0";

                String title = null;
                String source = null;
                String public_date = null;
                String content = null;
                String data_source = "百度新闻";
                HtmlPage mypage = null;
                try {
                    //webClient.getOptions().setProxyConfig(getProxy());
                    mypage = webClient.getPage(url);
                    Document doc = Jsoup.parse(mypage.asXml());
                    Elements elements = doc.getElementsByClass("result");
                    for(Element element : elements){
                        Url mUrl = new Url();
                        Element href = element.select("a[href]").get(0);
                        String raw_url = href.attr("abs:href");
                        Element writer = element.select("p").get(0);
                        mUrl.setWriter(writer.text().split(" ")[0]);
                        mUrl.setUrl(raw_url);
                        mUrl.setRefer(keys[key_index]);
                        mUrl.setSource("百度新闻");
                        System.out.println(raw_url + " " + writer.text().split(" ")[0] + " " + keys[key_index]);
                        urlsList.add(mUrl);
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                if(mypage != null){
                    if(!mypage.asText().contains("下一页")){
                        break;
                    }
                }else{
                    break;
                }
                index += 20;

                System.out.println("----------------------------------" + index + "----------------------" + keys[key_index]);
            }
        }
        return urlsList;
    }

//    public static void main(String[] args) {
//        WebClient webClient = new WebClient(BrowserVersion.CHROME);
//        webClient.getOptions().setJavaScriptEnabled(true);
//        webClient.getOptions().setCssEnabled(false);
//        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
//        webClient.getOptions().setTimeout(35000);
//        webClient.getOptions().setThrowExceptionOnScriptError(false);
//        webClient.addRequestHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3192.0 Safari/537.36");
//        webClient.waitForBackgroundJavaScript(20000);
//        String url = "http://news.baidu.com/ns?word=%E5%8D%81%E4%B9%9D%E5%A4%A7&tn=news&from=news&cl=2&rn=20&ct=1&oq=shijiu&f=3&rsp=1";
//
//        String title = null;
//        String source = null;
//        String public_date = null;
//        String content = null;
//        String data_source = "百度新闻";
//        HtmlPage mypage = null;
//        try {
//            mypage = webClient.getPage(url);
//            Document doc = Jsoup.parse(mypage.asXml());
//
//            Elements elements = doc.getElementsByClass("result");
//            for(Element element : elements){
//                Url mUrl = new Url();
//                Element href = element.select("a[href]").get(0);
//                String raw_url = href.attr("abs:href");
//                Element writer = element.select("p").get(0);
//                mUrl.setWriter(writer.text().split(" ")[0]);
//                mUrl.setUrl(raw_url);
//                mUrl.setSource("百度新闻");
//                urlsList.add(mUrl);
//            }
//
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//
//    }
}

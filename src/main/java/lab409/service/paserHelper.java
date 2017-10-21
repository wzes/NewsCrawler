package lab409.service;

import com.alibaba.fastjson.JSONArray;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lab409.model.News;
import lab409.model.Url;
import lab409.utils.ProxyUtil;
import net.minidev.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class paserHelper {
    public static News paser(Url url){
        News news = null;
        if(url.getSource().equals("百度新闻")){
            news = paserBaiduNews(url);
        }else if (url.getSource().equals("今日头条")){
            //news = paserTodayNews(url);
        }
        return news;
    }

    /**
     *
     * @param url
     * @return
     */
    private static News paserTodayNews(Url url){
        String title = null;
        String source = null;
        String public_date = null;
        String content = null;
        String data_source = url.getSource();

        Document doc = null;
        News news = new News();

        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        //webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        //webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setTimeout(35000);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.addRequestHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3192.0 Safari/537.36");
        //webClient.waitForBackgroundJavaScript(20000);


        try {
            HtmlPage page = webClient.getPage(url.getUrl());
            doc = Jsoup.parse(page.asXml());

            //System.out.println(doc.html());

            Elements article_title = doc.getElementsByClass("article-title");
            if(article_title.size() != 0){
                title = article_title.get(0).text();
            }
            Elements article_sub = doc.getElementsByClass("article-sub");

            if(article_sub.size() != 0){
                source = article_sub.get(0).text().substring(0, article_sub.get(0).text().indexOf(" "));
                public_date = article_sub.get(0).text().substring(article_sub.get(0).text().indexOf(" ") + 1);
            }

            Elements article_content = doc.getElementsByClass("article-content");
            if(article_content.size() != 0){
                content = article_content.get(0).text();
            }
            int type = KeyService.getIndexMap().get(url.getRefer());
            news.setUrl(url.getUrl());
            news.setTitle(title);
            news.setWriter(source);
            news.setPublish_time(public_date);
            news.setDatasource(data_source);
            news.setBody(content);
            NewsService.setNewsRefer(news, type);
            System.out.println(news.toString());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return news;
    }

    private static News paserBaiduNews(Url url){
        String title = null;
        String source = null;
        String public_date = null;
        String content = null;
        String data_source = url.getSource();

        Document doc = null;
        News news = new News();

//        WebClient webClient = new WebClient(BrowserVersion.CHROME);
//        webClient.getOptions().setJavaScriptEnabled(true);
//        webClient.getOptions().setCssEnabled(false);
//        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
//        webClient.getOptions().setTimeout(35000);
//        webClient.getOptions().setThrowExceptionOnScriptError(false);
//        webClient.addRequestHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3192.0 Safari/537.36");
//        webClient.waitForBackgroundJavaScript(20000);
        try {
            doc = Jsoup.connect(url.getUrl()).get();
//            webClient.getOptions().setProxyConfig(getProxy());
//            HtmlPage page = webClient.getPage(url.getUrl());
//            doc = Jsoup.parse(page.asXml());
            title = doc.title();
            public_date = getDate(doc.text());
            data_source = url.getSource();
            Elements elements = doc.getElementsByTag("p");
            StringBuffer sb = new StringBuffer();
            for (Element element : elements){
                sb.append(element.text());
            }
            content = sb.toString();
            int type = KeyService.getIndexMap().get(url.getRefer());
            news.setUrl(url.getUrl());
            news.setTitle(title);
            news.setWriter(url.getWriter());
            news.setPublish_time(public_date);
            news.setDatasource(data_source);
            news.setBody(content);
            NewsService.setNewsRefer(news, type);

            System.out.println(news.toString());
        } catch (IOException e) {
            System.out.println("--------------------------------------error");
        }
        return news;
    }

    public static void main(String[] args) {
        Url url = new Url();
        url.setRefer("镇江长江音乐节");
        url.setSource("百度新闻");
        url.setUrl("http://www.js.xinhuanet.com/2016-09/16/c_1119570354.htm");
        News news = paserBaiduNews(url);
        System.out.println(JSONArray.toJSON(news));
    }

    private static ProxyConfig getProxy(){
        ProxyConfig proxyConfig = new ProxyConfig(ProxyUtil.getOneRandom().split(":")[0],
                Integer.valueOf(ProxyUtil.getOneRandom().split(":")[1]));
        return proxyConfig;
    }

    public static String getDate(String text) {
        Pattern pattern = Pattern.compile("([0-9]{4}[/][0-9]{1,2}[/][0-9]{1,2}[ ][0-9]{1,2}[:][0-9]{1,2}[:][0-9]{1,2})|" +
                "([0-9]{4}[年][0-9]{1,2}[月][0-9]{1,2}[日 ][0-9]{1,2}[:][0-9]{1,2}[:][0-9]{1,2})|" +
                "([0-9]{4}[年][0-9]{1,2}[月][0-9]{1,2}[日 ][0-9]{1,2}[:][0-9]{1,2}[:][0-9]{1,2})|" +
                "([0-9]{4}[/][0-9]{1,2}[/][0-9]{1,2}[ ][0-9]{1,2}[:][0-9]{1,2})|" +
                "([0-9]{4}[-][0-9]{1,2}[-][0-9]{1,2}[ ][0-9]{1,2}[:][0-9]{1,2}[:][0-9]{1,2})|" +
                "([0-9]{4}[-][0-9]{1,2}[-][0-9]{1,2}[ ][0-9]{1,2}[:][0-9]{1,2})");
        Matcher matcher = pattern.matcher(text);

        String dateStr = null;
        if(matcher.find()){
            dateStr = matcher.group(0);
        }
        return dateStr;
    }

}

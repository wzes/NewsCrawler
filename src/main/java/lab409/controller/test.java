package lab409.controller;

import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lab409.model.News;
import lab409.model.Url;
import lab409.service.KeyService;
import lab409.service.NewsService;
import lab409.utils.HttpHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class test {
    public static void main(String[] args) {
//        String content = "articleInfo: {" +
//                "      title: '亮相北京国际音乐节李云迪首次办教堂独奏会',\n" +
//                "      content: '&lt;img src=&quot;http://p1.pstatp.com/large/1420/4907963207&quot; img_width=&quot;400&quot; img_height=&quot;266&quot; alt=&quot;亮相北京国际音乐节李云迪首次办教堂独奏会&quot; inline=&quot;0&quot;&gt;&lt;p&gt;京华时报讯（记者杨杨）10月15日，钢琴家李云迪亮相北京国际音乐节王府井教堂音乐会新闻发布会。此次不仅是李云迪阔别4年后重返音乐节舞台，同时也是教堂音乐会创办8年后迎来的首场钢琴独奏会。李云迪表示：“首次在教堂举办钢琴独奏会，气氛很特别，感觉很好。”&lt;/p&gt;&lt;p&gt;据悉，10月16日晚李云迪在王府井教堂的独奏会选曲兼容并包。音乐会上半场由极具浪漫主义情怀的肖邦《夜曲》与舒曼的《C大调幻想曲》组成。音乐会下半场，李云迪将演奏《彩云追月》《在那遥远的地方》，以及由著名作曲家王建中以云南民歌为素材创作的《云南民歌五首》。音乐会以贝多芬《第23钢琴奏鸣曲“热情”》压轴。京华时报记者王俭摄&lt;/p&gt;&lt;p&gt;更多&lt;/p&gt;'.replace(/<br \\/>|\\n|\\r/ig, ''),\n" +
//                "      groupId: '3597729017',\n" +
//                "      itemId: '1029617462',\n" +
//                "      type: 1,\n" +
//                "      subInfo: {\n" +
//                "        isOriginal: false,\n" +
//                "        source: '京华时报',\n" +
//                "        time: '2014-10-16 00:00'\n" +
//                "      },\n" +
//                "      tagInfo: {\n" +
//                "        tags: __ptags,\n" +
//                "        groupId: '3597729017',\n" +
//                "        itemId: '1029617462',\n" +
//                "        repin: 0\n" +
//                "      }\n" +
//                "    },\n" +
//                "    commentInfo: {\n" +
//                "      groupId: '3597729017',\n" +
//                "      itemId: '1029617462' || '',\n" +
//                "      comments_count: 0,\n" +
//                "      ban_comment: 0 + 0\n" +
//                "    },";
//
//        System.out.println(content.indexOf("articleInfo: ") + " " + content.indexOf(",\n" +
//                "    commentInfo:"));
//        String json = content.substring(content.indexOf("") + 13, content.indexOf(",\n" +
//                "    commentInfo:"));
//        System.out.println(json.trim());
//        System.out.println(JSONObject.parse(json.trim()));

        Url url = new Url();
        url.setUrl("http://www.toutiao.com/a6472970678567682318/");
        url.setRefer("上海简单生活节");
        url.setWriter("sss");
        url.setSource("今日头条");
        String title = null;
        String source = null;
        String public_date = null;
        String content = null;
        String data_source = url.getSource();

        Document doc = null;
        News news = new News();

        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setTimeout(100000);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.addRequestHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3192.0 Safari/537.36");
        webClient.waitForBackgroundJavaScript(20000);


        try {
            HtmlPage page = webClient.getPage(url.getUrl());
//            doc = Jsoup.connect(url.getUrl())
//                    .timeout(2000)
//                    .header("User-Agent", HttpHelper.getRandomUserAgent())
//                    .header("Content-Type", "text/html")
//                    .header("Connection", "keep-alive")
//                    .cookie("Cookie","__cfduid=d0b78c9d223500b0ac0b815622d4d8f031508587694; yjs_id=TW96aWxsYS81LjAgKFgxMTsgTGludXggeDg2XzY0OyBydjo1NS4wKSBHZWNrby8yMDEwMDEwMSBGaXJlZm94LzU1LjB8d3d3Ljg5aXAuY258MTUwODU4NzcwNDUyMHw; ctrl_time=1; UM_distinctid=15f3ed5c22c470-0e264f88e68bb78-3f6e4646-1fa400-15f3ed5c22d645; CNZZDATA1254651946=1933271950-1508584518-%7C1508584518")
//                    .get();
            doc = Jsoup.parse(page.asXml());
            System.out.println(doc.html());
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
    }
}

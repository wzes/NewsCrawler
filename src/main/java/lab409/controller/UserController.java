package lab409.controller;

import com.alibaba.fastjson.JSONArray;
import lab409.dao.NewsMapper;
import lab409.dao.UrlMapper;
import lab409.model.News;
import lab409.model.Url;
import lab409.service.BaiduNewsProcessor;
import lab409.service.TodayProcessor;
import lab409.service.UrlService;
import lab409.service.paserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RestController
@RequestMapping({"/home"})
public class UserController {

    @Autowired
    NewsMapper newsMapper;

    @Autowired
    UrlMapper urlMapper;


    /**
     * 收集 ulrs
     * @return
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public String add(){
        Long start = System.currentTimeMillis();

        //　今日头条
        List<Url> todayUrlsList = TodayProcessor.getUrlsList();
        for (Url url : todayUrlsList){
            try{
                urlMapper.insertUrl(url);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        //百度新闻
        List<Url> baiduUrlsList = BaiduNewsProcessor.getUrlsList();
        for (Url url : baiduUrlsList){
            try{
                urlMapper.insertUrl(url);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        Long end = System.currentTimeMillis();
        Long time = (end - start) / 1000 ;
        return String.valueOf(time);
    }

    @RequestMapping(value = "/urls")
    @ResponseBody
    public String getTodayUrls(){
        List<Url> urlsList = urlMapper.getUrls();
        return JSONArray.toJSON(urlsList).toString();
    }

    @RequestMapping(value = "/news")
    @ResponseBody
    public String addNews(){
        Long start = System.currentTimeMillis();
        List<Url> urls = UrlService.getUrls();
        System.out.println("-----------------------------------------------------------------------------" + urls.size());
        int nThreds = 20;
        ExecutorService executor = Executors.newFixedThreadPool(nThreds);
        for (Url url : urls){
            executor.execute(() -> {
                News news = paserHelper.paser(url);
                if(news != null && news.getTitle() != null){
                    try{
                        newsMapper.insertNews(news);
                        System.out.println("------------------------------------------------------" +
                                "-----------------------" + urls.indexOf(url));
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            });
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
//        for (Url url : urls){
//            News news = paserHelper.paser(url);
//            if(news != null && news.getTitle() != null){
//                try{
//                    newsMapper.insertNews(news);
//                    System.out.println("------------------------------------------------------" +
//                            "-----------------------" + urls.indexOf(url));
//                }catch (Exception e){
//                    System.out.println(e.getMessage());
//                }
//            }
//        }
        Long end = System.currentTimeMillis();
        Long time = (end - start) / 1000 ;
        return String.valueOf(time);
    }

}
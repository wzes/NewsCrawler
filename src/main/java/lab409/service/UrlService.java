package lab409.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lab409.model.Url;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UrlService {

    public static List<Url> getUrls(){
        List<Url> urls = new ArrayList<Url>();
        try {
            String json = Jsoup.connect("http://localhost:8080/home/urls").get().text();
            urls = JSONObject.parseArray(json, Url.class);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return urls;
    }
}

package lab409.controller;

import lab409.dao.NewsMapper;
import lab409.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping({"/home"})
public class UserController {

    @Autowired
    NewsMapper newsMapper;

    @RequestMapping(value = "/add")
    @ResponseBody
    public int add(News news){
        int id = newsMapper.insertNews(news);
        return id;
    }
}
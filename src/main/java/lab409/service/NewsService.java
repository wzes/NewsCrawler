package lab409.service;

import lab409.model.News;

public class NewsService {

    public static void setNewsRefer(News news, int index) {
        switch (index) {
            case 0:
                news.setYangzhou_guazhou(1);
                break;
            case 1:
                news.setZhenjiang_changjiang(1);
                break;
            case 2:
                news.setBeijing_guoji(1);
                break;
            case 3:
                news.setShanghai_jiandan(1);
                break;
            case 4:
                news.setHangzhou_xihu(1);
                break;
            case 5:
                news.setChengdu_rebo(1);
                break;
            case 6:
                news.setChangsha_juzhou(1);
                break;
            case 7:
                news.setZhangbei_caoyuan(1);
                break;
            case 8:
                news.setZhoushan_donghai(1);
                break;
            case 9:
                news.setCaomei(1);
                break;
            case 10:
                news.setMidi(1);
                break;
            case 11:
                news.setNanjing_senlin(1);
                break;
        }
    }
}

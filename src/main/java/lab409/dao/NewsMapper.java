package lab409.dao;

import lab409.model.News;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewsMapper {

    /**
     * 插入数据库一条数据
     * @param news
     * @return
     */
    @Insert("INSERT INTO news (title, body, publish_time, writer, datasource, url," +
            " yangzhou_guazhou, zhenjiang_changjiang, beijing_guoji, shanghai_jiandan, " +
            "hangzhou_xihu, chengdu_rebo, changsha_juzhou, zhangbei_caoyuan, zhoushan_donghai, " +
            "caomei, midi, nanjing_senlin) VALUES (#{title}, #{body}, #{publish_time}, #{writer}, #{datasource}, " +
            "#{url}, #{yangzhou_guazhou}, #{zhenjiang_changjiang}, #{beijing_guoji}, " +
            "#{shanghai_jiandan}, #{hangzhou_xihu}, #{chengdu_rebo}, #{changsha_juzhou}, " +
            "#{zhangbei_caoyuan}, #{zhoushan_donghai}, #{caomei}, #{midi}, #{nanjing_senlin});")
    int insertNews(News news);
}

package lab409.dao;

import lab409.model.News;
import lab409.model.Url;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UrlMapper {

    /**
     * @param news
     * @return
     */
    @Insert("INSERT INTO raw_url (url, refer, source, writer) VALUES (#{url},#{refer},#{source},#{writer});")
    int insertUrl(Url url);

    @Select("select url, refer, source, writer from raw_url")
    List<Url> getUrls();

}

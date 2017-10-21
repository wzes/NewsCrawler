package lab409.service;

import java.util.HashMap;

public class KeyService {

    /**
     *
     * @return
     */
    public static String[] getKeys(){
        String[] keys = {
                "扬州瓜洲音乐节",
                "镇江长江音乐节",
                "北京国际音乐节",
                "上海简单生活节",
                "杭州西湖音乐节",
                "成都热波音乐节",
                "长沙橘洲音乐节",
                "张北草原音乐节",
                "舟山东海音乐节",
                "草莓音乐节",
                "迷笛音乐节",
                "南京森林音乐"};
        return keys;
    }

    public static HashMap<String, Integer> getIndexMap(){
        HashMap<String, Integer> indexMap = new HashMap<String, Integer>();
        for(int index = 0; index < 12; index++){
            indexMap.put(getKeys()[index], index);
        }
        return indexMap;
    }

}

package lab409.utils;

import java.util.ArrayList;
import java.util.List;

/** 
 *@author  venbillyu 
 *@date 创建时间：2016年12月14日 上午10:23:13 
 *@describle 
 */
public class ProxyUtil {
	private static List<String> proxyIpList;
	static{
		proxyIpList=new ArrayList<String>();
		proxyIpList.add("112.65.134.46:81");
		proxyIpList.add("112.65.134.46:81");
		proxyIpList.add("120.204.85.29:3128");
		proxyIpList.add("116.224.190.88:8118");
		proxyIpList.add("61.129.129.72:8080");
		proxyIpList.add("180.159.140.185:8118");
		proxyIpList.add("58.246.194.70:8080");
	}
	//随机获取一个请求头
    public static String getOneRandom(){
    	return proxyIpList.get(
    	(int) Math.round(Math.random()*(proxyIpList.size()-1))); 
    	
    }
    public static List<String> getList(){
    	return proxyIpList;
    }
}

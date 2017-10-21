package lab409.utils;

import java.io.UnsupportedEncodingException;

public class httpHelper {
	//url解密
	public static String URLDecode(String param,String encoding){
		try {
			String res=java.net.URLDecoder.decode(param,encoding);
			return res;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	//加密
	public static String URLEncode(String param,String encoding){
		try {
			String res=java.net.URLEncoder.encode(param,encoding);
			return res;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	//截取首位字符串
	public static String regexStr(String sourceStr,String begStr,String endStr){
		String res="";
		String [] spStr = sourceStr.split(begStr);
		if (spStr.length>0) {
			spStr = spStr[1].split(endStr);
			if (spStr.length>0) {
				res = spStr[0];
			}
		}
		return res;
	}

	public static void main(String[] args) {

		String word = URLEncode("扬州瓜洲音乐节", "utf-8");
		System.out.println(word);
		String param = URLDecode("trialYear%2B2001%2B7%2B2001", "utf8");
		String Order = URLDecode("%E6%B3%95%E9%99%A2%E5%B1%82%E7%BA%A7", "utf8");
		System.out.println(param);
		System.out.println(Order);
	}

}

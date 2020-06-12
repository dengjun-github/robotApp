package com.dj.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BetUtil {

//	public static List<BetsTypeBo> getBo(String key , List<BetsType>betsTypes) throws ClientErrorException {
//
//		List<BetsTypeBo> collect = null;
//		try {
//			collect = contents.stream()
//					.map(e -> betsTypes.stream()
//							.map(be -> setBo(key, e))
//							.findAny().get())
//					.collect(Collectors.toList());
//
//		} catch (Exception e) {
//			throw new ClientErrorException("下注格式错误!");
//		}
//		return collect;
//	}
//
//
//
//
////	// 解析
////	private static String getKey4Content(String content){
////		final StringBuilder key = new StringBuilder();
////		ActionRegex.BET.getRegexMap()
////				.forEach((k,regexes) -> {
////					regexes.stream()
////							.map(Pattern::compile)
////							.map(pattern -> pattern.matcher(content))
////							.map(Matcher::matches)
////							.forEach(aBoolean -> {
////								if (aBoolean){
//////									System.out.println("======================key = " +k);
////									key.append(k);
////									return;
////								}
////
////							});
////		});
////		return  key.toString();
////	}
//
//	public static BetsTypeBo setBo() {
//		return BetsTypeBo.builder()
//				.money(getMoney(action,content))
//				.content(content)
//				.key(action.getKey())
//				.build();
//	}
//
//	private static Integer getMoney(GlobalConstant.Action action,String content) {
//		switch (action) {
//
//		}
//	}


	//利用正则匹配.提取字符串中的汉字
	public static String getChinese(String str){
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();

	}

	//利用正则匹配,提取字符串中的数字
	public static int getNumber(String str){
		String regEx = "[\\u4e00-\\u9fa5]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		String trim = m.replaceAll("").trim();
		if (!trim.equals("")) return Integer.parseInt(trim);
		return 0;
	}


	
	/**
	 * 针对连码和定位球求总点的方法
	 * @param str
	 * @return
	 */
	public static Integer getCount(String str) {
		String[] arr = str.split("/");//按照"/"拆分
		if(arr.length == 3) {
			//定位球
			return arr[0].toCharArray().length*arr[1].toCharArray().length;
		}
		return 1;
	}
	
	//判断字符串是不是以数字开头
	public static boolean isStartWithNumber(String str) {
	   Pattern pattern = Pattern.compile("[0-9]*");
	   Matcher isNum = pattern.matcher(str.charAt(0)+"");
	   if (!isNum.matches()) {
	       return false;
	   }
	   return true;
	}
}

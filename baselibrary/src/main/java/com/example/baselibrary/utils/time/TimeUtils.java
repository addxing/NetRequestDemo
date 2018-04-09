package com.example.baselibrary.utils.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 获取各种格式的时间
 * 
 * @author Susie
 */
public class TimeUtils {

	/**
	 * 获取包含时间的日期
	 * 
	 * @return yyyy-MM-dd hh:mm:ss格式 12小时制	HH 为24小时制
	 */
	public static String getDataAndTime() {

		Date today = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		String time = f.format(today);
		return time;

	}

	/**
	 * 获取日期
	 * @return
	 */
	public static String getDate() {
		Date today = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日");
		String time = f.format(today);
		return time;
		
	}
	/**
	 * 
	 * 比较与当前时间的大小
	 * @param date
	 * @return
	 */
	public static boolean getTimecompare(String date){
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
		long news = 0;
		try {
		  news = simple.parse(date).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long now = new Date().getTime();
		if(news < now){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * 转换当前的时间为毫秒值
	 *
	 * @return
	 */
	public static long formatDateToMill(String formatDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_Hms");

		try {
			return format.parse(formatDate).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return 0;

	}


	/**
	 * 获取当前的格式化时间
	 * @return 返回格式化好的时间
	 */
	public static String currentFormatDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmm");
		String currentTime = format.format(calendar.getTime());
		return currentTime;
	}


	/**
	 * 计算间隔时间
	 *
	 * @param first
	 * @param second
	 * @return
	 */
	public static long intervalTime(String first,String second){
		long first_mill = formatDateToMill(first);
		long second_mill = formatDateToMill(second);
		//返回结果
		long result = second_mill - first_mill;
		return result;
	}


}

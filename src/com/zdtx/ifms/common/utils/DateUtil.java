/*
 * @(#)DateUtils.java  Jan 12, 2011
 * Copyright(c) 2011, Wiflg Goth. All rights reserved.
 */
package com.zdtx.ifms.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
/**
 * 帮助类 - 所有的帮助类需要定义通用接口 根据不同的需求 不同的实现
 * */
/**
 * 
 * @author Wiflg Goth
 * @since Jan 12, 2011
 */
public class DateUtil {

	private static SimpleDateFormat sdf;
	private static SimpleDateFormat yFormat;
	private static SimpleDateFormat ymFormat;
	private static SimpleDateFormat noMinusFormat;
	private static SimpleDateFormat noMinusYMFormat;
	private static SimpleDateFormat noMinusLongTime;
	private static SimpleDateFormat longTime;
	private static SimpleDateFormat nolongTime;
	private static SimpleDateFormat sdf2;
	private static SimpleDateFormat sdf3;

	static {
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf2 = new SimpleDateFormat("HH:mm:ss");
		sdf3 = new SimpleDateFormat("HHmmss");
		yFormat = new SimpleDateFormat("yyyy");
		ymFormat = new SimpleDateFormat("yyyy-MM");
		noMinusFormat = new SimpleDateFormat("yyyyMMdd");
		noMinusYMFormat = new SimpleDateFormat("yyyyMM");
		noMinusLongTime = new SimpleDateFormat("yyyyMMddHHmmss");
		longTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		nolongTime = new SimpleDateFormat("yyyyMMddHHmm");
	}

	/**
	 * Convert string yyyyMMdd to yyyy-MM-dd
	 * @param yyyyMMdd
	 * @return	 yyyy-MM-dd
	 */
	public static String convertYyyyMMdd(String yyyyMMdd) {
		if (null == yyyyMMdd) {
			return null;
		}
		return formatDate(parseYyyyMMdd(yyyyMMdd));
	}

	/**
	 * Parse String yyyyMMdd to Date
	 * @param yyyyMMdd
	 * @return	Date
	 */
	public static Date parseYyyyMMdd(String yyyyMMdd) {

		Date date = null;
		try {
			date = noMinusFormat.parse(yyyyMMdd);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * Convert yyyyMM to yyyy-MM
	 * @param yyyymm
	 * @return yyyy-MM
	 */
	public static String converYyyyMM(String yyyymm) {

		if (null == yyyymm) {
			return null;
		}
		return ymFormat.format(parseYyyyMM(yyyymm));
	}

	/**
	 * Parse String yyyyMM to Date
	 * @param yyyymm
	 * @return to Date
	 */
	public static Date parseYyyyMM(String yyyymm) {
		Date date = null;
		try {
			date = noMinusYMFormat.parse(yyyymm);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 20080809182514 --> 2008-08-09 18:25
	 * @param longTimeString
	 * @return yyyy-MM-dd HH:mm
	 */
	public static String convertLongTime(String longTimeString) {

		if (null == longTimeString) {
			return null;
		}
		return longTime.format(parseLongTime(longTimeString));
	}

	/**
	 * 
	 * @param date
	 * @return String yyyy-MM-dd
	 */
	public static String formatDate(Date date) {
		return sdf.format(date);
	}
	public static String formatMonth(Date date) {
		return ymFormat.format(date);
	}
	public static String formatYear(Date date) {
		return yFormat.format(date);
	}

	/**
	 * Format Long time, convert date to String.
	 * 
	 * @param date
	 * @return String yyyy-MM-dd HH:mm:ss
	 */
	public static String formatLongTimeDate(Date date) {
		return longTime.format(date);
	}
	
	/**
	 * Format Long time, convert date to String.
	 * 
	 * @param date
	 * @return String yyyyMMddHHmmss
	 */
	public static String formatLongTimeDateWithoutSymbol(Date date) {
		return noMinusLongTime.format(date);
	}

	/**
	 * String convert yyyy-MM-dd to Date
	 * 
	 * @param String yyyy-MM-dd
	 * @return date
	 */
	public static Date parseMinute(String day) {

		Date date = null;
		try {
			date = longTime.parse(day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date parse(String day) {

		Date date = null;
		try {
			date = sdf.parse(day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static Date parse_m(String day) {
		Date date = null;
		try {
			date = ymFormat.parse(day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * String convert to Date
	 * 
	 * @param day
	 * @return date
	 */
	public static Date parseLongTime(String day) {
		Date date = null;
		try {
			date = noMinusLongTime.parse(day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 2011-01-12 --> 2011-01-13
	 * 
	 * @param day
	 * @return the next day, format: 20110113
	 */
	
	public static String getToDay() {
		Date date = new Date();
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		date = rightNow.getTime();
		return sdf.format(date);
	}
	
	public static String getNextDay(String day) {
		Date date = parse(day);
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.set(Calendar.DATE, rightNow.get(Calendar.DATE) + 1);
		date = rightNow.getTime();
		return sdf.format(date);
	}

	public static String getNextMinute(String day) {
		Date date = parseMinute(day);
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.set(Calendar.MINUTE, rightNow.get(Calendar.MINUTE) + 1);
		date = rightNow.getTime();
		return nolongTime.format(date);
	}

	/**
	 * 
	 * @param day
	 * @return the next day
	 */
	public static Date getNextDay(Date day) {

		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(day);
		rightNow.set(Calendar.DATE, rightNow.get(Calendar.DATE) + 1);
		return rightNow.getTime();
	}

	public static Date getNextMinute(Date day) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(day);
		rightNow.set(Calendar.MINUTE, rightNow.get(Calendar.MINUTE) + 1);
		return rightNow.getTime();
	}

	/**
	 * 
	 * @return
	 */
	public static Date getYesterday() {
		Calendar rightNow = Calendar.getInstance();
		rightNow.set(Calendar.DATE, rightNow.get(Calendar.DATE) - 1);
		return rightNow.getTime();
	}
	/**
	 * yyyyMMdd 00:00:00
	 * @return
	 */
	public static String getYesterdayBegin(){
		Calendar rightNow = Calendar.getInstance();
		rightNow.set(rightNow.get(Calendar.YEAR), rightNow.get(Calendar.MARCH), rightNow.get(Calendar.DATE) - 1, 00, 00, 00);
		return longTime.format(rightNow.getTime());
	}
	
	/**
	 * yyyyMMdd 23:59:59
	 * @return
	 */
	public static String getYesterdayEnd(){
		Calendar rightNow = Calendar.getInstance();
		rightNow.set(rightNow.get(Calendar.YEAR), rightNow.get(Calendar.MARCH), rightNow.get(Calendar.DATE) - 1, 23, 59, 59);
		return longTime.format(rightNow.getTime());
	}

	/**
	 * 
	 * @return
	 */
	public static String getYMDYesterday() {
		return sdf.format(getYesterday());
	}

	/**
	 * 
	 * @return
	 */
	public static String getLongTimeYesterday() {
		return longTime.format(getYesterday());
	}

	/**
	 * 
	 * @return
	 */
	public static Date getLastMonth() {
		Calendar rightNow = Calendar.getInstance();
		rightNow.set(Calendar.MONTH, rightNow.get(Calendar.MONTH) - 1);
		return rightNow.getTime();
	}

	/**
	 * 
	 * @return
	 */
	public static String getYMLastMonth() {
		return ymFormat.format(getLastMonth());
	}
	
	public static int[] getInt(String s){
		String[] sr=s.split("-");
		int[] is =new int[3];
		for(int i=0;i<sr.length;i++){
			is[i]=Integer.parseInt(sr[i]);
		}
		return is;
	}
	
	/**
	 * @param 返回日期对应的周格式
	 * @param date
	 * @return "yyyy-MM"
	 */
	public static String getWeek(Date date){
		Calendar calendar = new GregorianCalendar();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setMinimalDaysInFirstWeek(7);
		calendar.setTime(date);
		String week = calendar.get(Calendar.WEEK_OF_YEAR) < 10 ? "0"+calendar.get(Calendar.WEEK_OF_YEAR) : "" + calendar.get(Calendar.WEEK_OF_YEAR);
		return formatDate(date).substring(0,5)+week;
	}

	/**
	 * 获得下一个月当前天（例：2013-4-15 -》 2013-5-15）
	 * @param dateStr yyyy-MM-dd
	 * @return nextDateStr yyyy-MM-dd
	 */
	public static String getNextMonthDay(String dateStr) {
		return getSeveralMonthDay(dateStr, 1);
	}
	/**
	 * 2012-12-20 --> 2012-12-20
	 * 
	 * @param day
	 * @return the next day, format: 20110113
	 */
	public static String getNextMonth() {

		Date date = new Date();
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.set(Calendar.MONTH, rightNow.get(Calendar.MONTH) - 1);
		date = rightNow.getTime();
		return ymFormat.format(date);
	}
	/**
	 * Parse String yyyyMM to Date
	 * 
	 * @param yyyymm
	 * @return
	 */
	public static Date parseYyyyMM_1(String yyyymm) {
		Date date = null;
		try {
			date = ymFormat.parse(yyyymm);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	public static String getNextMonth(String yyyymm) {

	    Date time =parseYyyyMM_1(yyyymm);
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(time);
		rightNow.set(Calendar.MONTH, rightNow.get(Calendar.MONTH) - 1);
		time = rightNow.getTime();
		return ymFormat.format(time);
	}
	/**
	 * 获得上一个月当前天（例：2013-4-15 -》 2013-3-15）
	 * @param dateStr yyyy-MM-dd
	 * @return nextDateStr yyyy-MM-dd
	 */
	public static String getLastMonthDay(String dateStr) {
		return getSeveralMonthDay(dateStr, -1);
	}
	
	/**
	 * 获得若干月份后当前天
	 * @param dateStr yyyy-MM-dd
	 * @param int 月份差 负数代表过去月份
	 * @return  结果日期	yyyy-MM-dd
	 */
	public static String getSeveralMonthDay(String dateStr, int difference) {
		Calendar calendar = Calendar.getInstance();
		String[] subStr = dateStr.split("-");	//拆分年月日
		calendar.set(Calendar.YEAR, (Integer.valueOf(subStr[0])));	//设定年
		calendar.set(Calendar.MONTH, (Integer.valueOf(subStr[1]) - 1 + difference));	//设定上一个月
		int end = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);	//上个月实际最后一天
		int dayOfMonth = Integer.valueOf(subStr[2]);	//当前日期天数
		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth > end ? end : dayOfMonth);	//针对最后一天特殊处理
		return sdf.format(calendar.getTime());
	}
	
	/**
	 * 根据日期 获得所在周的周一和周日
	 * 
	 * @param date
	 * @return
	 */
	public static String[] dateToWeek(String date) {
		String[] date_week = new String[2];
		Date date_all = DateUtil.parse(date);
		 Calendar c = Calendar.getInstance();
		 c.setTime(date_all);
		  int dayofweek = c.get(Calendar.DAY_OF_WEEK) - 1;
		  if (dayofweek == 0)
		   dayofweek = 7;
		  c.add(Calendar.DATE, -dayofweek + 1);
		  
		  date_week[0]=sdf.format(c.getTime());
		  Calendar c1 = Calendar.getInstance();
		  c1.setTime(date_all);
		  int dayofweek1 = c1.get(Calendar.DAY_OF_WEEK) - 1;
		  if (dayofweek1 == 0)
			  dayofweek1 = 7;
		  c1.add(Calendar.DATE, -dayofweek1 + 7);
		  date_week[1]=sdf.format(c1.getTime());
		  
		return date_week;
	}
	/**
	 * 根据月份获得这一月的最后一天
	 * 
	 * @param date
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getLastDayOfMonth(String date) {
		Date date_begin = DateUtil.parse(date + "-01");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date_begin);
		int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		Date lastDate = calendar.getTime();
		lastDate.setDate(lastDay);
		return DateUtil.formatDate(lastDate);
	}
	/**
	 * 将日期转换成HH:MM:SS的格式
	 * @param date
	 * @return
	 */
	public static String formatHmsDate(Object date) {
		if (null == date || "".equals(date)) {
			return "00:00:00";
		} else {
			String _ = Utils.doubleFmt(date,"###############");
			String hmsDate = "";
			Long sec = Long.valueOf(_); // 秒数
			Long h = sec / 3600L; // 时
			Long m = (sec % 3600L) / 60L; // 分
			Long s = sec % 60L; // 秒
			String hh = h < 10 ? ("0" + h) : String.valueOf(h); // 补位
			String mm = m < 10 ? ("0" + m) : String.valueOf(m);
			String ss = s < 10 ? ("0" + s) : String.valueOf(s);
			hmsDate = hh + ":" + mm + ":" + ss;
			return hmsDate;
		}
	}
	public static String yyyy_MM_ddTOyyyyMMdd(String str){
		String str2="";
		try {
			str2=noMinusFormat.format(sdf.parse(str));
		} catch (ParseException e) {
		}
		return str2;
	}
	
	public static String toyyyyMMdd(String str){
		String str2="";
		try {
			str2 =noMinusFormat.format(sdf.parse(str));
		} catch (ParseException e) {
		}
		return str2;
	}
	public static String toHHmmss(String str){
		String str2="";
		try {
			str2 =sdf3.format(sdf2.parse(str2));
		} catch (ParseException e) {
		};
		return str2;
	}
	public static String toHHmmssPlus(long l,String str){
		String str2="";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2=new SimpleDateFormat("HHmmss");
		Date a;
		try {
			a=sdf.parse(str);
			a.setTime(a.getTime()+l*1000);
			str2=sdf2.format(a);
		} catch (ParseException e) {
		}
		return str2;
		
	}
	public static String toHHmmssPlus3(long l,String str){
		String str2="";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2=new SimpleDateFormat("%27yyyy-MM-dd%20HH:mm:ss%27");
		Date a;
		try {
			a=sdf.parse(str);
			a.setTime(a.getTime()+l*1000);
			str2=sdf2.format(a);
		} catch (ParseException e) {
		}
		return str2;
		
	}
	public static String toHHmmssPlus2(long l,String str){
		String str2="";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2=new SimpleDateFormat("HH:mm:ss");
		Date a;
		try {
			a=sdf.parse(str);
			a.setTime(a.getTime()+l*1000);
			str2=sdf2.format(a);
		} catch (ParseException e) {
		}
		return str2;
	}
	public static String toHHmmssPlusType1(long l,String str){
		String str2="";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2=new SimpleDateFormat("yyyyMMddTHHmmssZ");
		Date a;
		try {
			a=sdf.parse(str);
			a.setTime(a.getTime()+l*1000);
			str2=sdf2.format(a);
		} catch (ParseException e) {
		}
		return str2;
		
	}
	public static String toHHmmssPlusType2(long l,String str){
		String str2="";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2=new SimpleDateFormat("yyyyMMdd_HHmmss.000");
		Date a;
		try {
			a=sdf.parse(str);
			a.setTime(a.getTime()+l*1000);
			str2=sdf2.format(a);
		} catch (ParseException e) {
		}
		return str2;
		
	}
	/**
	 * 获取目标日期起前3整周（周一到周日为一整周）的起止日期
	 * @return String[] : [yyyy-MM-dd1, yyyy-MM-dd2]
	 */
	public static String[] getThreeWeeksBeginAndEnd(String targetDate) {
		String[] res = new String[2];
		int year = Integer.valueOf(targetDate.split("-")[0]);
		int month = Integer.valueOf(targetDate.split("-")[1]);
		int day = Integer.valueOf(targetDate.split("-")[2]);
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day);
		int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;	//目标日期在所在周中的天数
		 if (day_of_week == 0) {	//星期日设为一周内的第七天
			 day_of_week = 7;
		 }
		 calendar.add(Calendar.DATE, -day_of_week + 1 - 14);	//目标日期所在周一再取两周前-14天
		res[0] = DateUtil.formatDate(calendar.getTime());
		calendar.set(year, month - 1, day);
		calendar.add(Calendar.DATE, -day_of_week + 7);	//目标日期所在周日
		res[1] = DateUtil.formatDate(calendar.getTime());
		return res;
	}
}
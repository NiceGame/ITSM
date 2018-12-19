package com.vanvalt.utils;

import java.util.Date;
import java.util.Set;

import com.vanvalt.util.constant.Constant;
import com.vanvalt.util.date.DateUtil;
import com.vanvalt.util.date.DateUtil3;

/** 
 * @author xy.li@vanvalt.com 
 * @version Created By：2015年12月11日 下午7:07:59 
 * 
 */
public class RealtimeUtil {

	
	/**
	 * 获取中文时次-For 时次列表
	 * @param fileType
	 * @param fileName
	 * @return
	 */
	public static String getCNDate(String fileType, String fileName){
		
		String datePeriod = "";
		
		if(fileType != null && !"".equals(fileType)){
			
			String fileDateStr = formatDateStr(fileName);
			Date fileDate = DateUtil.dateFormat(fileDateStr, "yyyy-MM-dd HH:mm");
			
			// 有效时段映射关系
			Set<String> keySet = Constant.REALTIME_EFFECTIVE_PERIOD_MAP.keySet();
			
			// 有效时段 default值
			int period = 0; // 具体参照 Constant.REALTIME_EFFECTIVE_PERIOD_MAP
			for(String type:keySet){
				if(type.equals(fileType)){
					period = (Integer) Constant.REALTIME_EFFECTIVE_PERIOD_MAP.get(type);
				}
			}
			
			if(period == 0){ // 当前时间点
				datePeriod = DateUtil.dateFormat(fileDate, "MM月dd日HH时");
			} else if(period > 0){ // 有效时段的开始时间为发布时间前N个小时
				// 有效时段开始时间
				Date previousDate = DateUtil.dateAdd(DateUtil.INTERVAL_HOUR, fileDate, -period);
				
				if(DateUtil3.isSameDate(fileDate, previousDate)){ // 同一天 如：2015年(11月19日01时01分-02时00分)
					datePeriod = DateUtil.dateFormat(previousDate, "MM月dd日HH时mm分")+"-"
							+DateUtil.dateFormat(fileDate, "HH时mm分");
				} else { // 不在同一天 如：2015年(12月01日08时-02日08时)
					datePeriod = DateUtil.dateFormat(previousDate, "MM月dd日HH时")+"-"
							+DateUtil.dateFormat(fileDate, "dd日HH时");
				}
				
			} else { // 根据文件后缀获取有效时段
				
				if(fileName != null && !"".equals(fileName)){
					
					String startHour = fileName.substring(8,10);
					String endHour = fileName.substring(11,13);
					
					if(Integer.valueOf(startHour) < Integer.valueOf(endHour)){ // 两个时间点在同一天 如：12月01日08时-01日18时
						
						datePeriod = DateUtil.dateFormat(fileDate, "MM月dd日")+startHour+"时-"
								+DateUtil.dateFormat(fileDate, "dd日")+endHour+"时";
					} else { // 两个时间点跨天 如：11月18日20时-19日00时
						
						Date previousDate = DateUtil.dateAdd(DateUtil.INTERVAL_DAY, fileDate, -1);
						datePeriod = DateUtil.dateFormat(previousDate, "MM月dd日")+startHour+"时-"
								+DateUtil.dateFormat(fileDate, "dd日")+endHour+"时";
					}
				}
			} 
		}

		return datePeriod.replace(" ", "");
	}
	
	/**
	 * 获取中文时段 - For 标题
	 * @param fileType
	 * @param fileName
	 * @return
	 */
	public static String getCNDateForTitle(String fileType, String fileName){
		
		String datePeriod = "";
		
		if(fileType != null && !"".equals(fileType)){
			
			String fileDateStr = formatDateStr(fileName);
			Date fileDate = DateUtil.dateFormat(fileDateStr, "yyyy-MM-dd HH:mm");
			
			// 有效时段映射关系
			Set<String> keySet = Constant.REALTIME_EFFECTIVE_PERIOD_MAP.keySet();
			
			// 有效时段 default值
			int period = 0; // 具体参照 Constant.REALTIME_EFFECTIVE_PERIOD_MAP
			for(String type:keySet){
				if(type.equals(fileType)){
					period = (Integer) Constant.REALTIME_EFFECTIVE_PERIOD_MAP.get(type);
				}
			}
			
			if(period == 0){ // 当前时间点
				datePeriod = DateUtil.dateFormat(fileDate, "MM月dd日HH时");
			} else if(period > 0){ // 有效时段的开始时间为发布时间前N个小时
				// 有效时段开始时间
				Date previousDate = DateUtil.dateAdd(DateUtil.INTERVAL_HOUR, fileDate, -period);
				
				if(DateUtil3.isSameDate(fileDate, previousDate)){ // 同一天 如：2015年(11月19日01时01分-02时00分)
					datePeriod = DateUtil.dateFormat(previousDate, "yyyy年")+"("
							+DateUtil.dateFormat(previousDate, "MM月dd日HH时mm分")+"-"
							+DateUtil.dateFormat(fileDate, "HH时mm分")+")";
				} else { // 不在同一天 如：2015年(12月01日08时-02日08时)
					datePeriod = DateUtil.dateFormat(previousDate, "yyyy年")+"("
							+DateUtil.dateFormat(previousDate, "MM月dd日HH时")+"-"
							+DateUtil.dateFormat(fileDate, "dd日HH时")+")";
				}
				
			} else { // 根据文件后缀获取有效时段
				
				if(fileName != null && !"".equals(fileName)){
					
					String startHour = fileName.substring(8,10);
					String endHour = fileName.substring(11,13);
					
					if(Integer.valueOf(startHour) < Integer.valueOf(endHour)){ // 两个时间点在同一天 如：12月01日08时-01日18时
						
						datePeriod = DateUtil.dateFormat(fileDate, "yyyy年")+"("
								+DateUtil.dateFormat(fileDate, "MM月dd日")+startHour+"时-"
								+DateUtil.dateFormat(fileDate, "dd日 ")+endHour+"时)";
					} else { // 两个时间点跨天 如：11月18日20时-19日00时
						
						Date previousDate = DateUtil.dateAdd(DateUtil.INTERVAL_DAY, fileDate, -1);
						datePeriod = DateUtil.dateFormat(previousDate, "yyyy年")+"("
								+DateUtil.dateFormat(previousDate, "MM月dd日")+startHour+"时-"
								+DateUtil.dateFormat(fileDate, "dd日")+endHour+"时)";
					}
				}
			} 
		}

		return datePeriod.replace(" ", "");
	}
	
	/**
	 * 字符串转换为日期格式 如：2015120208 -> 2015-12-01 08:00
	 * @param str
	 * @return
	 */
	public static String formatDateStr(String str){
		
		String dateStr = "";
		
		if(str != null && str.length() >= 10){
			
			if(!str.contains(Constant.STRING_LINE)){
				dateStr = dateStr.concat(str.substring(0, 4)).concat(Constant.STRING_LINE).concat(str.substring(4, 6))
						.concat(Constant.STRING_LINE).concat(str.substring(6, 8)).concat(Constant.STRING_SPACE)
						.concat(str.substring(8, 10)).concat(Constant.STRING_COLON).concat("00");
			} else {
				dateStr = str;
			}
		}
		
		return dateStr;
	}
}

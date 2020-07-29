package com.rohit.schedulingdemo.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.cj.util.StringUtils;
import com.rohit.schedulingdemo.model.IntervalConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenerateCronExpression {

	
	public static List<String> getFixedTimeCronExpressions(String listOfTimeStamps){
		
		log.info("TimeStamp: {}", listOfTimeStamps);
		List<String> cronList = new ArrayList<>();
		
		List<String> cronTokens = StringUtils.split(listOfTimeStamps, ",", true);
		
		Map<Integer, String> minuteHourMap = new HashMap<>();
		
		for(String cronToken : cronTokens) {
			log.warn("Cron Token: {}", cronToken);
			int timeStamp = Integer.parseInt(cronToken);
			int hour = timeStamp / 100;
			int min = timeStamp % 100;
			
			if(!minuteHourMap.containsKey(min)) {
				minuteHourMap.put(min, String.valueOf(hour));
			}
			else {
				String hourString = minuteHourMap.get(min);
				minuteHourMap.put(min, hourString + "," + hour);
			}
		}
		
		
		for (int min : minuteHourMap.keySet()) {
			String cronExpression = getCronExpression("0", String.valueOf(min), minuteHourMap.get(min), "*", "*", "?");
			
			log.debug("Generated CronExpression: {}", cronExpression);
			
			cronList.add(cronExpression);
		}
		
		
		
		return cronList;
	}
	
	public static List<String> getIntervalCronStatements(IntervalConfig intervalConfig){
		List<String> cronStatements = new ArrayList<>();
		
		int startHour = intervalConfig.getBeginHour()/100;
		int startMin = intervalConfig.getBeginHour()%100;
		
		int endHour = intervalConfig.getEndHour()/100;
		int endMin = intervalConfig.getEndHour()%100;
				
		String hourDifference = startHour + "-" + endHour;
		
		log.info("Interval Type: {}", intervalConfig.getIntervalType());
		if(intervalConfig.getIntervalType().equals("H")) {
			String min = String.valueOf(startMin);
			String hours = hourDifference + "/" + intervalConfig.getInterval();
			cronStatements.add(
						getCronExpression("0", min, hours, "*", "*", "?")
					);
		}
		else {
			
			String min = String.valueOf(startMin % intervalConfig.getInterval()) +  "/" + intervalConfig.getInterval();
			String hours = hourDifference;
			cronStatements.add(
					getCronExpression("0", min, hours, "*", "*", "?")
				);
		}
		
		
		return cronStatements;
	}
	
	
	static String getCronExpression(String seconds,
									String minutes,
									String hours,
									String daysOfMonth,
									String year,
									String daysOfWeek) {
		return String.join(" ", Arrays.asList(seconds,minutes,hours,daysOfMonth,year,daysOfWeek));
	}
}

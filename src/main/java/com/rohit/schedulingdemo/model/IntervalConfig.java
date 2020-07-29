package com.rohit.schedulingdemo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IntervalConfig {
	
	private int beginHour;
	private int endHour;
	private int interval;
	private String intervalType;
}

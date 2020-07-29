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
public class ScheduleJobRequest {
	private String message;

	private String timeStampList;

	private IntervalConfig intervals;
}

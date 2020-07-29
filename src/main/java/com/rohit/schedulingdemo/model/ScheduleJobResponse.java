package com.rohit.schedulingdemo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleJobResponse {
	
	
	private boolean success;
	private String jobId;
	private String jobGroup;
	private String message;
	
	public ScheduleJobResponse(boolean success, String message) {
		this.success = success;
		this.message = message;
	}
	
}

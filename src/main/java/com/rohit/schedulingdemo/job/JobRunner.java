package com.rohit.schedulingdemo.job;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JobRunner extends QuartzJobBean {@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());
		
		JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
		String message = jobDataMap.getString("message");
		
		log.warn("Message: {}", message);
		
	}

}

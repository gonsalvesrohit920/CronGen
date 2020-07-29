package com.rohit.schedulingdemo.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rohit.schedulingdemo.job.JobRunner;
import com.rohit.schedulingdemo.model.ScheduleJobRequest;
import com.rohit.schedulingdemo.model.ScheduleJobResponse;
import com.rohit.schedulingdemo.util.GenerateCronExpression;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ScheduleController {
    

    @Autowired
    private Scheduler scheduler;
    
    
    @PostMapping("/scheduleEmailCron")
    public List<String> testCronExpressions(@RequestBody ScheduleJobRequest request){
    	List<String> cronList = new ArrayList<>();
    	log.info("Request: {}", request.toString());
    	if(request.getTimeStampList() != null) {
    		cronList.addAll(
    						GenerateCronExpression.getFixedTimeCronExpressions(request.getTimeStampList())    				
    				);
    	}
    	if(request.getIntervals() != null) {
    		cronList.addAll(
    					
    				GenerateCronExpression.getIntervalCronStatements(request.getIntervals())
    				
    				);
    		
    	}
    	
    	return cronList;
    }

    @PostMapping("/scheduleEmail")
    public ResponseEntity<ScheduleJobResponse> scheduleEmail(@RequestBody ScheduleJobRequest scheduleEmailRequest) {
        try {
           
            JobDetail jobDetail = buildJobDetail(scheduleEmailRequest);
            Trigger trigger = buildJobTrigger(jobDetail);
            scheduler.scheduleJob(jobDetail, trigger);

            ScheduleJobResponse scheduleEmailResponse = new ScheduleJobResponse(true,
                    jobDetail.getKey().getName(), jobDetail.getKey().getGroup(), "Email Scheduled Successfully!");
            return ResponseEntity.ok(scheduleEmailResponse);
        } catch (SchedulerException ex) {
            log.error("Error scheduling email", ex);

            ScheduleJobResponse scheduleEmailResponse = new ScheduleJobResponse(false,
                    "Error scheduling email. Please try later!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(scheduleEmailResponse);
        }
    }

    private JobDetail buildJobDetail(ScheduleJobRequest scheduleEmailRequest) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("message", scheduleEmailRequest.getMessage());
        
        return JobBuilder.newJob(JobRunner.class)
                .withIdentity(UUID.randomUUID().toString(), "email-jobs")
                .withDescription("Send Email Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

//    private Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
      private Trigger buildJobTrigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "email-triggers")
                .withDescription("Send Email Trigger")
                .startAt(Date.from(new Date().toInstant()))
                .withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?"))
                .build();
    }
}

package com.sparkplatform.api.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class OpenHouse extends ResourceEntity {
    private static final SimpleDateFormat MDY_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
	private static Logger logger = Logger.getLogger(OpenHouse.class);
	
	@JsonProperty("Date")
	private String dateString;
	@JsonIgnore
	private Date date;
	
	@JsonProperty("StartTime")
	private String startString;
	@JsonIgnore
	private Date startTime;
	
	@JsonProperty("EndTime")
	private String endString;
	@JsonIgnore
	private Date endTime;

	public Date getDate() {
		if(date == null){
	        try {
	            date = MDY_FORMAT.parse(dateString);
	        } catch (ParseException e) {
	        	logger.debug("Failed parsing expected date format.  Trying again with standard formats.", e);
	        }
		}
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getStartTime() {
		if(startTime == null){
	        try {
	        	startTime = FORMAT.parse(dateString + " " + startString);
	        } catch (ParseException e) {
	        	logger.debug("Failed parsing expected date format.  Trying again with standard formats.", e);
	        }
		}
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		if(endTime == null){
	        try {
	        	endTime = FORMAT.parse(dateString + " " + endString);
	        } catch (ParseException e) {
	        	logger.debug("Failed parsing expected date format.  Trying again with standard formats.", e);
	        }
		}
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}

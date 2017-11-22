package com.phoobs.pointplace2.models;

import java.io.Serializable;

public class CardOpeningHoursInfo implements Serializable{
	
	private String open_day = "";
	private String open_time = "";

	public String getOpen_day() {
		return open_day;
	}
	public void setOpen_day(String open_day) {
		this.open_day = open_day;
	}
	public String getOpen_time() {
		return open_time;
	}
	public void setOpen_time(String open_time) {
		this.open_time = open_time;
	}
	
	
}

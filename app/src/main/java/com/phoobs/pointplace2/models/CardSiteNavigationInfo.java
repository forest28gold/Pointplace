package com.phoobs.pointplace2.models;

import java.io.Serializable;

public class CardSiteNavigationInfo implements Serializable{
	
	private String card_site_display_name = "";
	private String card_site_url = "";

	public String getCard_site_display_name() {
		return card_site_display_name;
	}
	public void setCard_site_display_name(String card_site_display_name) {
		this.card_site_display_name = card_site_display_name;
	}
	public String getCard_site_url() {
		return card_site_url;
	}
	public void setCard_site_url(String card_site_url) {
		this.card_site_url = card_site_url;
	}
	
	
	
	
}

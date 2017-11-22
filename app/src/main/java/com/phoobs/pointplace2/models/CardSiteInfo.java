package com.phoobs.pointplace2.models;

import java.io.Serializable;
import java.util.ArrayList;

public class CardSiteInfo implements Serializable{

	private String card_site_logo = "";
	private String card_site_content = "";
	
	
	private ArrayList<CardSiteNavigationInfo> card_site_navigation_lists = null;

	public  void addCard_site_navigation_lists(CardSiteNavigationInfo val){
		if(card_site_navigation_lists == null)
		{
			card_site_navigation_lists = new ArrayList<CardSiteNavigationInfo>();
		}
		card_site_navigation_lists.add(val);
	}
	public void removeCard_opening_hours_lists(int index){
		if(card_site_navigation_lists == null)
			return;
		card_site_navigation_lists.remove(index);
	}
	
	public ArrayList<CardSiteNavigationInfo> getCard_site_navigation_lists() {
		return card_site_navigation_lists;
	}
	public void setCard_site_navigation_lists(ArrayList<CardSiteNavigationInfo> card_site_navigation_lists) {
		this.card_site_navigation_lists = card_site_navigation_lists;
	}
	
	public String getCard_site_logo() {
		return card_site_logo;
	}
	public void setCard_site_logo(String card_site_logo) {
		this.card_site_logo = card_site_logo;
	}
	public String getCard_site_content() {
		return card_site_content;
	}
	public void setCard_site_content(String card_site_content) {
		this.card_site_content = card_site_content;
	}
	
}

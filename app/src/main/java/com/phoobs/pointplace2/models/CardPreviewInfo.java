package com.phoobs.pointplace2.models;

import java.io.Serializable;
import java.util.ArrayList;

public class CardPreviewInfo implements Serializable{
	
	private String card_title = "";
	private String card_front = "";
	private String card_logo = "";
	private String card_url = "";
	private String card_location_description = "";
	private String card_category_icon = "";
	private String card_category = "";
	private String card_latitude = "";
	private String card_longitude = "";
	
	private ArrayList<CardOpeningHoursInfo> card_opening_hours_lists = null;
	private ArrayList<CardAddressInfo> card_address_lists = null;
	private ArrayList<CardContactInfo> card_contact_lists = null;
	

	public  void addCard_address_lists(CardAddressInfo val){
		if(card_address_lists == null)
		{
			card_address_lists = new ArrayList<CardAddressInfo>();
		}
		card_address_lists.add(val);
	}
	public void removeCard_address_lists(int index){
		if(card_address_lists == null)
			return;
		card_address_lists.remove(index);
	}
	
	public  void addCard_contact_lists(CardContactInfo val){
		if(card_contact_lists == null)
		{
			card_contact_lists = new ArrayList<CardContactInfo>();
		}
		card_contact_lists.add(val);
	}
	public void removeCard_contact_lists(int index){
		if(card_contact_lists == null)
			return;
		card_contact_lists.remove(index);
	}
	
	public  void addCard_opening_hours_lists(CardOpeningHoursInfo val){
		if(card_opening_hours_lists == null)
		{
			card_opening_hours_lists = new ArrayList<CardOpeningHoursInfo>();
		}
		card_opening_hours_lists.add(val);
	}
	public void removeCard_opening_hours_lists(int index){
		if(card_opening_hours_lists == null)
			return;
		card_opening_hours_lists.remove(index);
	}
	

	public String getCard_title() {
		return card_title;
	}
	public void setCard_title(String card_title) {
		this.card_title = card_title;
	}
	public String getCard_front() {
		return card_front;
	}
	public void setCard_front(String card_front) {
		this.card_front = card_front;
	}
	public String getCard_logo() {
		return card_logo;
	}
	public void setCard_logo(String card_logo) {
		this.card_logo = card_logo;
	}
	public String getCard_url() {
		return card_url;
	}
	public void setCard_url(String card_url) {
		this.card_url = card_url;
	}
	public String getCard_location_description() {
		return card_location_description;
	}
	public void setCard_location_description(String card_location_description) {
		this.card_location_description = card_location_description;
	}
	public String getCard_category_icon() {
		return card_category_icon;
	}
	public void setCard_category_icon(String card_category_icon) {
		this.card_category_icon = card_category_icon;
	}
	public String getCard_category() {
		return card_category;
	}
	public void setCard_category(String card_category) {
		this.card_category = card_category;
	}
	public String getCard_latitude() {
		return card_latitude;
	}
	public void setCard_latitude(String card_latitude) {
		this.card_latitude = card_latitude;
	}
	public String getCard_longitude() {
		return card_longitude;
	}
	public void setCard_longitude(String card_longitude) {
		this.card_longitude = card_longitude;
	}
	
	
	public ArrayList<CardOpeningHoursInfo> getCard_opening_hours_lists() {
		return card_opening_hours_lists;
	}
	public void setCard_opening_hours_lists(ArrayList<CardOpeningHoursInfo> card_opening_hours_lists) {
		this.card_opening_hours_lists = card_opening_hours_lists;
	}
	public ArrayList<CardAddressInfo> getCard_address_lists() {
		return card_address_lists;
	}
	public void setCard_address_lists(ArrayList<CardAddressInfo> card_address_lists) {
		this.card_address_lists = card_address_lists;
	}
	public ArrayList<CardContactInfo> getCard_contact_lists() {
		return card_contact_lists;
	}
	public void setCard_contact_lists(ArrayList<CardContactInfo> card_contact_lists) {
		this.card_contact_lists = card_contact_lists;
	}
	
	
}

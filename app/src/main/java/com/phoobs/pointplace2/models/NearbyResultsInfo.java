package com.phoobs.pointplace2.models;

import java.io.Serializable;

public class NearbyResultsInfo implements Serializable{

	private String category = "";
	private String nearby_cid = "";
	private String nearby_image = "";
	private String nearby_title = "";
	private String nearby_title_es = "";
	private String nearby_title_da = "";
	private String nearby_subcategory = "";
	private String nearby_subcategory_es = "";
	private String nearby_subcategory_da = "";
	private String nearby_distance = "";
	
	
	public String getNearby_cid() {
		return nearby_cid;
	}
	public void setNearby_cid(String nearby_cid) {
		this.nearby_cid = nearby_cid;
	}
	public String getNearby_image() {
		return nearby_image;
	}
	public void setNearby_image(String nearby_image) {
		this.nearby_image = nearby_image;
	}
	public String getNearby_title() {
		return nearby_title;
	}
	public void setNearby_title(String nearby_title) {
		this.nearby_title = nearby_title;
	}
	public String getNearby_subcategory() {
		return nearby_subcategory;
	}
	public void setNearby_subcategory(String nearby_subcategory) {
		this.nearby_subcategory = nearby_subcategory;
	}
	public String getNearby_distance() {
		return nearby_distance;
	}
	public void setNearby_distance(String nearby_distance) {
		this.nearby_distance = nearby_distance;
	}
	public String getNearby_title_es() {
		return nearby_title_es;
	}
	public void setNearby_title_es(String nearby_title_es) {
		this.nearby_title_es = nearby_title_es;
	}
	public String getNearby_title_da() {
		return nearby_title_da;
	}
	public void setNearby_title_da(String nearby_title_da) {
		this.nearby_title_da = nearby_title_da;
	}
	public String getNearby_subcategory_es() {
		return nearby_subcategory_es;
	}
	public void setNearby_subcategory_es(String nearby_subcategory_es) {
		this.nearby_subcategory_es = nearby_subcategory_es;
	}
	public String getNearby_subcategory_da() {
		return nearby_subcategory_da;
	}
	public void setNearby_subcategory_da(String nearby_subcategory_da) {
		this.nearby_subcategory_da = nearby_subcategory_da;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	
	

	
}

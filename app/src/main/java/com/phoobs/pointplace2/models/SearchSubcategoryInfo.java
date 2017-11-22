package com.phoobs.pointplace2.models;

import java.io.Serializable;

public class SearchSubcategoryInfo implements Serializable{
	
	private String card_subcategory_key = "";
	private String card_subcategory = "";

	public String getCard_subcategory() {
		return card_subcategory;
	}

	public void setCard_subcategory(String card_subcategory) {
		this.card_subcategory = card_subcategory;
	}

	public String getCard_subcategory_key() {
		return card_subcategory_key;
	}

	public void setCard_subcategory_key(String card_subcategory_key) {
		this.card_subcategory_key = card_subcategory_key;
	}
	
}

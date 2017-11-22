package com.phoobs.pointplace2.models;

import java.io.Serializable;

public class SearchCityInfo implements Serializable{
	
	private String card_city_key = "";
	private String card_city = "";

	public String getCard_city() {
		return card_city;
	}

	public void setCard_city(String card_city) {
		this.card_city = card_city;
	}

	public String getCard_city_key() {
		return card_city_key;
	}

	public void setCard_city_key(String card_city_key) {
		this.card_city_key = card_city_key;
	}
	
}

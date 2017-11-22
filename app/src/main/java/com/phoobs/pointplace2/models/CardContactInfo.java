package com.phoobs.pointplace2.models;

import java.io.Serializable;

public class CardContactInfo implements Serializable{
	
	private String card_contact_label = "";
	private String card_contact_data = "";
	
	
	public String getCard_contact_label() {
		return card_contact_label;
	}
	public void setCard_contact_label(String card_contact_label) {
		this.card_contact_label = card_contact_label;
	}
	public String getCard_contact_data() {
		return card_contact_data;
	}
	public void setCard_contact_data(String card_contact_data) {
		this.card_contact_data = card_contact_data;
	}

	
	
}

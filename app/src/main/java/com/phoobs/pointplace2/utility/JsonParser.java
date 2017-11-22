package com.phoobs.pointplace2.utility;

import android.util.Log;

import com.phoobs.pointplace2.models.CardAddressInfo;
import com.phoobs.pointplace2.models.CardContactInfo;
import com.phoobs.pointplace2.models.CardOpeningHoursInfo;
import com.phoobs.pointplace2.models.CardPreviewInfo;
import com.phoobs.pointplace2.models.CardSiteInfo;
import com.phoobs.pointplace2.models.CardSiteNavigationInfo;
import com.phoobs.pointplace2.models.Global;
import com.phoobs.pointplace2.models.NearbyResultsInfo;
import com.phoobs.pointplace2.models.SearchCityInfo;
import com.phoobs.pointplace2.models.SearchSubcategoryInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class JsonParser implements Serializable{
	
	private static final long 	serialVersionUID = 1L;
	
	public static String onCheckNull( JSONObject e, String Key) {
		try {
			if ((e.getString(Key)).equals(null)){
				return "";
			} else if ((e.getString(Key)).equals("null")) {
				return "";
			} else {
				return e.getString(Key);
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			return "";
		}
	}
	
	public static ArrayList<NearbyResultsInfo> getNearbyResults(JSONObject json) {
		
		ArrayList<NearbyResultsInfo> list = new ArrayList<NearbyResultsInfo>();
		
		if(json == null)
			return list;
	    try{
	    	JSONArray  panels = json.getJSONArray(Global.KEY_RESULTS);
	        for(int i=0; i < panels.length(); i++){                          
	            JSONObject e = panels.getJSONObject(i);

	            NearbyResultsInfo info = new NearbyResultsInfo();
	            
	            info.setNearby_cid(onCheckNull(e, Global.KEY_CID));
	            info.setNearby_image(onCheckNull(e, Global.KEY_IMAGE));
	            info.setNearby_title(onCheckNull(e, Global.KEY_TITLE));
	            info.setNearby_subcategory(onCheckNull(e, Global.KEY_SUBCATEGORY));
	            info.setNearby_distance(onCheckNull(e, Global.KEY_DISTANCE));
	            
	            list.add(info);
	            
	        }
	    }catch(JSONException e){
	         Log.e("log_tag", "Error parsing data "+e.toString());
		} 
		return list;
	}
	
	public static CardPreviewInfo getCardPreviewInfo(JSONObject jsons) {
		
		CardPreviewInfo info = new CardPreviewInfo();
		if (jsons == null)
			return null;
		
		ArrayList<CardOpeningHoursInfo> opening_list = new ArrayList<CardOpeningHoursInfo>();
		ArrayList<CardAddressInfo> address_list = new ArrayList<CardAddressInfo>();
		ArrayList<CardContactInfo> contact_list = new ArrayList<CardContactInfo>();
		
		try {
			
			JSONObject  json = jsons.getJSONObject(Global.KEY_RESULTS);
			
			info.setCard_logo(onCheckNull(json, Global.KEY_LOGO));
			info.setCard_category(onCheckNull(json, Global.KEY_CATEGORY));
			info.setCard_title(onCheckNull(json, Global.KEY_TITLE));
			info.setCard_category_icon(onCheckNull(json, Global.KEY_CATEGORY_ICON));
			
			String address_string = onCheckNull(json, Global.KEY_ADDRESS);
			
			JSONObject panel_address = new JSONObject(address_string);
			
			Iterator<String> panel_address_Keys = panel_address.keys();

			while(panel_address_Keys.hasNext()) {
	        	String address_Key = panel_address_Keys.next();
	        	
	        	CardAddressInfo address_info = new CardAddressInfo();
	        	
	        	address_info.setCard_address(onCheckNull(panel_address, address_Key));
	        	
	        	address_list.add(address_info);
			}
			
			info.setCard_address_lists(address_list);
			
			info.setCard_longitude(onCheckNull(json, Global.KEY_LONGITUDE));
			info.setCard_front(onCheckNull(json, Global.KEY_FRONT));
			info.setCard_latitude(onCheckNull(json, Global.KEY_LATITUDE));
			info.setCard_location_description(onCheckNull(json, Global.KEY_LOCATION_DESCRIPTION));
			
			String contact_string = onCheckNull(json, Global.KEY_CONTACT);
			
			JSONObject panel_contact = new JSONObject(contact_string);
			
			Iterator<String> panel_contact_Keys = panel_contact.keys();

			while(panel_contact_Keys.hasNext()) {
	        	String contact_Key = panel_contact_Keys.next();
	        	
	        	JSONObject panels = panel_contact.getJSONObject(contact_Key);
	        	JSONObject panel = panels.getJSONObject(Global.KEY_ONE);
	        	
	        	CardContactInfo contact_info = new CardContactInfo();
	        	
	        	contact_info.setCard_contact_label(onCheckNull(panel, Global.KEY_LABEL));
	        	contact_info.setCard_contact_data(onCheckNull(panel, Global.KEY_DATA));
	        	
	        	contact_list.add(contact_info);
			}
			
			info.setCard_contact_lists(contact_list);
			
			info.setCard_url(onCheckNull(json, Global.KEY_URL));
			
			String opening_string = onCheckNull(json, Global.KEY_OPENING_HOURS);
			
			JSONObject panel_opening = new JSONObject(opening_string);
			
			Iterator<String> panel_opening_Keys = panel_opening.keys();

			while(panel_opening_Keys.hasNext()) {
	        	String opening_Key = panel_opening_Keys.next();
	        	
	        	CardOpeningHoursInfo opening_info = new CardOpeningHoursInfo();
	        	
	        	opening_info.setOpen_day(opening_Key);
	        	opening_info.setOpen_time(onCheckNull(panel_opening, opening_Key));
	        	
	        	opening_list.add(opening_info);
			}
			
			info.setCard_opening_hours_lists(opening_list);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e("log_tag", "Error parsing data "+e.toString());
		}
		return info;
	}
	
	
	public static CardSiteInfo getCardSiteInfo(JSONObject jsons) {
		
		CardSiteInfo info = new CardSiteInfo();
		if (jsons == null)
			return null;
		
		ArrayList<CardSiteNavigationInfo> navigation_list = new ArrayList<CardSiteNavigationInfo>();
		
		try {
			
			JSONObject  json = jsons.getJSONObject(Global.KEY_RESULTS);
			
			info.setCard_site_logo(onCheckNull(json, Global.KEY_LOGO));
			info.setCard_site_content(onCheckNull(json, Global.KEY_HTML));
			
			JSONArray  panels = json.getJSONArray(Global.KEY_NAVIGATION);
			
	        for(int i=0; i < panels.length(); i++){   
	        	
	            JSONObject e = panels.getJSONObject(i);

	            CardSiteNavigationInfo navigation_info = new CardSiteNavigationInfo();
	            
	            navigation_info.setCard_site_display_name(onCheckNull(e, Global.KEY_DISPLAY_NAME));
	        	navigation_info.setCard_site_url(onCheckNull(e, Global.KEY_URL));
	            
	        	navigation_list.add(navigation_info);
	            
	        }
			
			info.setCard_site_navigation_lists(navigation_list);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e("log_tag", "Error parsing data "+e.toString());
		}
		return info;
	}
	
	
	public static ArrayList<SearchSubcategoryInfo> getSearchSubcategory(JSONObject json) {
		
		ArrayList<SearchSubcategoryInfo> list = new ArrayList<SearchSubcategoryInfo>();
		
		if(json == null)
			return list;
	    try{
	    	
	    	String panels = onCheckNull(json, Global.KEY_SEARCH_CATEGORIES);
			
			JSONObject panel_subcategories = new JSONObject(panels);
			
			Iterator<String> panel_subcategories_Keys = panel_subcategories.keys();

			while(panel_subcategories_Keys.hasNext()) {
	        	String subcategory_Key = panel_subcategories_Keys.next();
	        	
	        	SearchSubcategoryInfo subcategory_info = new SearchSubcategoryInfo();
	        	
	        	subcategory_info.setCard_subcategory_key(subcategory_Key);
	        	subcategory_info.setCard_subcategory(onCheckNull(panel_subcategories, subcategory_Key));
	        	
	        	list.add(subcategory_info);
			}
	    	
	    }catch(JSONException e){
	         Log.e("log_tag", "Error parsing data "+e.toString());
		} 
		return list;
	}
	
	public static ArrayList<SearchCityInfo> getSearchCity(JSONObject json) {
		
		ArrayList<SearchCityInfo> list = new ArrayList<SearchCityInfo>();
		
		if(json == null)
			return list;
	    try{
	    	
	    	String panels = onCheckNull(json, Global.KEY_SEARCH_CITYLIST);
			
			JSONObject panel_citylist = new JSONObject(panels);
			
			Iterator<String> panel_citylist_Keys = panel_citylist.keys();

			while(panel_citylist_Keys.hasNext()) {
	        	String city_Key = panel_citylist_Keys.next();
	        	
	        	SearchCityInfo city_info = new SearchCityInfo();
	        	
	        	city_info.setCard_city_key(city_Key);
	        	city_info.setCard_city(onCheckNull(panel_citylist, city_Key));
	        	
	        	list.add(city_info);
			}
	    	
	    }catch(JSONException e){
	         Log.e("log_tag", "Error parsing data "+e.toString());
		} 
		return list;
	}

}

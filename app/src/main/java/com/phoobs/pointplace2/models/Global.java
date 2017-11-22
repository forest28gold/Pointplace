package com.phoobs.pointplace2.models;

import android.support.v4.app.FragmentManager;

import com.phoobs.pointplace2.utility.DBService;

import java.util.ArrayList;


public class Global
{
	public static  ArrayList<SearchSubcategoryInfo> searchsubcategorylist = null;
	public static  ArrayList<SearchCityInfo> searchcitylist = null;

	/*
	 * API URL.
	 */
	public static final String API_KEY = "xq-----------------2X";

	public static final String SERVER_URL = "http://ap---------------u";
	
	public static final String LOCATION_URL = String.format("%s/location", SERVER_URL);

	public static final String NEARBY_URL = String.format("%s/search/nearby", SERVER_URL);
	public static final String CARDPREVIEW_URL = String.format("%s/card/preview", SERVER_URL);
	public static final String CARDSITE_URL = String.format("%s/card/site", SERVER_URL);
	public static final String SEARCH_URL = String.format("%s/search/query", SERVER_URL);
	public static final String SUBCATEGORY_URL = String.format("%s/resources/subcategories", SERVER_URL);
	public static final String CITYLIST_URL = String.format("%s/resources/citylist", SERVER_URL);
	
	/*
	 * Local DB Info.	
	 */
	
	public static final String LOCAL_DB_NAME = "poi------------";
	public static final String LOCAL_TABLE_FAVORITE = "phoobs_favorite_table";

	public static final String LOCAL_FIELD_FAVORITE_CATEGORY = "category";
	public static final String LOCAL_FIELD_FAVORITE_CID = "cid";
	public static final String LOCAL_FIELD_FAVORITE_TITLE = "title";
	public static final String LOCAL_FIELD_FAVORITE_SUBCATEGORY = "subcategory";
	public static final String LOCAL_FIELD_FAVORITE_IMAGE = "image";
	public static final String LOCAL_FIELD_FAVORITE_DISTANCE = "distance";

	public static final String LOCAL_VALUE_SETTINGS_EN = "en";
	public static final String LOCAL_VALUE_SETTINGS_ES = "es";
	public static final String LOCAL_VALUE_SETTINGS_DA = "da";
	
	/*
	 * Network error codes.	
	 */
	public static final int OK = 200;
	public static final int CONNECTION_ERROR = 201;
	public static final int SYSTEM_ERROR = 202;
	public static final int CONNECT_TIMEOUT = 203;
	public static final int READ_TIMEOUT = 204;
	public static final int IO_ERROR = 205;	
	public static final int CLIENT_PROTOCOL_ERROR = 206;
	
	/*
	 * Fragment Info.	
	 */
	
	public static final String FRAGMENT_HOME = "home";
	public static final String FRAGMENT_PROFILE = "profile";
	public static final String FRAGMENT_FAVORITES = "favorites";
	public static final String FRAGMENT_SETTINGS = "settings";
	public static final String FRAGMENT_BUSINESS = "business";
	public static final String FRAGMENT_ANALYTICS = "analytics";
	public static final String FRAGMENT_LOGOUT = "logout";
	
	public static final String FRAGMENT_CARDPREVIEW = "cardpreview";
	public static final String FRAGMENT_CARDSITE = "cardsite";

	/*
	 * Search Info.	
	 */
	
	public static final String SEARCH_CATEGORY = "category";
	public static final String SEARCH_SUBCATEGORY = "subcategory";
	public static final String SEARCH_NEARBY = "nearby";
	public static final String SEARCH_MAIN = "search_main";
	
	/*
	 * Global Variable.	
	 */
	
	public static DBService dbService = null;
	public static String latitude = "";
	public static String longitude = "";
	public static String settings_language = "en";
	public static FragmentManager fragmentManager = null;
	public static String card_cid = "";
	public static String card_site_page = "";
	public static String category_number = "";
	public static Boolean is_card_site = false;
	
	public static String location_info = "";
	
	/*
	 * API Key.	
	 */

	public static final String KEY_SUCCESS = "success";
	public static final String KEY_ERROR = "error";
	public static final String KEY_RESULTS = "results";

	public static final String KEY_CID = "cid";
	public static final String KEY_IMAGE = "image";
	public static final String KEY_TITLE = "title";
	public static final String KEY_SUBCATEGORY = "subcategory";
	public static final String KEY_DISTANCE = "distance";
	
	public static final String KEY_OPENING_HOURS = "opening_hours";
	public static final String KEY_FRONT = "front";
	public static final String KEY_LOGO = "logo";
	public static final String KEY_URL = "url";
	public static final String KEY_LOCATION_DESCRIPTION = "location_description";
	public static final String KEY_ADDRESS = "address";
	public static final String KEY_CONTACT = "contact";
	public static final String KEY_ONE = "1";
	public static final String KEY_LABEL = "label";
	public static final String KEY_DATA = "data";
	public static final String KEY_CATEGORY_ICON = "category_icon";
	public static final String KEY_CATEGORY = "category";
	public static final String KEY_LATITUDE = "latitude";
	public static final String KEY_LONGITUDE = "longitude";
	
	public static final String KEY_SEARCH_CATEGORIES = "categories";
	public static final String KEY_SEARCH_CITYLIST = "citylist";
	
	public static final String KEY_NAVIGATION = "navigation";
	public static final String KEY_DISPLAY_NAME = "display_name";
	public static final String KEY_HTML = "html";
	

}

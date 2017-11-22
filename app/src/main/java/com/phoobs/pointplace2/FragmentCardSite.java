package com.phoobs.pointplace2;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.phoobs.pointplace2.models.CardSiteInfo;
import com.phoobs.pointplace2.models.Global;
import com.phoobs.pointplace2.utility.JsonParser;
import com.phoobs.pointplace2.utility.NetworkHelper;
import com.phoobs.pointplace2.utility.Response;

import org.json.JSONException;
import org.json.JSONObject;


@SuppressLint("NewApi") public class FragmentCardSite extends Fragment {

	private SVProgressHUD mSVProgressHUD;

	private View view_card_site;

	private Boolean is_SiteMenu;

	private HttpGetTask card_site_task;

	private String is_select_menu = "";
	private CardSiteInfo cardSiteInfo = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		view_card_site = inflater.inflate(R.layout.fragment_card_site, container, false);
		
		is_select_menu = "card_site";
		
		Global.card_site_page = "home";
		
		is_SiteMenu = false;
        
        if (is_SiteMenu) {
			ShowSiteMenuList();
		} else {
			removeSiteMenuList();
		}

		Button btn_nav_site_menu = (Button) view_card_site.findViewById(R.id.button_nav_site_menu);
		Button btn_nav_site_info = (Button) view_card_site.findViewById(R.id.button_nav_info);
        
        btn_nav_site_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				if (!is_SiteMenu) {
					ShowSiteMenuList();
					is_SiteMenu = true;
				} else {
					removeSiteMenuList();
					is_SiteMenu = false;
				}
			}
		});
        
        btn_nav_site_info.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				getFragmentManager().popBackStack();
				Global.is_card_site = false;
			}
		});

		mSVProgressHUD = new SVProgressHUD(getActivity());
		mSVProgressHUD.showWithStatus(getString(R.string.please_wait), SVProgressHUD.SVProgressHUDMaskType.Gradient);
        onGetCardSite(Global.card_site_page);
		
		return view_card_site;
	}
	
	public void removeContent() {
		LinearLayout list = (LinearLayout) view_card_site.findViewById(R.id.linearlayout_list);
		list.removeAllViews();
    }
    
    public void removeSiteMenuList() {
		LinearLayout list = (LinearLayout) view_card_site.findViewById(R.id.linearLayout_site_menu_list);
		list.removeAllViews();
    }
    
    
    public void onGetCardSite(String page) {
    	
    	String url = Global.CARDSITE_URL;
    	card_site_task = new HttpGetTask();

		url += "/?cid=" + Global.card_cid
				+ "&page=" + page
				+ "&lg=" + Global.settings_language
				+ "&api_key=" + Global.API_KEY;

		Log.d("CardSiteURL", "URL: " + url);
		
		card_site_task.execute(url);
    	
    }
    
    private void processCardSiteResponse(String response) {

		removeContent();

		if (response == null || response.equals("")) {
			return;
		} else {
			
			JSONObject json = null;
			try {
				json = new JSONObject(response);
			} catch (JSONException e) {
				return;
			} 

			cardSiteInfo = JsonParser.getCardSiteInfo(json);
			if (cardSiteInfo == null) {
				return;
			}
			
			showCardSite();
			return;
		}
	}
    
	// async task to make network requests in separate thread
	public class HttpGetTask extends AsyncTask<String, Void, Response> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// display a ProgressDialog with message
		}

		@Override
		protected Response doInBackground(String... params) {
			// call method to initiate HTTP request
			
			return NetworkHelper.doGet(params[0], getActivity());
		}

		@Override
		protected void onPostExecute(Response result) {
			super.onPostExecute(result);

			mSVProgressHUD.dismiss();
			// show the result
			if (result.getStatusCode() == Global.OK) {
				String responseCardSiteData = "";
				responseCardSiteData = result.getResponseText();
				Log.d("CardSiteURL", "RESPONSE: " + responseCardSiteData);
				processCardSiteResponse(responseCardSiteData);
			} else {
				String responseCardSiteData = "";
				processCardSiteResponse(responseCardSiteData);
			}
		}
	}
	
	private void fitWidth(Bitmap bitmap, ImageView imageView) {
		int orgWid = bitmap.getWidth();
		int orgHeight = bitmap.getHeight();
		
		LayoutParams params = new LayoutParams(orgWid, orgHeight);
		imageView.setLayoutParams(params);
	}
	
	@SuppressLint("SetJavaScriptEnabled") public void showCardSite() {
		
		String logo_image = cardSiteInfo.getCard_site_logo();
		
		if (is_select_menu.equals("card_site")) {
			
			ImageView img_card_logo = (ImageView) view_card_site.findViewById(R.id.imageView_site_logo);
			
			if (logo_image.equals("")) {
				img_card_logo.setImageResource(R.mipmap.site_logo);
			} else {
				UrlImageViewHelper.setUrlDrawable(img_card_logo, logo_image, R.mipmap.site_logo, new UrlImageViewCallback() {
					@Override
					public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url, boolean loadedFromCache) {
					}
				});
			}
			
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.site_logo);
			fitWidth(bitmap, img_card_logo);
		} 
		
		LinearLayout list = (LinearLayout) view_card_site.findViewById(R.id.linearlayout_list);

		final LinearLayout newCell = (LinearLayout) (View.inflate(getActivity(), R.layout.cell_card_site_content, null));
		
		WebView webView = (WebView) newCell.findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadData(cardSiteInfo.getCard_site_content(), "text/html; charset=utf-8", "UTF-8");
		
		newCell.setTag(0);
		registerForContextMenu(newCell);
		list.addView(newCell);
	}
	
    
    public void ShowSiteMenuList() {
    	
    	removeSiteMenuList();
		
		int length = cardSiteInfo.getCard_site_navigation_lists().size();

		for (int number = 0; number < length; number++) {
			
			String menu = cardSiteInfo.getCard_site_navigation_lists().get(number).getCard_site_display_name();
			String page = cardSiteInfo.getCard_site_navigation_lists().get(number).getCard_site_url();
			
			appendSiteMenu(number, menu, page);
		}

	}
	
	public void appendSiteMenu(int number, String menu, final String page) {
		
		LinearLayout list = (LinearLayout) view_card_site.findViewById(R.id.linearLayout_site_menu_list);

		final LinearLayout newCell = (LinearLayout) (View.inflate(getActivity(), R.layout.cell_card_site_menu, null));
		
		((Button) (newCell.findViewById(R.id.button_site_menu))).setText(menu);
		
		((Button) (newCell.findViewById(R.id.button_site_menu))).setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				
				removeSiteMenuList();
				is_SiteMenu = false;
				
				removeContent();

				mSVProgressHUD = new SVProgressHUD(getActivity());
				mSVProgressHUD.showWithStatus(getString(R.string.please_wait), SVProgressHUD.SVProgressHUDMaskType.Gradient);
				
				is_select_menu = page;
				
				onGetCardSite(page);
			}
		});
		
		newCell.setTag(number);
		registerForContextMenu(newCell);
		list.addView(newCell);

	}
	
}

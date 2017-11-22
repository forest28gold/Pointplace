package com.phoobs.pointplace2;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.phoobs.pointplace2.models.Global;
import com.phoobs.pointplace2.models.NearbyResultsInfo;
import com.phoobs.pointplace2.utility.RectImageView;

import java.util.ArrayList;


@SuppressLint("NewApi") public class FragmentFavorites extends Fragment {
	
	private View view_favorites;
	RelativeLayout relativeLayout_no_result;

	private ArrayList<NearbyResultsInfo> favoritelist;
	int length;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		view_favorites = inflater.inflate(R.layout.fragment_favorites, container, false);

		relativeLayout_no_result = (RelativeLayout) view_favorites.findViewById(R.id.relativeLayout_no_result);
		relativeLayout_no_result.setVisibility(View.INVISIBLE);

		favoritelist = onGetFavoriteResults();
		
		if (favoritelist == null) {
			relativeLayout_no_result.setVisibility(View.VISIBLE);
		} else {
			showFavorites();
		}
		
		return view_favorites;
	}

	public ArrayList<NearbyResultsInfo> onGetFavoriteResults() {
		
		ArrayList<NearbyResultsInfo> list = new ArrayList<NearbyResultsInfo>();
		
		String sql = "select * " + " from " + Global.LOCAL_TABLE_FAVORITE;
		
		Cursor cursor = Global.dbService.query(sql, null);
		getActivity().startManagingCursor(cursor);
		if (cursor == null) {
			return null;
		} else {
			int nums = cursor.getCount();
			if (nums == 0) {
				getActivity().stopManagingCursor(cursor);
				cursor.close();
				// dbService.close();
				return null;
			} else {
				while (cursor.moveToNext()) {
					NearbyResultsInfo info = new NearbyResultsInfo();
					
					String category = cursor.getString(1);
					String cid = cursor.getString(2);
					String image = cursor.getString(3);
					String title = cursor.getString(4);
					String subcategory = cursor.getString(5);
					String distance = cursor.getString(6);
					
					info.setCategory(category);
					info.setNearby_cid(cid);
					info.setNearby_image(image);
					info.setNearby_title(title);
					info.setNearby_subcategory(subcategory);
					info.setNearby_distance(distance);
					
					list.add(info);
					
				}
				getActivity().stopManagingCursor(cursor);
				cursor.close();
				// dbService.close();
				return list;
			}
		}
	}

	public void showFavorites() {
		
		length = favoritelist.size();

		for (int number = 0; number < length; number++) {
			
			String category = favoritelist.get(number).getCategory();
			String cid = favoritelist.get(number).getNearby_cid();
			String image = favoritelist.get(number).getNearby_image();
			String title = favoritelist.get(number).getNearby_title();
			String subcategory = favoritelist.get(number).getNearby_subcategory();
			String distance = favoritelist.get(number).getNearby_distance();
			
			appendFavorites(number, category, cid, image, title, subcategory, distance);
		
		}
	}
	
	public void appendFavorites(final int number, final String category, final String cid, final String image, final String title, final String subcategory, final String distance) {
		
		final LinearLayout list = (LinearLayout) view_favorites.findViewById(R.id.linearlayout_list);

		final LinearLayout newCell = (LinearLayout) (View.inflate(getActivity(), R.layout.cell_menu_result, null));
		
		RectImageView user_photo = (RectImageView)(newCell.findViewById(R.id.rectImageView_result));
		if (image.equals("")) {
			user_photo.setImageResource(R.mipmap.image_loading);
		} else {
			UrlImageViewHelper.setUrlDrawable(user_photo, image, R.mipmap.image_loading, new UrlImageViewCallback() {
				@Override
				public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url, boolean loadedFromCache) {
				}
			});
		}
		
		((TextView) (newCell.findViewById(R.id.textView_description))).setText(title);
		
		if (subcategory.equals("null")) {
			((TextView) (newCell.findViewById(R.id.textView_subcategory))).setText("");
		} else {
			((TextView) (newCell.findViewById(R.id.textView_subcategory))).setText(subcategory);
		}
		
		((TextView) (newCell.findViewById(R.id.textView_distance))).setText(distance);
		((TextView) (newCell.findViewById(R.id.textView_distance))).setVisibility(View.INVISIBLE);
		
		((ImageView) (newCell.findViewById(R.id.imageView_distance))).setVisibility(View.INVISIBLE);
		
		((Button) (newCell.findViewById(R.id.button_like))).setBackgroundResource(R.mipmap.like_button_selected);
		
		((Button) (newCell.findViewById(R.id.button_like))).setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				
				removeFavoriteDB(category, title, distance);
				list.removeView(newCell);
				length--;
				if (length == 0) {
					relativeLayout_no_result.setVisibility(View.VISIBLE);
				}
			}
		});
		
		((Button) (newCell.findViewById(R.id.button_share))).setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {

				Intent intent = new Intent(getActivity(), CardActivity.class);
				intent.putExtra("cid", cid);
				intent.putExtra("category", category);
				startActivity(intent);
			}
		});
		
		newCell.setTag(number);
		registerForContextMenu(newCell);
		list.addView(newCell);
		
		newCell.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				
				Intent intent = new Intent(getActivity(), CardActivity.class);
				intent.putExtra("cid", cid);
				intent.putExtra("category", category);
				startActivity(intent);
			}
		});
	}
	
	public void removeFavoriteDB(String category, String title, String distance) {
		
		String sql = "delete from " + Global.LOCAL_TABLE_FAVORITE
				+ " where " + Global.LOCAL_FIELD_FAVORITE_CATEGORY + "=?"
				+ " and " + Global.LOCAL_FIELD_FAVORITE_TITLE + "=?"
				+ " and " + Global.LOCAL_FIELD_FAVORITE_DISTANCE + "=?";
		
		Object[] args = new Object[] { category, title, distance };
		Global.dbService.execSQL(sql, args);
	}
	
}

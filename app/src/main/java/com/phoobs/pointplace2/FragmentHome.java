package com.phoobs.pointplace2;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("NewApi") public class FragmentHome extends Fragment {
	
	public View view_home;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		view_home = inflater.inflate(R.layout.fragment_home, container, false);
		
		showHomeMenu();
		
		return view_home;
	}
	
	public void showHomeMenu() {
		
		appendHomeMenuHeader();

		int length = 6;

		for (int number = 1; number <= length; number++) {
			
			appendHomeMenu(number);
		}
		
		appendHomeMenuFooter();
	}
	
	public void appendHomeMenuHeader() {
		
		LinearLayout list = (LinearLayout) view_home.findViewById(R.id.linearlayout_list);

		final LinearLayout newCell = (LinearLayout) (View.inflate(getActivity(), R.layout.cell_home_menu_header, null));
		
		((RelativeLayout) (newCell.findViewById(R.id.relativelayout_background))).setBackgroundResource(R.mipmap.home_menu00);
		
		((ImageView) (newCell.findViewById(R.id.imageView_home_menu_icon))).setImageResource(R.mipmap.home_icon00);
		
		((TextView) (newCell.findViewById(R.id.textView_home_menu))).setText(R.string.str_home_menu00);
		
		((ImageButton) (newCell.findViewById(R.id.imageButton_menu_header))).setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				
				Intent intent = new Intent(getActivity(), CategoryActivity.class);
				intent.putExtra("home_menu", "00");
				startActivity(intent);
			}
		
		});
		
		newCell.setTag(0);
		registerForContextMenu(newCell);
		list.addView(newCell);

	}
	
	public int getMenuResources(String name, int number, int i, String type) {
		
		String res_name = "";
		res_name = name + String.valueOf(number) + String.valueOf(i);
		int resource = getResources().getIdentifier(res_name, type, getActivity().getPackageName());
		
		return resource;
	}

	public void appendHomeMenu(final int number) {
		
		LinearLayout list = (LinearLayout) view_home.findViewById(R.id.linearlayout_list);

		final LinearLayout newCell = (LinearLayout) (View.inflate(getActivity(), R.layout.cell_home_menu, null));
		
		int backImage1 = getMenuResources("home_menu", number, 1, "mipmap");
		int backImage2 = getMenuResources("home_menu", number, 2, "mipmap");
		int iconImage1 = getMenuResources("home_icon", number, 1, "mipmap");
		int iconImage2 = getMenuResources("home_icon", number, 2, "mipmap");
		int menuString1 = getMenuResources("str_home_menu", number, 1, "string");
		int menuString2 = getMenuResources("str_home_menu", number, 2, "string");
		
		((RelativeLayout) (newCell.findViewById(R.id.relativelayout_menu1))).setBackgroundResource(backImage1);
		((RelativeLayout) (newCell.findViewById(R.id.relativelayout_menu2))).setBackgroundResource(backImage2);
		
		((ImageView) (newCell.findViewById(R.id.imageView_home_menu_icon1))).setImageResource(iconImage1);
		((ImageView) (newCell.findViewById(R.id.imageView_home_menu_icon2))).setImageResource(iconImage2);
		
		((TextView) (newCell.findViewById(R.id.textView_home_menu1))).setText(menuString1);
		((TextView) (newCell.findViewById(R.id.textView_home_menu2))).setText(menuString2);
		
		((ImageButton) (newCell.findViewById(R.id.imageButton_menu1))).setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				
				String menu_number = String.valueOf(number) + "1";
				
				Intent intent = new Intent(getActivity(), CategoryActivity.class);
				intent.putExtra("home_menu", menu_number);
				startActivity(intent);
			}
		
		});
		
		((ImageButton) (newCell.findViewById(R.id.imageButton_menu2))).setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				
				String menu_number = String.valueOf(number) + "2";
				
				Intent intent = new Intent(getActivity(), CategoryActivity.class);
				intent.putExtra("home_menu", menu_number);
				startActivity(intent);
			}
		
		});
		
		newCell.setTag(number);
		registerForContextMenu(newCell);
		list.addView(newCell);

	}
	
	
	public void appendHomeMenuFooter() {
		
		LinearLayout list = (LinearLayout) view_home.findViewById(R.id.linearlayout_list);

		final LinearLayout newCell = (LinearLayout) (View.inflate(getActivity(), R.layout.cell_home_menu_footer, null));
		
		((TextView) (newCell.findViewById(R.id.textView_copyright))).setText(R.string.str_footer_copyright);
		
		newCell.setTag(0);
		registerForContextMenu(newCell);
		list.addView(newCell);

	}
	
}

package com.phoobs.pointplace2;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.phoobs.pointplace2.models.CardPreviewInfo;
import com.phoobs.pointplace2.models.Global;
import com.phoobs.pointplace2.utility.JsonParser;
import com.phoobs.pointplace2.utility.NetworkHelper;
import com.phoobs.pointplace2.utility.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;


@SuppressLint("NewApi")
public class FragmentCardPreview extends Fragment {

    private static final String TAG = "FragmentCardPreview";

    private SVProgressHUD mSVProgressHUD;

    private View view_card_preview;
    Button btn_nav_back, btn_nav_site;
    ImageView img_card_category;
    TextView txt_card_category;
    ScrollView scroll_card;
    ScrollView scroll_list;

    private MapView mapView;
    private GoogleMap mMap;
    public LatLng center;

    public HttpGetTask card_preview_task;

    private static String[] PERMISSIONS_LOCATION = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private static String[] PERMISSIONS_CALL = {
            android.Manifest.permission.CALL_PHONE
    };

    private CardPreviewInfo cardPreviewInfo = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view_card_preview = inflater.inflate(R.layout.fragment_card_preview, container, false);

        mapView = (MapView) view_card_preview.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_LOCATION, 1);
                }
                mMap.setMyLocationEnabled(true);
                // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
                try {
                    MapsInitializer.initialize(getActivity());
                } catch (Exception e) {

                }

                mapView.onResume();
            }
        });

        img_card_category = (ImageView) view_card_preview.findViewById(R.id.imageView_card_preview_category);
        txt_card_category = (TextView) view_card_preview.findViewById(R.id.textView_card_preview_category);

        scroll_card = (ScrollView) view_card_preview.findViewById(R.id.scrollView_card);
        scroll_list = (ScrollView) view_card_preview.findViewById(R.id.scrollView_list);

        btn_nav_back = (Button) view_card_preview.findViewById(R.id.button_nav_back);
        btn_nav_site = (Button) view_card_preview.findViewById(R.id.button_nav_site);

        btn_nav_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                getActivity().finish();
            }
        });

        btn_nav_site.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                flipCard();
            }
        });

        int iconImage = getMenuResources("home_icon", Global.category_number, "mipmap");
        int menuString = getMenuResources("str_home_menu", Global.category_number, "string");

        ((ImageView) (view_card_preview.findViewById(R.id.imageView_card_preview_category))).setImageResource(iconImage);
        ((TextView) (view_card_preview.findViewById(R.id.textView_card_preview_category))).setText(menuString);

        mSVProgressHUD = new SVProgressHUD(getActivity());
        mSVProgressHUD.showWithStatus(getString(R.string.please_wait), SVProgressHUD.SVProgressHUDMaskType.Gradient);

        onGetCardPreview();

        return view_card_preview;
    }

    public void flipCard() {

        Global.is_card_site = true;

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .replace(R.id.container, new FragmentCardSite())
                .addToBackStack(null)
                .commit();
    }

    public int getMenuResources(String name, String number, String type) {

        String res_name = "";
        res_name = name + number;
        int resource = getResources().getIdentifier(res_name, type, getActivity().getPackageName());
        return resource;
    }

    public void onGetCardPreview() {

        String url = Global.CARDPREVIEW_URL;
        card_preview_task = new HttpGetTask();

        url += "/?cid=" + Global.card_cid
                + "&lg=" + Global.settings_language
                + "&api_key=" + Global.API_KEY;

        Log.d("CardPreviewURL", "URL: " + url);

        card_preview_task.execute(url);

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
                String responseCardPreviewData = "";
                responseCardPreviewData = result.getResponseText();
                Log.d("CardPreviewURL", "RESPONSE: " + responseCardPreviewData);
                processCardPreviewResponse(responseCardPreviewData);
            } else {
                String responseCardPreviewData = "";
                processCardPreviewResponse(responseCardPreviewData);
            }
        }
    }

    private void processCardPreviewResponse(String response) {

        if (response == null || response.equals("")) {
            return;
        } else {

            JSONObject json = null;
            try {
                json = new JSONObject(response);
            } catch (JSONException e) {
                return;
            }

            cardPreviewInfo = JsonParser.getCardPreviewInfo(json);
            if (cardPreviewInfo == null) {
                return;
            }

            showCardPreview();
            return;
        }
    }

    public void showCardPreview() {

        showCardHeader();
        appendCardPreviewHeader();
        setPlaceLocation();
        appendCardPreviewFooter();
    }

    public void showCardHeader() {

        String header_image = cardPreviewInfo.getCard_front();

        ImageView img_card_header = (ImageView) view_card_preview.findViewById(R.id.imageView_card_preview_header);
        if (header_image.equals("")) {
            img_card_header.setImageResource(R.mipmap.top_card_prev_img_bg);
        } else {
            UrlImageViewHelper.setUrlDrawable(img_card_header, header_image, R.mipmap.top_card_prev_img_bg, new UrlImageViewCallback() {
                @Override
                public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url, boolean loadedFromCache) {
                }
            });
        }
    }

    public void setPlaceLocation() {

        double latitude = Double.parseDouble(cardPreviewInfo.getCard_latitude());
        double longitude = Double.parseDouble(cardPreviewInfo.getCard_longitude());

        if (latitude != 0.0 & longitude != 0.0) {

            MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(cardPreviewInfo.getCard_title());
            center = new LatLng(latitude, longitude);
            mMap.addMarker(marker);
            mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(center), 1750, null);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                CameraUpdate camera = CameraUpdateFactory
                        .newCameraPosition(new CameraPosition.Builder()
                                .target(marker.getPosition()).zoom(14).build());
                mMap.animateCamera(camera);
                mMap.moveCamera(camera);
                return false;
            }
        });
    }

    private void fitWidth(Bitmap bitmap, ImageView imageView) {
        int orgWid = bitmap.getWidth();
        int orgHeight = bitmap.getHeight();

        LayoutParams params = new LayoutParams(orgWid, orgHeight);
        imageView.setLayoutParams(params);
    }

    public void appendCardPreviewHeader() {

        LinearLayout list = (LinearLayout) view_card_preview.findViewById(R.id.linearlayout_header);

        final LinearLayout newCell = (LinearLayout) (View.inflate(getActivity(), R.layout.cell_card_header, null));

        String logo_image = cardPreviewInfo.getCard_logo();

        ImageView img_card_logo = (ImageView) newCell.findViewById(R.id.imageView_card_preview_logo);

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

        TextView txt_card_title = (TextView) newCell.findViewById(R.id.textView_card_title);
        txt_card_title.setText(cardPreviewInfo.getCard_title());

        TextView txt_card_subcategory = (TextView) newCell.findViewById(R.id.textView_subcategory);
        txt_card_subcategory.setText(cardPreviewInfo.getCard_category());

        TextView txt_card_description = (TextView) newCell.findViewById(R.id.textView_card_description);
        txt_card_description.setText(cardPreviewInfo.getCard_location_description());

        newCell.setTag(0);
        registerForContextMenu(newCell);
        list.addView(newCell);

    }

    public void appendCardPreviewFooter() {

        LinearLayout list = (LinearLayout) view_card_preview.findViewById(R.id.linearlayout_footer);

        final LinearLayout newCell = (LinearLayout) (View.inflate(getActivity(), R.layout.cell_card_footer, null));

        if (cardPreviewInfo.getCard_address_lists() != null) {

            int length = cardPreviewInfo.getCard_address_lists().size();

            Collections.reverse(cardPreviewInfo.getCard_address_lists());

            for (int number = 0; number < length; number++) {

                String address = cardPreviewInfo.getCard_address_lists().get(number).getCard_address();

                LinearLayout list_address = (LinearLayout) newCell.findViewById(R.id.linearlayout_info_address);

                final LinearLayout newCell_address = (LinearLayout) (View.inflate(getActivity(), R.layout.cell_card_info_item, null));

                ((TextView) (newCell_address.findViewById(R.id.textView_card_info_item))).setText("");
                ((TextView) (newCell_address.findViewById(R.id.textView_card_value_item))).setText(address);

                newCell_address.setTag(number);
                registerForContextMenu(newCell_address);
                list_address.addView(newCell_address);
            }
        }


        if (cardPreviewInfo.getCard_contact_lists() != null) {

            int length = cardPreviewInfo.getCard_contact_lists().size();

            Collections.reverse(cardPreviewInfo.getCard_contact_lists());

            for (int number = 0; number < length; number++) {

                String label = cardPreviewInfo.getCard_contact_lists().get(number).getCard_contact_label();
                final String data = cardPreviewInfo.getCard_contact_lists().get(number).getCard_contact_data();

                LinearLayout list_contact = (LinearLayout) newCell.findViewById(R.id.linearlayout_info_contact);

                final LinearLayout newCell_contact = (LinearLayout) (View.inflate(getActivity(), R.layout.cell_card_info_item, null));

                ((TextView) (newCell_contact.findViewById(R.id.textView_card_info_item))).setText(label + ":  ");

                if (label.equals("Email") || label.equals("E-mail")) {

                    ((TextView) (newCell_contact.findViewById(R.id.textView_card_value_item))).setText(data);
                    ((TextView) (newCell_contact.findViewById(R.id.textView_card_value_item))).setTextColor(getResources().getColor(R.color.blue));

                    ((TextView) (newCell_contact.findViewById(R.id.textView_card_value_item))).setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub

                            Intent emailIntent = new Intent(Intent.ACTION_SEND);
                            emailIntent.setType("text/plain");
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{data});
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.str_mail_subject));
                            emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.str_mail_content));
                            getActivity().startActivity(Intent.createChooser(emailIntent, "Send mail..."));

                        }
                    });

                } else if (label.equals("Mobile") || label.equals("Phone")) {

                    ((TextView) (newCell_contact.findViewById(R.id.textView_card_value_item))).setText(data);
                    ((TextView) (newCell_contact.findViewById(R.id.textView_card_value_item))).setTextColor(getResources().getColor(R.color.black));

                    ((TextView) (newCell_contact.findViewById(R.id.textView_card_value_item))).setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub

                            String number = data.replace("+", "");
                            String phone_number = "tel:" + number.replace(" ", "");
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + phone_number));

                            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_CALL, 2);
                                return;
                            }
                            getActivity().startActivity(callIntent);

                        }
					});
					
				} else {
					
					((TextView) (newCell_contact.findViewById(R.id.textView_card_value_item))).setText(data);
					((TextView) (newCell_contact.findViewById(R.id.textView_card_value_item))).setTextColor(getResources().getColor(R.color.black));
					
				}
				
				newCell_contact.setTag(number);
				registerForContextMenu(newCell_contact);
				list_contact.addView(newCell_contact);
			}
		}
		
		
		if (cardPreviewInfo.getCard_opening_hours_lists() != null) {
			
			int length = cardPreviewInfo.getCard_opening_hours_lists().size();
			
			Collections.reverse(cardPreviewInfo.getCard_opening_hours_lists());

			for (int number = 0; number < length; number++) {
				
				String day = cardPreviewInfo.getCard_opening_hours_lists().get(number).getOpen_day();
				String time = cardPreviewInfo.getCard_opening_hours_lists().get(number).getOpen_time();
				
				LinearLayout list_opening = (LinearLayout) newCell.findViewById(R.id.linearlayout_info_opening);

				final LinearLayout newCell_opening = (LinearLayout) (View.inflate(getActivity(), R.layout.cell_card_info_item, null));
				
				((TextView) (newCell_opening.findViewById(R.id.textView_card_info_item))).setText(day + ":  ");
				((TextView) (newCell_opening.findViewById(R.id.textView_card_value_item))).setText(time);
				
				newCell_opening.setTag(number);
				registerForContextMenu(newCell_opening);
				list_opening.addView(newCell_opening);
			}
		}
		
		((TextView) (newCell.findViewById(R.id.textView_card_copyright))).setText(getString(R.string.str_card_copyright) + "  " +cardPreviewInfo.getCard_title());
		
		newCell.setTag(0);
		registerForContextMenu(newCell);
		list.addView(newCell);
		
	}
	
}

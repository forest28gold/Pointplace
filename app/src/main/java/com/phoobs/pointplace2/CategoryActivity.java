package com.phoobs.pointplace2;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.phoobs.pointplace2.models.Global;
import com.phoobs.pointplace2.models.NearbyResultsInfo;
import com.phoobs.pointplace2.models.SearchCityInfo;
import com.phoobs.pointplace2.models.SearchSubcategoryInfo;
import com.phoobs.pointplace2.utility.JsonParser;
import com.phoobs.pointplace2.utility.NetworkHelper;
import com.phoobs.pointplace2.utility.RectImageView;
import com.phoobs.pointplace2.utility.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private static final String TAG = "CategoryActivity";

    public List<Boolean> isLike_list = new ArrayList<Boolean>();

    private Toolbar toolbar;
    private SVProgressHUD mSVProgressHUD;

    RelativeLayout relativeLayout_no_result, relativeLayout_search;
    EditText edit_search;
    ImageView img_search_category_icon;
    TextView txt_search_category_name;
    TextView txt_search_subcategory_name;
    TextView txt_search_nearby_name;

    Boolean is_search;
    String menu_number, search_category, search_subcategory, search_nearby;

//    public String latitude = "36.538927";
//    public String longitude = "-4.623142";

    public String search_query = "";
    public String search_mode = Global.SEARCH_CATEGORY;

    public Dialog search_dia;
    private int scrWid;
    private int scrHei;

    private ArrayList<NearbyResultsInfo> nearbyresultslist = null;
    private ArrayList<NearbyResultsInfo> searchresultslist = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        is_search = false;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSetFavoriteResults();
                finish();
            }
        });

        DisplayMetrics mec = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mec);
        scrWid = mec.widthPixels;
        scrHei = mec.heightPixels;

        Intent intent = getIntent();
        menu_number = intent.getStringExtra("home_menu");

        relativeLayout_no_result = (RelativeLayout) findViewById(R.id.relativeLayout_no_result);
        relativeLayout_search = (RelativeLayout) findViewById(R.id.relativeLayout_search);
        relativeLayout_no_result.setVisibility(View.INVISIBLE);
        relativeLayout_search.setVisibility(View.INVISIBLE);

        edit_search = (EditText) findViewById(R.id.editText_search);
        img_search_category_icon = (ImageView) findViewById(R.id.imageView_search_category_icon);
        txt_search_category_name = (TextView) findViewById(R.id.textView_search_category_name);
        txt_search_subcategory_name = (TextView) findViewById(R.id.textView_search_subcategory);
        txt_search_nearby_name = (TextView) findViewById(R.id.textView_search_nearby);

        ((RelativeLayout) findViewById(R.id.relativeLayout_search_category)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                search_mode = Global.SEARCH_CATEGORY;
                showSearchBox(search_mode);
            }
        });

        ((RelativeLayout) findViewById(R.id.relativeLayout_search_subcategory)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                search_mode = Global.SEARCH_SUBCATEGORY;
                showSearchBox(search_mode);
            }
        });

        ((RelativeLayout) findViewById(R.id.relativeLayout_search_nearby)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                search_mode = Global.SEARCH_NEARBY;
                showSearchBox(search_mode);
            }
        });

        ((Button) findViewById(R.id.button_search_results)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                relativeLayout_search.setVisibility(View.INVISIBLE);
                is_search = false;
                removeContent();
                search_query = edit_search.getText().toString();
                search_mode = Global.SEARCH_MAIN;

                mSVProgressHUD = new SVProgressHUD(CategoryActivity.this);
                mSVProgressHUD.showWithStatus(getString(R.string.please_wait), SVProgressHUD.SVProgressHUDMaskType.Gradient);
                onGetSearchResults();
            }
        });

        mSVProgressHUD = new SVProgressHUD(this);
        mSVProgressHUD.showWithStatus(getString(R.string.please_wait), SVProgressHUD.SVProgressHUDMaskType.Gradient);
        onGetNearbyResult();
    }

    public String onGetCategoryID (String home_number) {

        String category_id = "";

        switch (home_number) {
            case "00":
                category_id = "50";		//Food & Dining
                break;
            case "11":
                category_id = "70";		//Retail Shopping
                break;
            case "12":
                category_id = "40";		//Travel & Lodging
                break;
            case "21":
                category_id = "10"; 	//Automotive
                break;
            case "22":
                category_id = "71";		//Home & Garden
                break;
            case "31":
                category_id = "30";		//Education
                break;
            case "32":
                category_id = "20";		//Computers & Electronics
                break;
            case "41":
                category_id = "92";		//Health & Beauty
                break;
            case "42":
                category_id = "81";		//Legal & Financial Services
                break;
            case "51":
                category_id = "51";		//Entertainment & Arts
                break;
            case "52":
                category_id = "91";		//Recreation & Sporting Goods
                break;
            case "61":
                category_id = "80";		//Professional Services
                break;
            case "62":
                category_id = "82";		//Real Estate
                break;
            case "71":
                category_id = "90";		//Business to Business
                break;
            case "72":
                category_id = "60";		//Government & Community
                break;
            case "81":
                category_id = "65";		//Landmarks
                break;
            default:
                break;
        }

        return category_id;
    }

    public void removeContent() {
        LinearLayout list = (LinearLayout) findViewById(R.id.linearlayout_list);
        list.removeAllViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            if (!is_search) {

                relativeLayout_search.setVisibility(View.VISIBLE);
                is_search = true;

                search_category = onGetCategoryID(menu_number);
                search_subcategory = "";
                search_nearby = Global.SEARCH_NEARBY;

                int iconImage = getMenuResources("home_icon_dark", menu_number, "mipmap");
                int menuString = getMenuResources("str_home_menu", menu_number, "string");
                img_search_category_icon.setImageResource(iconImage);
                txt_search_category_name.setText(menuString);
                txt_search_subcategory_name.setText(R.string.str_search_subcategory);
                txt_search_nearby_name.setText(R.string.str_search_nearby);

            } else {
                relativeLayout_search.setVisibility(View.INVISIBLE);
                is_search = false;
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //====================== Init NearBy Category Display ==================

    public void onGetNearbyResult() {

        String url = Global.NEARBY_URL;
        HttpGetTask nearby_task = new HttpGetTask();

        url += "/?category=" + onGetCategoryID(menu_number)
                + "&latitude=" + Global.latitude
                + "&longitude=" + Global.longitude
                + "&api_key=" + Global.API_KEY
                + "&lg=" + Global.settings_language;

//        url += "/?category=" + onGetCategoryID(menu_number)
//                + "&latitude=" + latitude
//                + "&longitude=" + longitude
//                + "&api_key=" + Global.API_KEY
//                + "&lg=" + Global.settings_language;

        Log.d("NearbyResultsURL", "URL: " + url);

        nearby_task.execute(url);

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
            return NetworkHelper.doGet(params[0], getApplicationContext());
        }

        @Override
        protected void onPostExecute(Response result) {
            super.onPostExecute(result);

            mSVProgressHUD.dismiss();

            // show the result
            if (result.getStatusCode() == Global.OK) {
                // showToast(result.getResponseText());
                String responseNearbyData = "";
                responseNearbyData = result.getResponseText();
                Log.d(TAG, "NearbyResults RESPONSE: " + responseNearbyData);
                processNearbyResponse(responseNearbyData);
            } else {
                String responseNearbyData = "";
                processNearbyResponse(responseNearbyData);
            }
        }
    }

    private void processNearbyResponse(String response) {

        removeContent();

        if (response == null || response.equals("")) {
            relativeLayout_no_result.setVisibility(View.VISIBLE);
            return;
        } else {

            JSONObject json = null;
            try {
                json = new JSONObject(response);
            } catch (JSONException e) {
                relativeLayout_no_result.setVisibility(View.VISIBLE);
                return;
            }

            nearbyresultslist = JsonParser.getNearbyResults(json);
            if (nearbyresultslist.size() == 0) {
                relativeLayout_no_result.setVisibility(View.VISIBLE);
                return;
            }
            relativeLayout_no_result.setVisibility(View.INVISIBLE);
            showResults();
            return;
        }
    }

    public void showResults() {

        appendResultHeader();

        isLike_list = new ArrayList<Boolean>();

        if (search_mode.equals(Global.SEARCH_MAIN)) {

            int length = searchresultslist.size();

            for (int number = 0; number < length; number++) {

                Boolean is_favorite = false;

                String cid = searchresultslist.get(number).getNearby_cid();
                String image = searchresultslist.get(number).getNearby_image();
                String title = searchresultslist.get(number).getNearby_title();
                String subcategory = searchresultslist.get(number).getNearby_subcategory();
                String distance = searchresultslist.get(number).getNearby_distance();

                is_favorite = onCheckFavoriteDB(menu_number, title, distance);

                if (is_favorite) {
                    isLike_list.add(true);
                } else {
                    isLike_list.add(false);
                }

                appendResultMenu(number, cid, image, title, subcategory, distance);

            }
        } else {

            int length = nearbyresultslist.size();

            for (int number = 0; number < length; number++) {

                Boolean is_favorite = false;

                String cid = nearbyresultslist.get(number).getNearby_cid();
                String image = nearbyresultslist.get(number).getNearby_image();
                String title = nearbyresultslist.get(number).getNearby_title();
                String subcategory = nearbyresultslist.get(number).getNearby_subcategory();
                String distance = nearbyresultslist.get(number).getNearby_distance();

                is_favorite = onCheckFavoriteDB(menu_number, title, distance);

                if (is_favorite) {
                    isLike_list.add(true);
                } else {
                    isLike_list.add(false);
                }

                appendResultMenu(number, cid, image, title, subcategory, distance);

            }
        }

        appendResultFooter();
    }

    public int getMenuResources(String name, String number, String type) {

        String res_name = "";
        res_name = name + number;
        int resource = getResources().getIdentifier(res_name, type, this.getPackageName());

        return resource;
    }

    public void appendResultHeader() {

        LinearLayout list = (LinearLayout) findViewById(R.id.linearlayout_list);

        final LinearLayout newCell = (LinearLayout) (View.inflate(this, R.layout.cell_menu_result_header, null));

        int iconImage = getMenuResources("home_icon_dark", menu_number, "mipmap");
        int menuString = getMenuResources("str_home_menu", menu_number, "string");

        ((ImageView) (newCell.findViewById(R.id.imageView_menu))).setImageResource(iconImage);
        ((TextView) (newCell.findViewById(R.id.textView_menu))).setText(menuString);

        newCell.setTag(0);
        registerForContextMenu(newCell);
        list.addView(newCell);

    }

    public void appendResultMenu(final int number, final String cid, final String image, final String title, final String subcategory, final String distance) {

        LinearLayout list = (LinearLayout) findViewById(R.id.linearlayout_list);

        final LinearLayout newCell = (LinearLayout) (View.inflate(this, R.layout.cell_menu_result, null));

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

        if (isLike_list.get(number)) {
            ((Button) (newCell.findViewById(R.id.button_like))).setBackgroundResource(R.mipmap.like_button_selected);
        } else {
            ((Button) (newCell.findViewById(R.id.button_like))).setBackgroundResource(R.mipmap.like_button);
        }

        ((Button) (newCell.findViewById(R.id.button_like))).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if (!isLike_list.get(number)) {
                    ((Button) (newCell.findViewById(R.id.button_like))).setBackgroundResource(R.mipmap.like_button_selected);
                    isLike_list.set(number, true);
                } else {
                    ((Button) (newCell.findViewById(R.id.button_like))).setBackgroundResource(R.mipmap.like_button);
                    isLike_list.set(number, false);
                }
            }
        });

        ((Button) (newCell.findViewById(R.id.button_share))).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent intent = new Intent(CategoryActivity.this, CardActivity.class);
                intent.putExtra("cid", cid);
                intent.putExtra("category", menu_number);
                startActivity(intent);
            }
        });

        newCell.setTag(number);
        registerForContextMenu(newCell);
        list.addView(newCell);

        newCell.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent intent = new Intent(CategoryActivity.this, CardActivity.class);
                intent.putExtra("cid", cid);
                intent.putExtra("category", menu_number);
                startActivity(intent);
            }
        });

    }

    public void appendResultFooter() {

        LinearLayout list = (LinearLayout) findViewById(R.id.linearlayout_list);

        final LinearLayout newCell = (LinearLayout) (View.inflate(this, R.layout.cell_home_menu_footer, null));

        newCell.setTag(0);
        registerForContextMenu(newCell);
        list.addView(newCell);

    }

    // ===================== Favorites DataBase ======================

    public void onSetFavoriteResults() {

        int length = isLike_list.size();

        if (search_mode.equals(Global.SEARCH_MAIN)) {

            for (int i = 0; i < length; i++) {

                String category = menu_number;
                String cid = searchresultslist.get(i).getNearby_cid();
                String image = searchresultslist.get(i).getNearby_image();
                String title = searchresultslist.get(i).getNearby_title();
                String subcategory = searchresultslist.get(i).getNearby_subcategory();
                String distance = searchresultslist.get(i).getNearby_distance();

                if (isLike_list.get(i).booleanValue()) {

                    Boolean is_favorite = onCheckFavoriteDB(category, title, distance);

                    if (!is_favorite) {
                        insertFavoriteDB(category, cid, image, title, subcategory, distance);
                    }

                } else {

                    removeFavoriteDB(category, title, distance);
                }
            }
        } else {

            for (int i = 0; i < length; i++) {

                String category = menu_number;
                String cid = nearbyresultslist.get(i).getNearby_cid();
                String image = nearbyresultslist.get(i).getNearby_image();
                String title = nearbyresultslist.get(i).getNearby_title();
                String subcategory = nearbyresultslist.get(i).getNearby_subcategory();
                String distance = nearbyresultslist.get(i).getNearby_distance();

                if (isLike_list.get(i).booleanValue()) {

                    Boolean is_favorite = onCheckFavoriteDB(category, title, distance);

                    if (!is_favorite) {
                        insertFavoriteDB(category, cid, image, title, subcategory, distance);
                    }

                } else {

                    removeFavoriteDB(category, title, distance);
                }
            }
        }
    }

    public void removeFavoriteDB(String category, String title, String distance) {

        String sql = "delete from " + Global.LOCAL_TABLE_FAVORITE
                + " where " + Global.LOCAL_FIELD_FAVORITE_CATEGORY + "=?"
                + " and " + Global.LOCAL_FIELD_FAVORITE_TITLE + "=?"
                + " and " + Global.LOCAL_FIELD_FAVORITE_DISTANCE + "=?";

        Object[] args = new Object[] { category, title, distance };
        Global.dbService.execSQL(sql, args);
    }

    @SuppressWarnings("deprecation")
    public Boolean onCheckFavoriteDB(String category, String title, String distance) {

        String sql = "select * from " + Global.LOCAL_TABLE_FAVORITE
                + " where " + Global.LOCAL_FIELD_FAVORITE_CATEGORY + "='" + category + "'"
                + " and " + Global.LOCAL_FIELD_FAVORITE_TITLE + "='" + title + "'"
                + " and " + Global.LOCAL_FIELD_FAVORITE_DISTANCE + "='" + distance + "'";

        Cursor cursor = Global.dbService.query(sql, null);
        startManagingCursor(cursor);
        if (cursor == null) {

            return false;
        } else {
            int nums = cursor.getCount();
            if (nums == 0) {
                stopManagingCursor(cursor);
                cursor.close();

                return false;
            } else {

                return true;
            }
        }
    }

    public void insertFavoriteDB(String category, String cid, String image, String title, String subcategory, String distance) {

        String sql = "insert into " + Global.LOCAL_TABLE_FAVORITE + " ("
                + Global.LOCAL_FIELD_FAVORITE_CATEGORY + ", "
                + Global.LOCAL_FIELD_FAVORITE_CID + ", "
                + Global.LOCAL_FIELD_FAVORITE_IMAGE + ", "
                + Global.LOCAL_FIELD_FAVORITE_TITLE + ", "
                + Global.LOCAL_FIELD_FAVORITE_SUBCATEGORY + ", "
                + Global.LOCAL_FIELD_FAVORITE_DISTANCE
                + ") values(?,?,?,?,?,?)";
        Object[] args = new Object[] { category, cid, image, title, subcategory, distance };
        Global.dbService.execSQL(sql, args);
    }

    //======================  Show Search Category Dialog ==================================

    public void removeSearchContent() {
        LinearLayout list = (LinearLayout) search_dia.findViewById(R.id.linearLayout_search_subject_list);
        list.removeAllViews();
    }

    public void showSearchBox(String subject) {

        search_dia = new Dialog(this, R.style.CustomTheme);
        search_dia.setContentView(R.layout.cell_search_box);
        Window drawWin = search_dia.getWindow();
        WindowManager.LayoutParams diaParam = drawWin.getAttributes();
        diaParam.gravity = Gravity.CENTER;
        drawWin.setAttributes(diaParam);
        search_dia.getWindow().setLayout(scrWid, scrHei / 10 * 25 / 3);
        search_dia.getWindow().getAttributes().windowAnimations = R.animator.slide_up;

        ((RelativeLayout) (search_dia.findViewById(R.id.relativeLayout_no_search))).setVisibility(View.INVISIBLE);

        if (subject.equals(Global.SEARCH_CATEGORY)) {
            ((TextView) (search_dia.findViewById(R.id.textView_search_subject))).setText(R.string.str_search_category);
            removeSearchContent();
            onGetSearchCategories();
        } else if (subject.equals(Global.SEARCH_SUBCATEGORY)) {
            ((TextView) (search_dia.findViewById(R.id.textView_search_subject))).setText(R.string.str_search_subcategory);
            if (Global.searchsubcategorylist != null && Global.searchsubcategorylist.size() > 0) {
                removeSearchContent();
                ((RelativeLayout) (search_dia.findViewById(R.id.relativeLayout_no_search))).setVisibility(View.INVISIBLE);
                onShowSearchSubcategories();
            } else {
                onGetSearchSubcategories();
            }
        } else if (subject.equals(Global.SEARCH_NEARBY)) {
            ((TextView) (search_dia.findViewById(R.id.textView_search_subject))).setText(R.string.str_search_city);
            if (Global.searchcitylist != null && Global.searchcitylist.size() > 0) {
                removeSearchContent();
                ((RelativeLayout) (search_dia.findViewById(R.id.relativeLayout_no_search))).setVisibility(View.INVISIBLE);
                onShowSearchCitylist();
            } else {
                onGetSearchCitylists();
            }
        }
        search_dia.show();
    }

    //------------------ Search Category Show ----------------------------

    public void onGetSearchCategories() {

        int length = 8;
        for (int number = 0; number <= length; number++) {
            appendSearchCategories(number);
        }
    }

    public int getSearchMenuResources(String name, int number, int i, String type) {

        String res_name = "";
        res_name = name + String.valueOf(number) + String.valueOf(i);
        int resource = getResources().getIdentifier(res_name, type, this.getPackageName());

        return resource;
    }

    public void appendSearchCategories(final int number) {

        LinearLayout list = (LinearLayout) search_dia.findViewById(R.id.linearLayout_search_subject_list);

        final LinearLayout newCell = (LinearLayout) (View.inflate(this, R.layout.cell_search_category_item, null));

        if (number == 0) {

            final int iconImage = getSearchMenuResources("home_icon_dark", number, 0, "mipmap");
            final int menuString = getSearchMenuResources("str_home_menu", number, 0, "string");

            ((ImageView) (newCell.findViewById(R.id.imageView_search_category_icon))).setImageResource(iconImage);
            ((TextView) (newCell.findViewById(R.id.textView_search_category_name))).setText(menuString);

            ((RelativeLayout) (newCell.findViewById(R.id.relativeLayout_search_category))).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    menu_number = "00";
                    search_category = onGetCategoryID(menu_number);
                    search_dia.dismiss();

                    img_search_category_icon.setImageResource(iconImage);
                    txt_search_category_name.setText(menuString);
                }
            });

            newCell.setTag(number);
            registerForContextMenu(newCell);
            list.addView(newCell);

        } else if (number == 8) {

            final int iconImage = getSearchMenuResources("home_icon_dark", number, 1, "mipmap");
            final int menuString = getSearchMenuResources("str_home_menu", number, 1, "string");

            ((ImageView) (newCell.findViewById(R.id.imageView_search_category_icon))).setImageResource(iconImage);
            ((TextView) (newCell.findViewById(R.id.textView_search_category_name))).setText(menuString);

            ((RelativeLayout) (newCell.findViewById(R.id.relativeLayout_search_category))).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    menu_number = "81";
                    search_category = onGetCategoryID(menu_number);
                    search_dia.dismiss();

                    img_search_category_icon.setImageResource(iconImage);
                    txt_search_category_name.setText(menuString);
                }
            });

            newCell.setTag(number);
            registerForContextMenu(newCell);
            list.addView(newCell);

        } else {

            final int iconImage1 = getSearchMenuResources("home_icon_dark", number, 1, "mipmap");
            final int menuString1 = getSearchMenuResources("str_home_menu", number, 1, "string");

            ((ImageView) (newCell.findViewById(R.id.imageView_search_category_icon))).setImageResource(iconImage1);
            ((TextView) (newCell.findViewById(R.id.textView_search_category_name))).setText(menuString1);

            ((RelativeLayout) (newCell.findViewById(R.id.relativeLayout_search_category))).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    menu_number = String.valueOf(number) + "1";
                    search_category = onGetCategoryID(menu_number);
                    search_dia.dismiss();

                    img_search_category_icon.setImageResource(iconImage1);
                    txt_search_category_name.setText(menuString1);
                }
            });

            final LinearLayout newCell2 = (LinearLayout) (View.inflate(this, R.layout.cell_search_category_item, null));

            final int iconImage2 = getSearchMenuResources("home_icon_dark", number, 2, "mipmap");
            final int menuString2 = getSearchMenuResources("str_home_menu", number, 2, "string");

            ((ImageView) (newCell2.findViewById(R.id.imageView_search_category_icon))).setImageResource(iconImage2);
            ((TextView) (newCell2.findViewById(R.id.textView_search_category_name))).setText(menuString2);

            ((RelativeLayout) (newCell2.findViewById(R.id.relativeLayout_search_category))).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    menu_number = String.valueOf(number) + "2";
                    search_category = onGetCategoryID(menu_number);
                    search_dia.dismiss();

                    img_search_category_icon.setImageResource(iconImage2);
                    txt_search_category_name.setText(menuString2);
                }
            });

            newCell.setTag(number);
            registerForContextMenu(newCell);
            list.addView(newCell);

            newCell2.setTag(number);
            registerForContextMenu(newCell2);
            list.addView(newCell2);
        }

    }

    //------------------ Search Subcategory and City Show ----------------------------

    public void onGetSearchSubcategories() {

        String url = Global.SUBCATEGORY_URL;
        HttpGetSearchTask search_subcategories_task = new HttpGetSearchTask();

        url += "/?category=" + search_category
                + "&api_key=" + Global.API_KEY
                + "&lg=" + Global.settings_language;

        Log.d(TAG, "SubcategoriesURL :  " + url);

        search_subcategories_task.execute(url);
    }

    public void onGetSearchCitylists() {

        String url = Global.CITYLIST_URL;
        HttpGetSearchTask search_nearby_task = new HttpGetSearchTask();

        url += "/?api_key=" + Global.API_KEY
                + "&lg=" + Global.settings_language;

        Log.d(TAG, "CitylistsURL :  " + url);

        search_nearby_task.execute(url);
    }

    @SuppressWarnings("rawtypes")
    public class SortBasedOnSubcategoryName implements Comparator {

        public int compare(Object o1, Object o2) {

            SearchSubcategoryInfo subcategory1 = (SearchSubcategoryInfo) o1;// where FBFriends_Obj is
            // your object class
            SearchSubcategoryInfo subcategory2 = (SearchSubcategoryInfo) o2;

            return subcategory1.getCard_subcategory().compareToIgnoreCase(subcategory2.getCard_subcategory());// where uname is  field name
        }

    }

    @SuppressWarnings("unchecked")
    public void onShowSearchSubcategories() {

        appendSearchAllSubcategory();

        int length = Global.searchsubcategorylist.size();

        Collections.sort(Global.searchsubcategorylist, new SortBasedOnSubcategoryName());

        for (int number = 0; number < length; number++) {

            String subcategory = Global.searchsubcategorylist.get(number).getCard_subcategory();

            appendSearchSubcategories(number, subcategory);
        }
    }

    public void appendSearchAllSubcategory() {

        LinearLayout list = (LinearLayout) search_dia.findViewById(R.id.linearLayout_search_subject_list);

        final LinearLayout newCell = (LinearLayout) (View.inflate(this, R.layout.cell_search_subcategory_item, null));

        ((TextView) (newCell.findViewById(R.id.textView_search_subcategory))).setText(R.string.str_search_all_subcategory);

        ((RelativeLayout) (newCell.findViewById(R.id.relativeLayout_search_subcategory))).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                search_subcategory = "";
                search_dia.dismiss();

                txt_search_subcategory_name.setText(R.string.str_search_all_subcategory);
            }
        });

        newCell.setTag(0);
        registerForContextMenu(newCell);
        list.addView(newCell);

    }

    public void appendSearchSubcategories(final int number, final String subcategory) {

        LinearLayout list = (LinearLayout) search_dia.findViewById(R.id.linearLayout_search_subject_list);

        final LinearLayout newCell = (LinearLayout) (View.inflate(this, R.layout.cell_search_subcategory_item, null));

        ((TextView) (newCell.findViewById(R.id.textView_search_subcategory))).setText(subcategory);

        ((RelativeLayout) (newCell.findViewById(R.id.relativeLayout_search_subcategory))).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                search_subcategory = subcategory;
                search_dia.dismiss();

                txt_search_subcategory_name.setText(subcategory);
            }
        });

        newCell.setTag(number);
        registerForContextMenu(newCell);
        list.addView(newCell);

    }

    @SuppressWarnings("rawtypes")
    public class SortBasedOnCityName implements Comparator {

        public int compare(Object o1, Object o2) {

            SearchCityInfo city1 = (SearchCityInfo) o1;// where FBFriends_Obj is
            // your object class
            SearchCityInfo city2 = (SearchCityInfo) o2;

            return city1.getCard_city().compareToIgnoreCase(city2.getCard_city());// where uname is  field name
        }

    }

    @SuppressWarnings("unchecked")
    public void onShowSearchCitylist() {

        int length = Global.searchcitylist.size();

        Collections.sort(Global.searchcitylist, new SortBasedOnCityName());

        appendSearchNearby();

        for (int number = 0; number < length; number++) {

            String city = Global.searchcitylist.get(number).getCard_city();
            appendSearchCitylist(number, city);
        }
    }

    public void appendSearchNearby() {

        LinearLayout list = (LinearLayout) search_dia.findViewById(R.id.linearLayout_search_subject_list);

        final LinearLayout newCell = (LinearLayout) (View.inflate(this, R.layout.cell_search_subcategory_item, null));

        ((TextView) (newCell.findViewById(R.id.textView_search_subcategory))).setText(Global.SEARCH_NEARBY);

        ((RelativeLayout) (newCell.findViewById(R.id.relativeLayout_search_subcategory))).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                search_nearby = Global.SEARCH_NEARBY;
                search_dia.dismiss();

                txt_search_nearby_name.setText(Global.SEARCH_NEARBY);
            }
        });

        newCell.setTag(0);
        registerForContextMenu(newCell);
        list.addView(newCell);

    }

    public void appendSearchCitylist(final int number, final String city) {

        LinearLayout list = (LinearLayout) search_dia.findViewById(R.id.linearLayout_search_subject_list);

        final LinearLayout newCell = (LinearLayout) (View.inflate(this, R.layout.cell_search_subcategory_item, null));

        ((TextView) (newCell.findViewById(R.id.textView_search_subcategory))).setText(city);

        ((RelativeLayout) (newCell.findViewById(R.id.relativeLayout_search_subcategory))).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                search_nearby = city;
                search_dia.dismiss();

                txt_search_nearby_name.setText(city);
            }
        });

        newCell.setTag(number);
        registerForContextMenu(newCell);
        list.addView(newCell);

    }

    // ===================== Search ===================================

    public void onGetSearchResults() {

        String url = Global.SEARCH_URL;
        HttpGetSearchTask search_main_task = new HttpGetSearchTask();

        search_query = search_query.replace(" ", "%20");
        search_nearby = search_nearby.replace(" ", "%20");
        search_subcategory = search_subcategory.replace(" ", "%20");

        url += "/?category=" + search_category
                + "&latitude=" + Global.latitude
                + "&longitude=" + Global.longitude
                + "&query=" + search_query
                + "&location=" + search_nearby
                + "&subcategory=" + search_subcategory
                + "&api_key=" + Global.API_KEY
                + "&lg=" + Global.settings_language;

//        url += "/?category=" + search_category
//                + "&latitude=" + latitude
//                + "&longitude=" + longitude
//                + "&query=" + search_query
//                + "&location=" + search_nearby
//                + "&subcategory=" + search_subcategory
//                + "&api_key=" + Global.API_KEY
//                + "&lg=" + Global.settings_language;

        Log.d(TAG, "SearchResults URL: " + url);

        search_main_task.execute(url);
    }

    private void processSearchModeResponse(String response) {

        if (response == null || response.equals("")) {

            if (search_mode.equals(Global.SEARCH_SUBCATEGORY)) {
                removeSearchContent();
                ((RelativeLayout) (search_dia.findViewById(R.id.relativeLayout_no_search))).setVisibility(View.VISIBLE);
                return;
            } else if (search_mode.equals(Global.SEARCH_NEARBY)) {
                removeSearchContent();
                ((RelativeLayout) (search_dia.findViewById(R.id.relativeLayout_no_search))).setVisibility(View.VISIBLE);
                return;
            } else if (search_mode.equals(Global.SEARCH_MAIN)) {
                removeContent();
                relativeLayout_no_result.setVisibility(View.VISIBLE);
                return;
            }
        } else {

            JSONObject json = null;
            try {
                json = new JSONObject(response);
            } catch (JSONException e) {
                if (search_mode.equals(Global.SEARCH_SUBCATEGORY)) {
                    removeSearchContent();
                    ((RelativeLayout) (search_dia.findViewById(R.id.relativeLayout_no_search))).setVisibility(View.VISIBLE);
                    return;
                } else if (search_mode.equals(Global.SEARCH_NEARBY)) {
                    removeSearchContent();
                    ((RelativeLayout) (search_dia.findViewById(R.id.relativeLayout_no_search))).setVisibility(View.VISIBLE);
                    return;
                } else if (search_mode.equals(Global.SEARCH_MAIN)) {
                    removeContent();
                    relativeLayout_no_result.setVisibility(View.VISIBLE);
                    return;
                }
                return;
            }

            if (search_mode.equals(Global.SEARCH_SUBCATEGORY)) {
                removeSearchContent();
                Global.searchsubcategorylist = JsonParser.getSearchSubcategory(json);
                if (Global.searchsubcategorylist.size() == 0) {
                    ((RelativeLayout) (search_dia.findViewById(R.id.relativeLayout_no_search))).setVisibility(View.VISIBLE);
                    return;
                }
                ((RelativeLayout) (search_dia.findViewById(R.id.relativeLayout_no_search))).setVisibility(View.INVISIBLE);
                onShowSearchSubcategories();

            } else if (search_mode.equals(Global.SEARCH_NEARBY)) {
                removeSearchContent();
                Global.searchcitylist = JsonParser.getSearchCity(json);
                if (Global.searchcitylist.size() == 0) {
                    ((RelativeLayout) (search_dia.findViewById(R.id.relativeLayout_no_search))).setVisibility(View.VISIBLE);
                    return;
                }
                ((RelativeLayout) (search_dia.findViewById(R.id.relativeLayout_no_search))).setVisibility(View.INVISIBLE);
                onShowSearchCitylist();

            } else if (search_mode.equals(Global.SEARCH_MAIN)) {
                removeContent();
                searchresultslist = JsonParser.getNearbyResults(json);
                if (searchresultslist.size() == 0) {
                    relativeLayout_no_result.setVisibility(View.VISIBLE);
                    return;
                }
                relativeLayout_no_result.setVisibility(View.INVISIBLE);
                showResults();
            }

            return;
        }
    }

    public class HttpGetSearchTask extends AsyncTask<String, Void, Response> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a ProgressDialog with message
        }

        @Override
        protected Response doInBackground(String... params) {
            // call method to initiate HTTP request

            return NetworkHelper.doGet(params[0], getApplicationContext());
        }

        @Override
        protected void onPostExecute(Response result) {
            super.onPostExecute(result);

            mSVProgressHUD.dismiss();
            // show the result
            if (result.getStatusCode() == Global.OK) {
                // showToast(result.getResponseText());
                String responseSearchModeData = "";
                responseSearchModeData = result.getResponseText();
                Log.d(TAG, "SearchMode RESPONSE: " + responseSearchModeData);
                processSearchModeResponse(responseSearchModeData);
            } else {
                String responseSearchModeData = "";
                processSearchModeResponse(responseSearchModeData);
            }
        }
    }

    @Override
    public void onBackPressed() {
        onSetFavoriteResults();
        finish();
    }

}

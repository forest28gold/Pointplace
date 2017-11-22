package com.phoobs.pointplace2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.phoobs.pointplace2.models.Global;


@SuppressLint("NewApi") public class CardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_card);

        Global.is_card_site = false;

        Global.fragmentManager = getSupportFragmentManager();

        Intent intent = getIntent();
        Global.card_cid = intent.getStringExtra("cid");
        Global.category_number = intent.getStringExtra("category");

        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new FragmentCardPreview())
                    .commit();
        } else {
            Global.is_card_site = (getFragmentManager().getBackStackEntryCount() > 0);
        }
    }
    
    @Override
	public void onBackPressed() {
    	
    	if (Global.is_card_site) {
    		Global.is_card_site = false;
    		getFragmentManager().popBackStack();
		} else {
			finish();
		}
	}
    
}

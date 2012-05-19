/*
Copyright (c) 2012, Phillip Thelen
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met: 

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer. 
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package net.pherth.omnomagon;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.internal.nineoldandroids.animation.ObjectAnimator;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.foound.widget.AmazingListView;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitleProvider;

public class MainActivity extends SherlockActivity {

	private Context cxt;
    private ProgressDialog m_ProgressDialog = null;
    private Runnable loadNew;
    private String[] items;
	private ArrayList<MealAdapter> mAdapterList = new ArrayList<MealAdapter>();
    com.actionbarsherlock.app.ActionBar actionBar;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        cxt = this;

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(cxt);

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar));
        actionBar.setDisplayShowHomeEnabled(false);
	    actionBar.setTitle(getCurrentMensa(sharedPrefs.getString("mensaPreference", "Mensa")));
	    actionBar.setSubtitle("07.05.2012");

        items = getResources().getStringArray(R.array.weekDays);

        Calendar calendar = Calendar.getInstance();
        Log.i("Calendar", calendar.toString());
        calendar.setFirstDayOfWeek(0);
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 2;

        OnSharedPreferenceChangeListener prefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            			actionBar.setTitle(getCurrentMensa(prefs.getString("mensaPreference", "Mensa")));
            			for(int i=0; i<5; i++){
            	    		mAdapterList.get(i).notifyDataSetChanged();
            	    	}
                }
            };
        sharedPrefs.registerOnSharedPreferenceChangeListener(prefsListener);
        for(int x=0; x<5; x++) {
        	mAdapterList.add(new MealAdapter(cxt));
        }
	    MainPagerAdapter adapter = new MainPagerAdapter(this);
        ViewPager pager = (ViewPager)findViewById( R.id.mainpager );
        TitlePageIndicator indicator = (TitlePageIndicator)findViewById( R.id.indicator );
        indicator.bringToFront();
        pager.setAdapter(adapter);
        indicator.setViewPager( pager );
        Log.i("Day", String.valueOf(day));
        if (day < 5) {
        	pager.setCurrentItem(day);
        }
        loadNew = new Runnable(){
            @Override
            public void run() {
                getPlan(false);
            }
        };

	    Long lastUpdate = sharedPrefs.getLong("lastUpdate", -1);
	    if (sharedPrefs.getBoolean("automaticUpdate", true) && !updateToday(lastUpdate)) {
	        Thread thread =  new Thread(null, loadNew, "MagentoBackground");
	        thread.start();
	        m_ProgressDialog = ProgressDialog.show(MainActivity.this,
	              getString( R.string.pleaseWait), getString(R.string.retrvData), true);
	    } else {
	    	Runnable loadDatabase = new Runnable(){
	            @Override
	            public void run() {
	                getPlan(true);

	            }
	        };
	        Thread thread =  new Thread(null, loadDatabase, "MagentoBackground");
	        thread.start();
	        m_ProgressDialog = ProgressDialog.show(MainActivity.this,
	              getString( R.string.pleaseWait), getString(R.string.retrvData), true);
	    }
    }

    private class MainPagerAdapter extends PagerAdapter implements TitleProvider{

    	private final Context context;

    	public MainPagerAdapter( Context context )
        {
            this.context = context;
        }

        @Override
        public int getCount() {
	        return 5;
        }
        @Override
        public Object instantiateItem(View collection, int position) {
        	AmazingListView v = new AmazingListView( context );
	        ImageView iv = new ImageView( context );
	        ((ViewPager) collection).addView(v, 0);
	        v.addFooterView(iv);
	        iv.setImageResource(R.drawable.footer);
            v.setAdapter(mAdapterList.get(position));
            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.background));
            v.setDivider(getResources().getDrawable(android.R.color.transparent));
            v.setCacheColorHint(0x00000000);
	        v.setSelector(android.R.color.transparent);
            v.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View res,
						int arg2, long arg3) {
					TextView textView = (TextView) res.findViewById(R.id.additionsTextView);
					if (textView.getVisibility() == View.GONE) {
						textView.setVisibility(View.VISIBLE);
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.rolldown);
						textView.setAnimation(anim);
						anim.start();
					} else {
						Animation anim = AnimationUtils.loadAnimation(context, R.anim.rollup);
						textView.setAnimation(anim);
						anim.start();
						textView.setVisibility(View.GONE);
					}
				}
              });

            return v;
        }

        @Override
        public void destroyItem(View collection, int position, Object view) {
                ((ViewPager) collection).removeView((ListView) view);
        }



        @Override
        public boolean isViewFromObject(View view, Object object) {
                return view==((ListView)object);
        }

        @Override
        public void finishUpdate(View arg0) {
        }


        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {}

        @Override
        public Parcelable saveState() {
                return null;
        }

        @Override
        public void startUpdate(View arg0) {}

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public String getTitle( int position )
        {
            return items[ position ];
        }
    }

    private void getPlan(boolean fromDatabase){
    	if (isNetworkAvailable()) {
		    Data data = new Data(cxt);
    	data.getAllData(fromDatabase);
    	for(int i=0; i< data.getDayCount(); i++){
    		MealAdapter mealAdapter = mAdapterList.get(i);
    		mealAdapter.setData(data.getCurrentDay(i));
    		mAdapterList.set(i, mealAdapter);
    	}
    	}
        runOnUiThread(returnRes);
    }

    private Runnable returnRes = new Runnable() {

        @Override
        public void run() {
            m_ProgressDialog.dismiss();
            for(int i=0; i<5; i++){
	    		mAdapterList.get(i).notifyDataSetChanged();
	    	}
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.optionmenu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Log.i("Item", item.toString());
        switch (item.getItemId()) {
            case R.id.menu_settings:
            	Intent settingsActivity = new Intent(getBaseContext(),
                        MainPreference.class);
            		startActivityForResult(settingsActivity, 0);
                   	break;
            case R.id.menu_refresh:
            	Thread thread =  new Thread(null, loadNew, "MagentoBackground");
                thread.start();
                m_ProgressDialog = ProgressDialog.show(MainActivity.this,
                		getString( R.string.pleaseWait), getString(R.string.retrvData), true);
                break;

        }
        return true;
    }

    public String getCurrentMensa(String key) {
    	String currentMensa = "Omnomagon";
        Resources res = getResources();
        String[] mensaKeys = res.getStringArray(R.array.beListValues);
        int mensaIndex = java.util.Arrays.asList(mensaKeys).indexOf(key);
        if (mensaIndex >= 0) {
        	currentMensa = res.getStringArray(R.array.beList)[mensaIndex];
        }
        return currentMensa;
    }

    @Override
    public void onPause() {
    	if(m_ProgressDialog != null) {
    		m_ProgressDialog.dismiss();
    	}
    	super.onPause();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
              = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static final void setAppFont(ViewGroup mContainer, Typeface mFont)
    {
        if (mContainer == null || mFont == null) return;

        final int mCount = mContainer.getChildCount();

        // Loop through all of the children.
        for (int i = 0; i < mCount; ++i)
        {
            final View mChild = mContainer.getChildAt(i);
            if (mChild instanceof TextView)
            {
                // Set the font if it is a TextView.
                ((TextView) mChild).setTypeface(mFont);
            }
            else if (mChild instanceof ViewGroup)
            {
                // Recursively attempt another ViewGroup.
                setAppFont((ViewGroup) mChild, mFont);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
    }

	
	public Boolean updateToday(Long timestamp) {
		Long currTimestamp = System.currentTimeMillis();
		Float diff = (currTimestamp.floatValue() - timestamp) / 86400000;
		Log.i("MainActivity", diff.toString());
		return (diff < 1);
		
	}
}


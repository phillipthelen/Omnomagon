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

package net.pherth.mensa;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.foound.widget.AmazingAdapter;

public class MealAdapter extends AmazingAdapter {
	List<Pair<Integer, List<Meal>>> all = new ArrayList<Pair<Integer, List<Meal>>>();
	private Context context;
	
	public MealAdapter(Context context, List<Pair<Integer, List<Meal>>> data){
		this.context = context;
		if (data == null){
			this.all = data;
		}
		
	}
	
	public MealAdapter(Context context) {
		this.context = context;
	}
	
	public void setData(List<Pair<Integer, List<Meal>>> data) {
		this.all = data;
	}
	
	@Override
	public int getCount() {
		int res = 0;
		for (int i = 0; i < all.size(); i++) {
			res += all.get(i).second.size();
		}
		return res;
	}

	@Override
	public Meal getItem(int position) {
		int c = 0;
		for (int i = 0; i < all.size(); i++) {
			if (position >= c && position < c + all.get(i).second.size()) {
				return all.get(i).second.get(position - c);
			}
			c += all.get(i).second.size();
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	protected void onNextPageRequested(int page) {
	}

	@Override
	protected void bindSectionHeader(View view, int position, boolean displaySectionHeader) {
		if (displaySectionHeader) {
			view.findViewById(R.id.header).setVisibility(View.VISIBLE);
			ImageView lSectionTitle = (ImageView) view.findViewById(R.id.header);
			int section = getSections()[getSectionForPosition(position)];
			lSectionTitle.setImageResource(section);
			view.findViewById(R.id.listTextContainer).setBackgroundResource(R.drawable.schatten);
		} else {
			view.findViewById(R.id.header).setVisibility(View.GONE);
			view.findViewById(R.id.listTextContainer).setBackgroundResource(android.R.color.transparent);
		}
	}

	@Override
	public View getAmazingView(int position, View convertView, ViewGroup parent) {
		View res = convertView;
		if (res == null) {
			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			res = vi.inflate(R.layout.list_item, null);
		}
		
		TextView itemBig = (TextView) res.findViewById(R.id.itemViewBig);
		TextView itemPrice = (TextView) res.findViewById(R.id.itemViewPrice);
		ImageView bioImageView = (ImageView) res.findViewById(R.id.bioImageView);
		ImageView veganVegetarianImageView = (ImageView) res.findViewById(R.id.veganVegetarianImageView);
		ListView additionsListView = (ListView) res.findViewById(R.id.additionsListView);
		
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.context);
		Meal meal = getItem(position);
		itemBig.setText(meal.getName());
		itemPrice.setText(meal.getCorrectPriceString(Integer.parseInt(sharedPrefs.getString("priceCategory", "2"))));
		if (sharedPrefs.getBoolean("bioCheckbox", false) && meal.getBio()) {
			bioImageView.setVisibility(View.VISIBLE);
		} else {
			bioImageView.setVisibility(View.INVISIBLE);
		}
		Integer m = meal.getVeganterianMsc(this.context);
		if (m != 0) {
			veganVegetarianImageView.setVisibility(View.VISIBLE);
			veganVegetarianImageView.setImageResource(m);
		} else {
			veganVegetarianImageView.setVisibility(View.INVISIBLE);
		}
		
		res.findViewById(R.id.allergyImageView).setVisibility(View.GONE);
		List<String> additionList = meal.getAdditions();
		if (!additionList.isEmpty()) {
			AdditionAdapter additionAdapter = new AdditionAdapter(context, R.layout.addition_list_item, additionList);
			additionsListView.setAdapter(additionAdapter);
		}
		additionsListView.setVisibility(View.GONE);
		Util.setListViewHeightBasedOnChildren(additionsListView);
		return res;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
	}

	@Override
	public int getPositionForSection(int section) {
		if (section < 0) section = 0;
		if (section >= all.size()) section = all.size() - 1;
		int c = 0;
		for (int i = 0; i < all.size(); i++) {
			if (section == i) { 
				return c;
			}
			c += all.get(i).second.size();
		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		int c = 0;
		for (int i = 0; i < all.size(); i++) {
			if (position >= c && position < c + all.get(i).second.size()) {
				return i;
			}
			c += all.get(i).second.size();
		}
		return -1;
	}

	@Override
	public Integer[] getSections() {
		Integer[] res = new Integer[all.size()];
		for (int i = 0; i < all.size(); i++) {
			res[i] = all.get(i).first;
		}
		return res;
	}
}

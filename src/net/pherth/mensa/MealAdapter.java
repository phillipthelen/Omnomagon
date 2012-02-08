package net.pherth.mensa;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foound.widget.AmazingAdapter;

public class MealAdapter extends AmazingAdapter {
	List<Pair<String, List<Meal>>> all = new ArrayList<Pair<String, List<Meal>>>();
	private Context context;
	
	public MealAdapter(Context context, List<Pair<String, List<Meal>>> data){
		this.context = context;
		if (data == null){
			this.all = data;
		}
		
	}
	
	public MealAdapter(Context context) {
		this.context = context;
	}
	
	public void setData(List<Pair<String, List<Meal>>> data) {
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
			view.findViewById(R.id.headerBar).setVisibility(View.VISIBLE);
			TextView lSectionTitle = (TextView) view.findViewById(R.id.header);
			lSectionTitle.setText(getSections()[getSectionForPosition(position)]);
		} else {
			view.findViewById(R.id.header).setVisibility(View.GONE);
			view.findViewById(R.id.headerBar).setVisibility(View.GONE);
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
		TextView itemSmall = (TextView) res.findViewById(R.id.itemViewSmall);
		TextView itemPrice = (TextView) res.findViewById(R.id.itemViewPrice);
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.context);
		Meal meal = getItem(position);
		itemBig.setText(meal.getName());
		if (meal.getDescription() == "") {
			itemSmall.setText(meal.getDescription());
		} else {
			itemSmall.setTextSize((float) 5.0);
		}
		itemPrice.setText(meal.getCorrectPriceString(Integer.parseInt(sharedPrefs.getString("priceCategory", "2"))));
		
		return res;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		TextView lSectionHeader = (TextView)header.findViewById(R.id.header);
		header.findViewById(R.id.headerBar).setVisibility(View.GONE);
		lSectionHeader.setText(getSections()[getSectionForPosition(position)]);
		lSectionHeader.setBackgroundColor(alpha << 24 | (0x000000));
		lSectionHeader.setTextColor(alpha << 24 | (0xFFFFFF));
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
	public String[] getSections() {
		String[] res = new String[all.size()];
		for (int i = 0; i < all.size(); i++) {
			res[i] = all.get(i).first;
		}
		return res;
	}
}

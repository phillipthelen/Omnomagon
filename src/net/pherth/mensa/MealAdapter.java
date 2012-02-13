package net.pherth.mensa;

import java.util.ArrayList;
import java.util.List;

import net.pherth.mensa.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
			ImageView lSectionTitle = (ImageView) view.findViewById(R.id.header);
			String section = getSections()[getSectionForPosition(position)];
			int drawable;
			if (section.equals("Aktionsstand")) {
				drawable = R.drawable.aktion;
			} else if (section.equals("Beilagen")) {
				drawable = R.drawable.beilagen;
			} else if (section.equals("Desserts")) {
				drawable = R.drawable.desserts;
			} else if (section.equals("Essen")) {
				drawable = R.drawable.essen;
			} else if (section.equals("Salate")) {
				drawable = R.drawable.salate;
			} else if (section.equals("Suppen")) {
				drawable = R.drawable.suppen;
			} else if (section.equals("Vorspeisen")) {
				drawable = R.drawable.vorspeisen;
			} else {
				drawable = R.drawable.essen;
			}
			System.out.println(drawable);
			lSectionTitle.setImageResource(drawable);
		} else {
			view.findViewById(R.id.header).setVisibility(View.GONE);
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
		if (meal.getBio()) {
			bioImageView.setVisibility(View.VISIBLE);
		} else {
			bioImageView.setVisibility(View.GONE);
		}
		if (meal.getVeganterianMsc() != 0) {
			veganVegetarianImageView.setVisibility(View.VISIBLE);
			veganVegetarianImageView.setImageResource(meal.getVeganterianMsc());
		} else {
			veganVegetarianImageView.setVisibility(View.GONE);
		}
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
	public String[] getSections() {
		String[] res = new String[all.size()];
		for (int i = 0; i < all.size(); i++) {
			res[i] = all.get(i).first;
		}
		return res;
	}
}

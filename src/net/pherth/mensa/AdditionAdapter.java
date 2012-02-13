package net.pherth.mensa;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

public class AdditionAdapter extends ArrayAdapter<String> {

	public AdditionAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}
	
	public AdditionAdapter(Context context, int textViewResourceId, List<String> list) {
		super(context, textViewResourceId, list);
	}
	
	@Override
	public boolean isEnabled(int position) {
		return false;
	}

}

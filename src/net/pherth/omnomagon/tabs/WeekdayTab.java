package net.pherth.omnomagon.tabs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.pherth.omnomagon.R;
import net.pherth.omnomagon.data.Day;
import net.pherth.omnomagon.data.PriceGroup;

public class WeekdayTab extends Fragment {

    private ViewHolder _viewHolder;
    private WeekdayMealAdapter _weekdayMealAdapter;

    public WeekdayTab() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.menu_overview_weekday_fragment, container, false);
        _viewHolder = new ViewHolder(rootView);
        final WeekdayMealAdapter weekdayMealAdapter = getWeekdayMealAdapter();
        //todo show some hint on empty list
        _viewHolder.setAdapter(weekdayMealAdapter);
        return rootView;
    }

    @NonNull
    private WeekdayMealAdapter getWeekdayMealAdapter() {
        if (_weekdayMealAdapter == null) {
            _weekdayMealAdapter = new WeekdayMealAdapter();
        }
        return _weekdayMealAdapter;
    }

    public void setData(@NonNull Day day, @NonNull PriceGroup priceGroup) {
        final WeekdayMealAdapter weekdayMealAdapter = getWeekdayMealAdapter();
        weekdayMealAdapter.setData(day, priceGroup);
        if (_viewHolder != null) {
            _viewHolder.setAdapter(weekdayMealAdapter);
        }
    }

    @Override
    public void onDestroyView() {
        _viewHolder = null;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        _weekdayMealAdapter = null;
        super.onDestroy();
    }

    private static class ViewHolder {

        private final RecyclerView _listView;

        public ViewHolder(@NonNull View rootView) {
            _listView = (RecyclerView) rootView.findViewById(R.id.menu_overview_weekday_fragment_list);
            final Context context = rootView.getContext();
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            _listView.setLayoutManager(linearLayoutManager);
            final DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
            _listView.setItemAnimator(defaultItemAnimator);
            //todo add dividers
        }

        public void setAdapter(@NonNull WeekdayMealAdapter weekdayMealAdapter) {
            _listView.setAdapter(weekdayMealAdapter);
        }
    }
}

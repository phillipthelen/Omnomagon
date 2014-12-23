package net.pherth.omnomagon.tabs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.pherth.omnomagon.R;
import net.pherth.omnomagon.data.Day;
import net.pherth.omnomagon.data.Meal;
import net.pherth.omnomagon.data.MealGroup;
import net.pherth.omnomagon.data.PriceGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WeekdayMealAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_MEAL_GROUP = 0;
    private static final int VIEW_TYPE_MEAL = 1;

    private final Map<Integer,Integer> _typeMap = new HashMap<Integer, Integer>();
    private final List<Object> _dataObjects = new ArrayList<Object>();
    private PriceGroup _priceGroup = PriceGroup.guests;
    private WeekdayMealViewHolder.IndicatorConfiguration _indicatorConfiguration = WeekdayMealViewHolder.IndicatorConfiguration.defaultConfiguration();

    public void setData(@NonNull Day day, @NonNull PriceGroup priceGroup, @NonNull WeekdayMealViewHolder.IndicatorConfiguration indicatorConfiguration) {
        _priceGroup = priceGroup;
        _typeMap.clear();
        _dataObjects.clear();
        final Map<MealGroup, List<Meal>> groupedMeals = day.getMeals();
        int index = 0;
        for (final Entry<MealGroup, List<Meal>> groupedMeal : groupedMeals.entrySet()) {
            final List<Meal> meals = groupedMeal.getValue();
            if (meals != null && !meals.isEmpty()) {
                _dataObjects.add(index, groupedMeal.getKey());
                _typeMap.put(index, VIEW_TYPE_MEAL_GROUP);
                index++;
                for (final Meal meal : meals) {
                    _dataObjects.add(index, meal);
                    _typeMap.put(index, VIEW_TYPE_MEAL);
                    index++;
                }
            }
        }
        notifyDataSetChanged();
    }

    public void changePriceForCurrentData(@NonNull PriceGroup priceGroup) {
        _priceGroup = priceGroup;
        if (!_dataObjects.isEmpty()) {
            notifyDataSetChanged();
        }
    }

    public void changeIndicatorsForCurrentData(@NonNull WeekdayMealViewHolder.IndicatorConfiguration indicatorConfiguration) {
        _indicatorConfiguration = indicatorConfiguration;
        if (!_dataObjects.isEmpty()) {
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        final LayoutInflater layoutInflater = LayoutInflater.from(context);
        final RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_TYPE_MEAL_GROUP) {
            final View mealGroup = layoutInflater.inflate(R.layout.menu_overview_list_header, parent, false);
            viewHolder = new WeekdayMealGroupViewHolder(mealGroup);
        } else {
            final View meal = layoutInflater.inflate(R.layout.menu_overview_list_item, parent, false);
            viewHolder = new WeekdayMealViewHolder(meal);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final int type = getItemViewType(position);
        if (type == VIEW_TYPE_MEAL_GROUP && viewHolder instanceof WeekdayMealGroupViewHolder) {
            final WeekdayMealGroupViewHolder mealGroupViewHolder = (WeekdayMealGroupViewHolder) viewHolder;
            final MealGroup mealGroup = (MealGroup) _dataObjects.get(position);
            mealGroupViewHolder.setMealGroup(mealGroup);
        } else {
            final WeekdayMealViewHolder mealViewHolder = (WeekdayMealViewHolder) viewHolder;
            mealViewHolder.setIndicatorConfiguration(_indicatorConfiguration);
            final Meal meal = (Meal) _dataObjects.get(position);
            mealViewHolder.setMeal(meal, _priceGroup);
        }
    }

    @Override
    public int getItemCount() {
        return _dataObjects.size();
    }

    @Override
    public int getItemViewType(int position) {
        return _typeMap.get(position);
    }
}

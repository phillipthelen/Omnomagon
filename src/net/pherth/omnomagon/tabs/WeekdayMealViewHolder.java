package net.pherth.omnomagon.tabs;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import net.pherth.omnomagon.R;
import net.pherth.omnomagon.data.Meal;
import net.pherth.omnomagon.data.PriceGroup;

import java.util.List;

//todo make view expandable to show all additions?
public class WeekdayMealViewHolder extends RecyclerView.ViewHolder {

    private final TextView _mealName;
    private final TextView _description;
    private final TextView _price;
    private final ImageView _bioIndicator;
    private final ImageView _vegetarianIndicator;
    private final ImageView _veganIndicator;

    public WeekdayMealViewHolder(@NonNull View itemView) {
        super(itemView);
        _mealName = (TextView) itemView.findViewById(R.id.menu_overview_list_item_name);
        _description = (TextView) itemView.findViewById(R.id.menu_overview_list_item_description);
        _price = (TextView) itemView.findViewById(R.id.menu_overview_list_item_price);
        _bioIndicator = (ImageView) itemView.findViewById(R.id.menu_overview_list_indicator_bio);
        _vegetarianIndicator = (ImageView) itemView.findViewById(R.id.menu_overview_list_indicator_vegetarian);
        _veganIndicator = (ImageView) itemView.findViewById(R.id.menu_overview_list_indicator_vegan);
        //todo add fish indicator
    }

    public void setMeal(@NonNull Meal meal, @NonNull PriceGroup priceGroup) {
        setMealName(meal);
        setDescription(meal);
        setPrice(meal, priceGroup);
        setIndicatorVisibility(_bioIndicator, meal.getBio());
        setIndicatorVisibility(_vegetarianIndicator, meal.getVegetarian());
        setIndicatorVisibility(_veganIndicator, meal.getVegan());
    }

    private void setMealName(@NonNull Meal meal) {
        final String mealName = meal.getName();
        _mealName.setText(mealName);
    }

    private void setDescription(@NonNull Meal meal) {
        final StringBuilder stringBuilder = concatAdditions(meal);
        final String description = stringBuilder.toString();
        _description.setText(description);
    }

    @NonNull
    private StringBuilder concatAdditions(@NonNull Meal meal) {
        final List<String> additions = meal.getAdditions();
        final StringBuilder stringBuilder = new StringBuilder("");
        if (additions != null && !additions.isEmpty()) {
            boolean first = true;
            for (final String addition : additions) {
                if (!first) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(addition);
                first = false;
            }
        }
        return stringBuilder;
    }

    private void setPrice(@NonNull Meal meal, @NonNull PriceGroup priceGroup) {
        final int priceGroupTypeInteger = priceGroup.getTypeInteger();
        final String priceText = meal.getCorrectPriceString(priceGroupTypeInteger);
        _price.setText(priceText);
    }

    private void setIndicatorVisibility(@NonNull View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}

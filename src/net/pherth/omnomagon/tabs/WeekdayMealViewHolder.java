package net.pherth.omnomagon.tabs;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import net.pherth.omnomagon.R;
import net.pherth.omnomagon.data.Meal;
import net.pherth.omnomagon.data.PriceGroup;
import net.pherth.omnomagon.settings.UserPreferences;

import java.util.List;

public class WeekdayMealViewHolder extends RecyclerView.ViewHolder {

    private final TextView _mealName;
    private final TextView _description;
    private final TextView _price;
    private final ImageView _fishIndicator;
    private final ImageView _bioIndicator;
    private final ImageView _vegetarianIndicator;
    private final ImageView _veganIndicator;
    private IndicatorConfiguration _indicatorConfiguration = IndicatorConfiguration.defaultConfiguration();

    public WeekdayMealViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setTag(false);
        _mealName = (TextView) itemView.findViewById(R.id.menu_overview_list_item_name);
        _description = (TextView) itemView.findViewById(R.id.menu_overview_list_item_description);
        _price = (TextView) itemView.findViewById(R.id.menu_overview_list_item_price);
        _fishIndicator = (ImageView) itemView.findViewById(R.id.menu_overview_list_indicator_fish);
        _bioIndicator = (ImageView) itemView.findViewById(R.id.menu_overview_list_indicator_bio);
        _vegetarianIndicator = (ImageView) itemView.findViewById(R.id.menu_overview_list_indicator_vegetarian);
        _veganIndicator = (ImageView) itemView.findViewById(R.id.menu_overview_list_indicator_vegan);
    }

    public void setMeal(@NonNull Meal meal, @NonNull PriceGroup priceGroup) {
        setMealName(meal);
        setDescription(meal);
        setPrice(meal, priceGroup);
        setIndicatorVisibility(_fishIndicator, meal.getMsc() && _indicatorConfiguration._showFishIndicator);
        setIndicatorVisibility(_bioIndicator, meal.getBio() && _indicatorConfiguration._showBioIndicator);
        setIndicatorVisibility(_vegetarianIndicator, meal.getVegetarian() && _indicatorConfiguration._showVegetarianIndicator);
        setIndicatorVisibility(_veganIndicator, meal.getVegan() && _indicatorConfiguration._showVeganIndicator);
    }

    public void setIndicatorConfiguration(@NonNull IndicatorConfiguration indicatorConfiguration) {
        _indicatorConfiguration = indicatorConfiguration;
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
        final boolean hasPriceSet = meal.hasPriceSet();
        if (hasPriceSet) {
            final String priceText = meal.getCorrectPriceString(priceGroupTypeInteger);
            _price.setText(priceText);
        } else {
            _price.setText("");
        }
    }

    private void setIndicatorVisibility(@NonNull View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public static class IndicatorConfiguration {

        private static IndicatorConfiguration DEFAULT;

        private final boolean _showFishIndicator;
        private final boolean _showBioIndicator;
        private final boolean _showVegetarianIndicator;
        private final boolean _showVeganIndicator;

        @NonNull
        public static IndicatorConfiguration defaultConfiguration() {
            if (DEFAULT == null) {
                DEFAULT = new IndicatorConfiguration(true, true, true, true);
            }
            return DEFAULT;
        }

        @NonNull
        public static IndicatorConfiguration from(@NonNull UserPreferences userPreferences) {
            return new IndicatorConfiguration(userPreferences.fishFlagSelected(), userPreferences.bioFlagSelected(), userPreferences.vegetarianFlagSelected(), userPreferences.veganFlagSelected());
        }

        public IndicatorConfiguration(boolean showFishIndicator, boolean showBioIndicator, boolean showVegetarianIndicator, boolean showVeganIndicator) {
            _showFishIndicator = showFishIndicator;
            _showBioIndicator = showBioIndicator;
            _showVegetarianIndicator = showVegetarianIndicator;
            _showVeganIndicator = showVeganIndicator;
        }
    }
}

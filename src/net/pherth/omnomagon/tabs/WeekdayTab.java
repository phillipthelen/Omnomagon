package net.pherth.omnomagon.tabs;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import net.pherth.omnomagon.MenuOverviewActivity;
import net.pherth.omnomagon.R;
import net.pherth.omnomagon.SettingsActivity;
import net.pherth.omnomagon.data.Day;
import net.pherth.omnomagon.data.PriceGroup;
import net.pherth.omnomagon.settings.UserPreferences;

public class WeekdayTab extends Fragment {

    private ViewHolder _viewHolder;
    private WeekdayMealAdapter _weekdayMealAdapter;
    private boolean _dataWasSet;
    private Integer _tabPosition;

    public WeekdayTab() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.menu_overview_weekday_fragment, container, false);
        _viewHolder = new ViewHolder(rootView);
        final WeekdayMealAdapter weekdayMealAdapter = getWeekdayMealAdapter();
        configureHint(weekdayMealAdapter);
        _viewHolder.setAdapter(weekdayMealAdapter);
        return rootView;
    }

    public void refreshHint() {
        final WeekdayMealAdapter weekdayMealAdapter = getWeekdayMealAdapter();
        configureHint(weekdayMealAdapter);
    }

    private void configureHint(@NonNull WeekdayMealAdapter weekdayMealAdapter) {
        final int itemCount = weekdayMealAdapter.getItemCount();
        if (_viewHolder != null) {
            if (itemCount > 0) {
                _viewHolder.hideHint();
            } else {
                showErrorHint();
            }
        }
    }

    private void showErrorHint() {
        final MenuOverviewActivity activity = (MenuOverviewActivity) getActivity();
        if (activity != null && !activity.isFinishing()) {
            final UserPreferences userPreferences = new UserPreferences(activity);
            final boolean mensaSelected = userPreferences.validator().hasMensaSelected();
            if (mensaSelected) {
                if (_dataWasSet) {
                    _viewHolder.showHint(R.string.error_no_meals_found, null);
                } else {
                    showRefreshError(activity);
                }
            } else {
                showConfigurationError(activity);
            }
        }
    }

    private void showRefreshError(@NonNull final MenuOverviewActivity activity) {
        _viewHolder.showHint(R.string.error_no_refresh, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activity.isFinishing()) {
                    activity.triggerRefresh();
                }
            }
        });
    }

    private void showConfigurationError(final MenuOverviewActivity activity) {
        _viewHolder.showHint(R.string.error_no_mensa_selected, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activity.isFinishing()) {
                    final Intent intent = new Intent(activity, SettingsActivity.class);
                    activity.startActivity(intent);
                }
            }
        });
    }

    @NonNull
    private WeekdayMealAdapter getWeekdayMealAdapter() {
        if (_weekdayMealAdapter == null) {
            _weekdayMealAdapter = new WeekdayMealAdapter();
        }
        return _weekdayMealAdapter;
    }

    public void initializeData(@Nullable Day day, @NonNull PriceGroup priceGroup) {
        setData(day, priceGroup, true);
    }

    public void setData(@Nullable Day day, @NonNull PriceGroup priceGroup) {
        setData(day, priceGroup, false);
    }

    private void setData(@Nullable Day day, @NonNull PriceGroup priceGroup, boolean initialized) {
        final WeekdayMealAdapter weekdayMealAdapter = getWeekdayMealAdapter();
        if (day != null) {
            weekdayMealAdapter.setData(day, priceGroup);
        } else {
            weekdayMealAdapter.setData(Day.dummy(), priceGroup);
        }
        if (!initialized) {
            _dataWasSet = true;
        }
        if (_viewHolder != null) {
            configureHint(weekdayMealAdapter);
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

        private static Drawable GREYSCALE_LOGO;

        private final RecyclerView _listView;
        private final View _emptyListHintLayout;
        private final TextView _emptyListText;
        private final ImageView _emptyListImage;

        public ViewHolder(@NonNull View rootView) {
            _listView = (RecyclerView) rootView.findViewById(R.id.menu_overview_weekday_fragment_list);
            _emptyListHintLayout = rootView.findViewById(R.id.menu_overview_weekday_empty_list_hint);
            _emptyListText = (TextView) rootView.findViewById(R.id.menu_overview_weekday_empty_list_hint_text);
            _emptyListImage = (ImageView) rootView.findViewById(R.id.menu_overview_weekday_empty_list_hint_image);
            final Context context = rootView.getContext();
            configureViews(context);
        }

        private void configureViews(@NonNull Context context) {
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            _listView.setLayoutManager(linearLayoutManager);
            final DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
            _listView.setItemAnimator(defaultItemAnimator);
            final WeekdayMealGroupDivider mealGroupDivider = new WeekdayMealGroupDivider(context);
            _listView.addItemDecoration(mealGroupDivider);
            final Drawable greyscaleLogo = getGreyscaleLogo(context);
            _emptyListImage.setImageDrawable(greyscaleLogo);
        }

        @NonNull
        private Drawable getGreyscaleLogo(@NonNull Context context) {
            if (GREYSCALE_LOGO == null) {
                final Resources resources = context.getResources();
                final Drawable logo = resources.getDrawable(R.drawable.logo);
                final ColorMatrix colorMatrix = new ColorMatrix();
                colorMatrix.setSaturation(0);
                final ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
                logo.setColorFilter(colorMatrixColorFilter);
                GREYSCALE_LOGO = logo;
            }
            return GREYSCALE_LOGO;
        }

        public void setAdapter(@NonNull WeekdayMealAdapter weekdayMealAdapter) {
            _listView.setAdapter(weekdayMealAdapter);
        }

        public void showHint(@StringRes int hint, @Nullable View.OnClickListener onClickListener) {
            _emptyListText.setText(hint);
            _emptyListHintLayout.setOnClickListener(onClickListener);
            _emptyListHintLayout.setVisibility(View.VISIBLE);
        }

        public void hideHint() {
            _emptyListHintLayout.setVisibility(View.GONE);
        }
    }

    @Nullable
    public Integer getTabPosition() {
        return _tabPosition;
    }

    public void setTabPosition(int tabPosition) {
        _tabPosition = tabPosition;
    }
}

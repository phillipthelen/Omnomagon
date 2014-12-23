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

public class WeekdayTab extends Fragment {

    private static final String STATE_TAB_POSITION = "tabPosition";
    private static final String STATE_HINT = "hint";
    private static final String STATE_PRICE = "price";

    private ViewHolder _viewHolder;
    private WeekdayMealAdapter _weekdayMealAdapter;
    private Integer _tabPosition;
    private WeekdayTabHint _currentHint = WeekdayTabHint.NoMealData;
    private PriceGroup _currentPrice = PriceGroup.guests;
    private WeekdayMealViewHolder.IndicatorConfiguration _indicatorConfiguration = WeekdayMealViewHolder.IndicatorConfiguration.defaultConfiguration();

    public WeekdayTab() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        restoreStates(savedInstanceState);
        final View rootView = inflater.inflate(R.layout.menu_overview_weekday_fragment, container, false);
        _viewHolder = new ViewHolder(rootView);
        final WeekdayMealAdapter weekdayMealAdapter = getWeekdayMealAdapter();
        configureHint(weekdayMealAdapter);
        _viewHolder.setAdapter(weekdayMealAdapter);
        return rootView;
    }

    private void restoreStates(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            final int tabPosition = savedInstanceState.getInt(STATE_TAB_POSITION, -1);
            if (tabPosition >= 0) {
                _tabPosition = tabPosition;
            }
            final String hintName = savedInstanceState.getString(STATE_HINT);
            if (hintName != null) {
                _currentHint = WeekdayTabHint.valueOf(hintName);
            }
            final String priceName = savedInstanceState.getString(STATE_PRICE);
            if (priceName != null) {
                _currentPrice = PriceGroup.valueOf(priceName);
            }
        }
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
            if (_currentHint == WeekdayTabHint.NoMensaSelected) {
                showConfigurationError(activity);
            } else {
                if(_currentHint == WeekdayTabHint.UpdateInProgress) {
                    _viewHolder.showHint(R.string.retrvData, null);
                } else if(_currentHint == WeekdayTabHint.NoNetwork) {
                    showNetworkError(activity);
                } else if(_currentHint == WeekdayTabHint.SomethingWentWrong) {
                    showUnknownError(activity);
                } else {
                    _viewHolder.showHint(R.string.error_no_meals_found, null);
                }
            }
        }
    }

    private void showNetworkError(@NonNull final MenuOverviewActivity activity) {
        _viewHolder.showHint(R.string.error_no_network, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activity.isFinishing()) {
                    activity.triggerRefresh();
                }
            }
        });
    }

    private void showUnknownError(@NonNull final MenuOverviewActivity activity) {
        _viewHolder.showHint(R.string.error_unknown, new View.OnClickListener() {
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
                    activity.startActivityForResult(intent, MenuOverviewActivity.REQUEST_SETTINGS);
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

    public void setData(@Nullable Day day) {
        final WeekdayMealAdapter weekdayMealAdapter = getWeekdayMealAdapter();
        if (day != null) {
            weekdayMealAdapter.setData(day, _currentPrice, _indicatorConfiguration);
        } else {
            weekdayMealAdapter.setData(Day.dummy(), _currentPrice, _indicatorConfiguration);
        }
        if (_viewHolder != null) {
            configureHint(weekdayMealAdapter);
            _viewHolder.setAdapter(weekdayMealAdapter);
        }
    }

    public void setCurrentPrice(@NonNull PriceGroup priceGroup) {
        _currentPrice = priceGroup;
        final WeekdayMealAdapter weekdayMealAdapter = getWeekdayMealAdapter();
        weekdayMealAdapter.changePriceForCurrentData(priceGroup);
    }

    public void setCurrentIndicators(@NonNull WeekdayMealViewHolder.IndicatorConfiguration indicators) {
        _indicatorConfiguration = indicators;
        final WeekdayMealAdapter weekdayMealAdapter = getWeekdayMealAdapter();
        weekdayMealAdapter.changeIndicatorsForCurrentData(indicators);
    }

    public void setCurrentHint(@NonNull WeekdayTabHint currentHint) {
        _currentHint = currentHint;
        final WeekdayMealAdapter weekdayMealAdapter = getWeekdayMealAdapter();
        configureHint(weekdayMealAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (_tabPosition != null) {
            outState.putInt(STATE_TAB_POSITION, _tabPosition);
        }
        if (_currentHint != null) {
            outState.putString(STATE_HINT, _currentHint.name());
        }
        if (_currentPrice != null) {
            outState.putString(STATE_PRICE, _currentPrice.name());
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

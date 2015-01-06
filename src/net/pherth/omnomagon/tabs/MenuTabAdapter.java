package net.pherth.omnomagon.tabs;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import net.pherth.omnomagon.R;
import net.pherth.omnomagon.data.Day;
import net.pherth.omnomagon.data.PriceGroup;

import java.util.List;

public class MenuTabAdapter extends FragmentStatePagerAdapter {

    private final static int DEFAULT_TAB_COUNT = 5;

    private final Context _context;
    private final FragmentManager _fragmentManager;
    private List<Day> _data;
    private PriceGroup _priceGroup = PriceGroup.guests;
    private WeekdayTabHint _hint = WeekdayTabHint.NoMealData;
    private WeekdayMealViewHolder.IndicatorConfiguration _indicatorConfiguration = WeekdayMealViewHolder.IndicatorConfiguration.defaultConfiguration();

    public MenuTabAdapter(@NonNull Context context, @NonNull FragmentManager fragmentManager) {
        super(fragmentManager);
        _fragmentManager = fragmentManager;
        _context = context;
    }

    @Override
    public Fragment getItem(int position) {
        final WeekdayTab weekdayTab = new WeekdayTab();
        weekdayTab.setTabPosition(position);
        weekdayTab.setCurrentPrice(_priceGroup);
        weekdayTab.setCurrentHint(_hint);
        final Day day = getDay(position);
        weekdayTab.setData(day);
        return weekdayTab;
    }

    @Nullable
    private Day getDay(int position) {
        final Day day;
        if (_data != null && position < _data.size()) {
            day = _data.get(position);
        } else {
            day = null;
        }
        return day;
    }

    @Override
    public int getCount() {
        return DEFAULT_TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        final Resources resources = _context.getResources();
        final String[] weekDays = resources.getStringArray(R.array.weekDays);
        return weekDays[position];
    }

    public void setData(@Nullable List<Day> data) {
        if (_data != data) {
            _data = data;
            forEachFragment(new FragmentOperation<WeekdayTab>() {
                @Override
                public void invoke(@NonNull WeekdayTab tab) {
                    final Integer tabPosition = tab.getTabPosition();
                    if (tabPosition != null) {
                        final Day day = getDay(tabPosition);
                        tab.setData(day);
                    } else {
                        tab.setData(null);
                    }
                }
            });
        }
    }

    public void setPriceGroup(@NonNull final PriceGroup priceGroup) {
        if (_priceGroup != priceGroup) {
            _priceGroup = priceGroup;
            forEachFragment(new FragmentOperation<WeekdayTab>() {
                @Override
                public void invoke(@NonNull WeekdayTab tab) {
                    tab.setCurrentPrice(priceGroup);
                }
            });
        }
    }

    public void setHint(@NonNull final WeekdayTabHint hint) {
        if (_hint != hint) {
            _hint = hint;
            forEachFragment(new FragmentOperation<WeekdayTab>() {
                @Override
                public void invoke(@NonNull WeekdayTab tab) {
                    tab.setCurrentHint(hint);
                }
            });
        }
    }

    public void setIndicators(@NonNull final WeekdayMealViewHolder.IndicatorConfiguration indicators) {
        if (_indicatorConfiguration != indicators) {
            _indicatorConfiguration = indicators;
            forEachFragment(new FragmentOperation<WeekdayTab>() {
                @Override
                public void invoke(@NonNull WeekdayTab tab) {
                    tab.setCurrentIndicators(indicators);
                }
            });
        }
    }

    private void forEachFragment(@NonNull FragmentOperation<WeekdayTab> fragmentOperation) {
        final List<Fragment> fragments = _fragmentManager.getFragments();
        if (fragments != null) {
            for (final Fragment fragment : fragments) {
                if (fragment instanceof WeekdayTab) {
                    fragmentOperation.invoke((WeekdayTab) fragment);
                }
            }
        }
    }

    private static interface FragmentOperation<T extends Fragment> {
        public void invoke(@NonNull T tab);
    }
}

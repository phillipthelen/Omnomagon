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
import net.pherth.omnomagon.data.MealGroupBasedSorter;
import net.pherth.omnomagon.data.PriceGroup;

import java.util.List;

public class MenuTabAdapter extends FragmentStatePagerAdapter {

    private final static int DEFAULT_TAB_COUNT = 5;

    private final Context _context;
    private final FragmentManager _fragmentManager;
    private List<Day> _data;
    private PriceGroup _priceGroup = PriceGroup.guests;

    public MenuTabAdapter(@NonNull Context context, @NonNull FragmentManager fragmentManager) {
        super(fragmentManager);
        _fragmentManager = fragmentManager;
        _context = context;
    }

    @Override
    public Fragment getItem(int position) {
        final WeekdayTab weekdayTab = new WeekdayTab();
        weekdayTab.setTabPosition(position);
        if (_data != null) {
            weekdayTab.setData(_data.get(position), _priceGroup);
        } else {
            weekdayTab.initializeData(null, _priceGroup);
        }
        return weekdayTab;
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

    public void setData(@NonNull List<Day> data, @Nullable PriceGroup priceGroup) {
        MealGroupBasedSorter.sort(data);
        _data = data;
        _priceGroup = (priceGroup == null) ? PriceGroup.guests : priceGroup;
        refreshFragmentData();
    }

    private void refreshFragmentData() {
        final List<Fragment> fragments = _fragmentManager.getFragments();
        if (fragments != null) {
            for (final Fragment fragment : fragments) {
                if (fragment instanceof WeekdayTab) {
                    final WeekdayTab weekdayTab = (WeekdayTab) fragment;
                    final Integer tabPosition = weekdayTab.getTabPosition();
                    if (tabPosition != null) {
                        final Day day = _data.get(tabPosition);
                        weekdayTab.setData(day, _priceGroup);
                    }
                }
            }
        }
    }

    public void refreshHints() {
        final List<Fragment> fragments = _fragmentManager.getFragments();
        if (fragments != null) {
            for (final Fragment fragment : fragments) {
                if (fragment instanceof WeekdayTab) {
                    final WeekdayTab weekdayTab = (WeekdayTab) fragment;
                    weekdayTab.refreshHint();
                }
            }
        }
    }
}

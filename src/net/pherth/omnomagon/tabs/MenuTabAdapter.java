package net.pherth.omnomagon.tabs;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import net.pherth.omnomagon.R;
import net.pherth.omnomagon.data.Day;
import net.pherth.omnomagon.data.DummyDataProvider;
import net.pherth.omnomagon.data.PriceGroup;

import java.util.List;

public class MenuTabAdapter extends FragmentStatePagerAdapter {

    private final static int DEFAULT_TAB_COUNT = 5;

    private final Context _context;
    private final List<Day> dummyData = DummyDataProvider.generateDummyData();

    public MenuTabAdapter(@NonNull Context context, @NonNull FragmentManager fragmentManager) {
        super(fragmentManager);
        _context = context;
    }

    @Override
    public Fragment getItem(int position) {
        final WeekdayTab weekdayTab = new WeekdayTab();
        weekdayTab.setData(dummyData.get(position), PriceGroup.students);
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
}

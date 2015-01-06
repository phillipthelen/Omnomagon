package net.pherth.omnomagon.tabs;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.astuetz.PagerSlidingTabStrip;
import net.pherth.omnomagon.R;

import java.util.ArrayList;
import java.util.List;

public class MenuTabHandler implements ViewPager.OnPageChangeListener {

    private final List<TextView> _tabs;
    private final ViewPager.OnPageChangeListener _onPageChangeListener;
    private final MenuTabAdapter _menuTabAdapter;
    private final ViewPager _viewPager;
    private int _selectedTab = 0;

    public MenuTabHandler(@NonNull ActionBarActivity actionBarActivity, @NonNull ViewPager.OnPageChangeListener onPageChangeListener) {
        _onPageChangeListener = onPageChangeListener;
        final MenuTabAdapter menuTabAdapter = createTabAdapter(actionBarActivity);
        _menuTabAdapter = menuTabAdapter;
        final int menuTabAdapterCount = menuTabAdapter.getCount();
        _tabs = new ArrayList<TextView>(menuTabAdapterCount);
        _viewPager = createViewPager(actionBarActivity, menuTabAdapter);
        createTabs(actionBarActivity);
    }

    @NonNull
    private MenuTabAdapter createTabAdapter(@NonNull ActionBarActivity actionBarActivity) {
        final FragmentManager supportFragmentManager = actionBarActivity.getSupportFragmentManager();
        return new MenuTabAdapter(actionBarActivity, supportFragmentManager);
    }

    @NonNull
    private ViewPager createViewPager(@NonNull ActionBarActivity actionBarActivity, @NonNull MenuTabAdapter menuTabAdapter) {
        final ViewPager viewPager = (ViewPager) actionBarActivity.findViewById(R.id.menu_overview_view_pager);
        viewPager.setAdapter(menuTabAdapter);
        return viewPager;
    }

    private void createTabs(@NonNull ActionBarActivity actionBarActivity) {
        final PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) actionBarActivity.findViewById(R.id.menu_overview_tabs);
        tabStrip.setViewPager(_viewPager);
        tabStrip.setOnPageChangeListener(this);
        final LinearLayout tabContainer = (LinearLayout) tabStrip.getChildAt(0);
        for (int i = 0; i < tabContainer.getChildCount(); i++) {
            final TextView tab = (TextView) tabContainer.getChildAt(i);
            final Resources resources = actionBarActivity.getResources();
            final ColorStateList colorStateList = resources.getColorStateList(R.color.tab_text_color);
            tab.setTextColor(colorStateList);
            if (i == 0) {
                tab.setSelected(true);
            }
            _tabs.add(tab);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        _onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        if (_viewPager != null) {
            _viewPager.setCurrentItem(position);
            _tabs.get(_selectedTab).setSelected(false);
            _tabs.get(position).setSelected(true);
            _selectedTab = position;
        }
        _onPageChangeListener.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        _onPageChangeListener.onPageScrollStateChanged(state);
    }

    public int getSelectedTab() {
        return _selectedTab;
    }

    @NonNull
    public MenuTabAdapter getMenuTabAdapter() {
        return _menuTabAdapter;
    }
}

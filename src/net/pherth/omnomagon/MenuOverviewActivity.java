package net.pherth.omnomagon;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.astuetz.PagerSlidingTabStrip;
import net.pherth.omnomagon.tabs.MenuTabAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenuOverviewActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener {

    private final List<TextView> _tabs = new ArrayList<TextView>(7);
    private ViewPager _viewPager;
    private int _selectedTab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_overview);
        configureActionBar();
        setUpViewPager();
        setUpTabs();
    }

    private void configureActionBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.support_actionbar);
        setSupportActionBar(toolbar);
        final ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayShowTitleEnabled(false);
    }

    private void setUpViewPager() {
        final FragmentManager supportFragmentManager = getSupportFragmentManager();
        final MenuTabAdapter menuTabAdapter = new MenuTabAdapter(supportFragmentManager);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.menu_overview_view_pager);
        viewPager.setAdapter(menuTabAdapter);
        _viewPager = viewPager;
    }

    private void setUpTabs() {
        final PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.menu_overview_tabs);
        tabStrip.setViewPager(_viewPager);
        tabStrip.setOnPageChangeListener(this);
        final LinearLayout tabContainer = (LinearLayout) tabStrip.getChildAt(0);
        for (int i = 0; i < tabContainer.getChildCount(); i++) {
            final TextView tab = (TextView) tabContainer.getChildAt(i);
            final Resources resources = getResources();
            final ColorStateList colorStateList = resources.getColorStateList(R.color.tab_text_color);
            tab.setTextColor(colorStateList);
            if (i == 0) {
                tab.setSelected(true);
            }
            _tabs.add(tab);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_overview, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

    @Override
    public void onPageSelected(int position) {
        if (_viewPager != null) {
            _viewPager.setCurrentItem(position);
            _tabs.get(_selectedTab).setSelected(false);
            _tabs.get(position).setSelected(true);
            _selectedTab = position;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) { }
}

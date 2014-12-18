package net.pherth.omnomagon;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import net.pherth.omnomagon.header.FeatureImageHandler;
import net.pherth.omnomagon.header.MensaNameViewHolder;
import net.pherth.omnomagon.tabs.MenuTabHandler;

public class MenuOverviewActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener {

    private static final long FIVE_MINUTES = 1000L * 60L * 5L;

    private MensaNameViewHolder _mensaNameViewHolder;
    private MenuTabHandler _menuTabHandler;
    private FeatureImageHandler _featureImageHandler;
    private long _lastFeatureImageChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_overview);
        configureActionBar();
        _mensaNameViewHolder = new MensaNameViewHolder(this);
        _mensaNameViewHolder.setMensaName("Veggie Nr.1");
        _menuTabHandler = new MenuTabHandler(this, this);
        _featureImageHandler = new FeatureImageHandler(this);
        _lastFeatureImageChange = System.currentTimeMillis();
    }

    private void configureActionBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.support_actionbar);
        setSupportActionBar(toolbar);
        final ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeFeatureImageAfterFiveMinutes();
    }

    private void changeFeatureImageAfterFiveMinutes() {
        final long currentTimeMillis = System.currentTimeMillis();
        if (Math.abs(currentTimeMillis - _lastFeatureImageChange) >= FIVE_MINUTES) {
            _featureImageHandler.switchFeatureImage();
            final int selectedTab = _menuTabHandler.getSelectedTab();
            _featureImageHandler.onPageSelected(selectedTab);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_overview, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        _featureImageHandler.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        _featureImageHandler.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        _featureImageHandler.onPageScrollStateChanged(state);
    }
}

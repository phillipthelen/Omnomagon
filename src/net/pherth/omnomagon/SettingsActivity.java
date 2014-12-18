package net.pherth.omnomagon;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import net.pherth.omnomagon.settings.*;

public class SettingsActivity extends ActionBarActivity implements CitySelectionViewHolder.Listener {

    private UserPreferences _userPreferences;
    private CitySelectionViewHolder _citySelectionViewHolder;
    private MensaSelectionViewHolder _mensaSelectionViewHolder;
    private PriceSelectionViewHolder _priceSelectionViewHolder;
    private SettingsViewHolder _settingsViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        configureActionBar();
        _citySelectionViewHolder = new CitySelectionViewHolder(this, this);
        _mensaSelectionViewHolder = new MensaSelectionViewHolder(this);
        _priceSelectionViewHolder = new PriceSelectionViewHolder(this);
        _settingsViewHolder = new SettingsViewHolder(this);
    }

    private void configureActionBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.support_actionbar);
        setSupportActionBar(toolbar);
        final ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayShowHomeEnabled(true);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onCitySelected() {
        _mensaSelectionViewHolder.onCitySelected();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = false;
        final int id = item.getItemId();
        if (id == android.R.id.home) {
            handled = true;
            finish();
        }
        return handled || super.onOptionsItemSelected(item);
    }

    @NonNull
    public UserPreferences getUserPreferences() {
        if (_userPreferences == null) {
            _userPreferences = new UserPreferences(this);
        }
        return _userPreferences;
    }
}

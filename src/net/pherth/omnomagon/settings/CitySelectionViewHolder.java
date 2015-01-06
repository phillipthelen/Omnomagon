package net.pherth.omnomagon.settings;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import net.pherth.omnomagon.R;
import net.pherth.omnomagon.SettingsActivity;

public class CitySelectionViewHolder implements PopupMenu.OnMenuItemClickListener {

    private final UserPreferences _userPreferences;
    private final Listener _listener;
    private final View _citySelectionButton;
    private final TextView _citySelectionValue;

    public CitySelectionViewHolder(@NonNull SettingsActivity settingsActivity) {
        this(settingsActivity, null);
    }

    public CitySelectionViewHolder(@NonNull SettingsActivity settingsActivity, @Nullable Listener listener) {
        _userPreferences = settingsActivity.getUserPreferences();
        _listener = listener;
        _citySelectionButton = settingsActivity.findViewById(R.id.settings_city_button);
        _citySelectionValue = (TextView) settingsActivity.findViewById(R.id.settings_city_value);
        initializeValues();
        PopupMenuHelper.configurePopupMenu(_citySelectionButton, R.array.cityList, this);
    }

    private void initializeValues() {
        final Integer selectedCityId = _userPreferences.getSelectedCityId();
        if (selectedCityId != null) {
            _citySelectionValue.setText(selectedCityId);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        final CharSequence title = menuItem.getTitle();
        _citySelectionValue.setText(title);
        final int cityId = menuItem.getItemId();
        _userPreferences.setSelectedCityId(cityId);
        if (_listener != null) {
            _listener.onCitySelected();
        }
        return true;
    }

    public static interface Listener {
        public void onCitySelected();
    }
}

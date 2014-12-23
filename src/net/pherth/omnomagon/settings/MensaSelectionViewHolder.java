package net.pherth.omnomagon.settings;

import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import net.pherth.omnomagon.R;
import net.pherth.omnomagon.SettingsActivity;

public class MensaSelectionViewHolder implements PopupMenu.OnMenuItemClickListener, CitySelectionViewHolder.Listener {

    private final SettingsActivity _settingsActivity;
    private final UserPreferences _userPreferences;
    private final View _mensaSelectionButton;
    private final TextView _mensaSelectionValue;

    public MensaSelectionViewHolder(@NonNull SettingsActivity settingsActivity) {
        _settingsActivity = settingsActivity;
        _userPreferences = settingsActivity.getUserPreferences();
        _mensaSelectionButton = settingsActivity.findViewById(R.id.settings_mensa_button);
        _mensaSelectionValue = (TextView) settingsActivity.findViewById(R.id.settings_mensa_value);
        initializeValues();
        generatePopupMenu();
    }

    private void initializeValues() {
        final Integer selectedCityId = _userPreferences.getSelectedCityId();
        if (selectedCityId != null) {
            final Integer selectedMensaId = _userPreferences.getSelectedMensaId();
            if (selectedMensaId != null) {
                _mensaSelectionValue.setText(selectedMensaId);
            }
        }
    }

    private void generatePopupMenu() {
        final Integer selectedCityId = _userPreferences.getSelectedCityId();
        if (selectedCityId == null) {
            _mensaSelectionButton.setOnClickListener(null);
            _mensaSelectionValue.setHint(R.string.settings_select_hint_select_city_first);
        } else {
            if (selectedCityId == R.string.city_berlin) {
                _mensaSelectionValue.setHint(R.string.settings_select_hint);
                PopupMenuHelper.configurePopupMenu(_mensaSelectionButton, R.array.beList, this);
            } else {
                _mensaSelectionButton.setOnClickListener(null);
                _userPreferences.setSelectedMensaId(null);
                _mensaSelectionValue.setHint(R.string.settings_select_hint_only_one_mensa);
            }
        }
    }

    @Override
    public void onCitySelected() {
        _mensaSelectionValue.setText(null);
        generatePopupMenu();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        _settingsActivity.registerChange(SettingsActivity.CHANGED_MENSA);
        final CharSequence title = menuItem.getTitle();
        _mensaSelectionValue.setText(title);
        final int mensaId = menuItem.getItemId();
        _userPreferences.setSelectedMensaId(mensaId);
        return true;
    }
}

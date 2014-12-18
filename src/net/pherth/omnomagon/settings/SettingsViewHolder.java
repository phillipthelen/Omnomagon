package net.pherth.omnomagon.settings;

import android.support.annotation.NonNull;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import net.pherth.omnomagon.R;
import net.pherth.omnomagon.SettingsActivity;

public class SettingsViewHolder implements CompoundButton.OnCheckedChangeListener {

    private final UserPreferences _userPreferences;
    private final SwitchCompat _autoRefreshSwitch;
    private final SwitchCompat _vegetarianSwitch;
    private final SwitchCompat _veganSwitch;
    private final SwitchCompat _bioSwitch;
    private final SwitchCompat _fishSwitch;

    public SettingsViewHolder(@NonNull SettingsActivity settingsActivity) {
        _userPreferences = settingsActivity.getUserPreferences();
        _autoRefreshSwitch = (SwitchCompat) settingsActivity.findViewById(R.id.settings_auto_update_switch);
        _vegetarianSwitch = (SwitchCompat) settingsActivity.findViewById(R.id.settings_indicator_vegetarian_switch);
        _veganSwitch = (SwitchCompat) settingsActivity.findViewById(R.id.settings_indicator_vegan_switch);
        _bioSwitch = (SwitchCompat) settingsActivity.findViewById(R.id.settings_indicator_bio_switch);
        _fishSwitch = (SwitchCompat) settingsActivity.findViewById(R.id.settings_indicator_fish_switch);
        initializeValues();
        setListener(_autoRefreshSwitch, _vegetarianSwitch, _veganSwitch, _bioSwitch, _fishSwitch);
    }

    private void initializeValues() {
        _autoRefreshSwitch.setChecked(_userPreferences.shouldReloadAutomatically());
        _vegetarianSwitch.setChecked(_userPreferences.vegetarianFlagSelected());
        _veganSwitch.setChecked(_userPreferences.veganFlagSelected());
        _bioSwitch.setChecked(_userPreferences.bioFlagSelected());
        _fishSwitch.setChecked(_userPreferences.fishFlagSelected());
    }

    private void setListener(@NonNull CompoundButton... buttons) {
        for (final CompoundButton button : buttons) {
            button.setOnCheckedChangeListener(this);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == _autoRefreshSwitch) {
            _userPreferences.setAutomaticReload(isChecked);
        } else if (buttonView == _vegetarianSwitch) {
            _userPreferences.setVegetarianFlag(isChecked);
        } else if (buttonView == _veganSwitch) {
            _userPreferences.setVeganFlag(isChecked);
        } else if (buttonView == _bioSwitch) {
            _userPreferences.setBioFlag(isChecked);
        } else if (buttonView == _fishSwitch) {
            _userPreferences.setFishFlag(isChecked);
        }
    }
}

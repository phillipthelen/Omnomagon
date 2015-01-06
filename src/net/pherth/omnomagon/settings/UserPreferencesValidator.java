package net.pherth.omnomagon.settings;

import android.support.annotation.NonNull;
import net.pherth.omnomagon.R;

public class UserPreferencesValidator {

    private final UserPreferences _userPreferences;

    public UserPreferencesValidator(@NonNull UserPreferences userPreferences) {
        _userPreferences = userPreferences;
    }

    public boolean hasMensaSelected() {
        boolean selectionValid = false;
        final Integer cityId = _userPreferences.getSelectedCityId();
        if (cityId != null) {
            if (cityId == R.string.city_berlin) {
                final Integer selectedMensaId = _userPreferences.getSelectedMensaId();
                selectionValid = (selectedMensaId != null);
            } else {
                selectionValid = true;
            }
        }
        return selectionValid;
    }
}

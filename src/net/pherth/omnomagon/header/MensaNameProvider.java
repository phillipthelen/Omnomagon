package net.pherth.omnomagon.header;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import net.pherth.omnomagon.R;
import net.pherth.omnomagon.settings.UserPreferences;

public class MensaNameProvider {

    private final UserPreferences _userPreferences;
    private final Context _context;

    public MensaNameProvider(@NonNull Activity activity) {
        _userPreferences = new UserPreferences(activity);
        _context = activity;
    }

    @NonNull
    public String getName() {
        String name = "";
        final Integer selectedCityId = _userPreferences.getSelectedCityId();
        if (selectedCityId != null) {
            final Integer selectedMensaId = _userPreferences.getSelectedMensaId();
            if (selectedCityId == R.string.city_berlin) {
                if (selectedMensaId != null) {
                    name = _context.getString(selectedMensaId);
                }
            } else if (selectedCityId == R.string.city_ulm) {
                name = _context.getString(R.string.ulm_mensa);
            }
        }
        return name;
    }
}

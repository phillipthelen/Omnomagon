package net.pherth.omnomagon.data.mensa;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import net.pherth.omnomagon.R;
import net.pherth.omnomagon.data.Data;
import net.pherth.omnomagon.data.Day;
import net.pherth.omnomagon.data.Mensa;
import net.pherth.omnomagon.settings.UserPreferences;

import java.util.Calendar;
import java.util.List;

public class DatabaseSnapshot implements Mensa {

    private static final String SHARED_PREFERENCES_NAME = "Omnomagon.Snapshot";
    private static final String SHARED_PREFERENCES_KEY_MENSA = SHARED_PREFERENCES_NAME + ".Mensa";
    private static final String SHARED_PREFERENCES_KEY_WEEK_IN_YEAR = SHARED_PREFERENCES_NAME + ".Week";
    private static final String SHARED_PREFERENCES_KEY_YEAR = SHARED_PREFERENCES_NAME + ".Year";

    private final Context _context;
    private final Data _data;

    public DatabaseSnapshot(@NonNull Context context, @NonNull Data data) {
        _context = context;
        _data = data;
    }

    public static void storeMetadata(@NonNull Context context) {
        final UserPreferences userPreferences = new UserPreferences(context);
        final Integer selectedMensaId = userPreferences.getSelectedMensaId();
        if (selectedMensaId != null) {
            final SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            final Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            editor.putInt(SHARED_PREFERENCES_KEY_WEEK_IN_YEAR, calendar.get(Calendar.WEEK_OF_YEAR));
            editor.putInt(SHARED_PREFERENCES_KEY_YEAR, calendar.get(Calendar.YEAR));
            editor.putInt(SHARED_PREFERENCES_KEY_MENSA, selectedMensaId);
            editor.commit();
        }
    }

    private boolean hasValidSnapshot() {
        boolean hasValidData = true;
        final UserPreferences userPreferences = new UserPreferences(_context);
        final Integer selectedMensaId = userPreferences.getSelectedMensaId();
        if (selectedMensaId != null) {
            final SharedPreferences sharedPreferences = _context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
            final int savedMensa = sharedPreferences.getInt(SHARED_PREFERENCES_KEY_MENSA, R.string.error_no_mensa_selected);
            if (savedMensa != selectedMensaId) {
                hasValidData = false;
            } else {
                final Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                final int currentYear = calendar.get(Calendar.YEAR);
                final int savedYear = sharedPreferences.getInt(SHARED_PREFERENCES_KEY_YEAR, 0);
                if (currentYear != savedYear) {
                    hasValidData = false;
                } else {
                    final int savedWeek = sharedPreferences.getInt(SHARED_PREFERENCES_KEY_WEEK_IN_YEAR, 0);
                    final int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
                    if (savedWeek != currentWeek) {
                        hasValidData = false;
                    }
                }
            }
        } else {
            hasValidData = false;
        }
        return hasValidData;
    }

    @Nullable
    @Override
    public List<Day> getMeals() {
        final boolean hasValidSnapshot = hasValidSnapshot();
        return hasValidSnapshot ? _data.loadDataFromDatabase() : null;
    }
}

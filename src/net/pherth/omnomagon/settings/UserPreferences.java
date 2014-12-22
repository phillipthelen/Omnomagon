package net.pherth.omnomagon.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import static net.pherth.omnomagon.settings.UserPreferences.Setting.*;

public class UserPreferences {

    private static final String SHARED_PREFERENCES_NAME = "Omnomagon.Settings";

    protected enum Setting {
        AutomaticReload,
        City, Mensa, Price,
        VegetarianFlag, VeganFlag, BioFlag, FishFlag;

        public String peferenceName() {
            return SHARED_PREFERENCES_NAME + "." + name();
        }
    }

    private final Context _context;

    public UserPreferences(@NonNull Context context) {
        _context = context;
    }

    @NonNull
    private SharedPreferences getPreferences() {
        return _context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @NonNull
    private SharedPreferences.Editor getEditor() {
        return getPreferences().edit();
    }

    private void commitOrApply(@NonNull SharedPreferences.Editor editor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    public boolean shouldReloadAutomatically() {
        return getPreferences().getBoolean(AutomaticReload.peferenceName(), true);
    }

    public void setAutomaticReload(boolean automaticReload) {
        setFlag(AutomaticReload, automaticReload);
    }

    private void setFlag(@NonNull Setting setting, boolean flag) {
        final SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(setting.peferenceName(), flag);
        commitOrApply(editor);
    }

    @StringRes
    @Nullable
    public Integer getSelectedCityId() {
        return getId(City);
    }

    @StringRes
    @Nullable
    public Integer getSelectedMensaId() {
        return getId(Mensa);
    }

    @StringRes
    @Nullable
    public Integer getSelectedPriceId() {
        return getId(Price);
    }

    @Nullable
    private Integer getId(@NonNull Setting setting) {
        final String id = getPreferences().getString(setting.peferenceName(), null);
        return id == null ? null : Integer.valueOf(id);
    }

    public void setSelectedCityId(@StringRes @Nullable Integer id) {
        setId(City, id);
    }

    public void setSelectedMensaId(@StringRes @Nullable Integer id) {
        setId(Mensa, id);
    }

    public void setSelectedPriceId(@StringRes @Nullable Integer id) {
        setId(Price, id);
    }

    private void setId(@NonNull Setting setting, @Nullable Integer id) {
        final SharedPreferences.Editor editor = getEditor();
        if (id != null) {
            editor.putString(setting.peferenceName(), String.valueOf(id));
        } else {
            editor.remove(setting.peferenceName());
        }
        commitOrApply(editor);
    }

    public boolean vegetarianFlagSelected() {
        return getPreferences().getBoolean(VegetarianFlag.peferenceName(), false);
    }

    public boolean veganFlagSelected() {
        return getPreferences().getBoolean(VeganFlag.peferenceName(), false);
    }

    public boolean bioFlagSelected() {
        return getPreferences().getBoolean(BioFlag.peferenceName(), false);
    }

    public boolean fishFlagSelected() {
        return getPreferences().getBoolean(FishFlag.peferenceName(), false);
    }

    public void setVegetarianFlag(boolean vegetarianFlag) {
        setFlag(VegetarianFlag, vegetarianFlag);
    }

    public void setVeganFlag(boolean veganFlag) {
        setFlag(VeganFlag, veganFlag);
    }

    public void setBioFlag(boolean bioFlag) {
        setFlag(BioFlag, bioFlag);
    }

    public void setFishFlag(boolean fishFlag) {
        setFlag(FishFlag, fishFlag);
    }

    public UserPreferencesValidator validator() {
        return new UserPreferencesValidator(this);
    }
}

package net.pherth.omnomagon.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import net.pherth.omnomagon.settings.UserPreferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DataProvider {

    private static final String SHARED_PREFERENCES_NAME = "Omnomagon.AutoUpdate";
    private static final String SHARED_PREFERENCES_LAST_UPDATE_KEY = SHARED_PREFERENCES_NAME + ".LastUpdate";

    private enum State {
        Uninitialized, Requested, Initialized
    }

    private final Activity _activity;
    private List<Day> _data = new ArrayList<Day>(0);
    private State _state = State.Uninitialized;
    private RequestDataTask _requestDataTask;
    private boolean _blockAutoRefresh;

    public DataProvider(@NonNull Activity activity) {
        _activity = activity;
    }

    public void blockNextAutoRefresh() {
        _blockAutoRefresh = true;
    }

    public boolean autoRefreshForMensa(@NonNull DataListener dataListener, @NonNull UserPreferences userPreferences) {
        final boolean shouldReloadAutomatically = userPreferences.shouldReloadAutomatically();
        final boolean shouldRefresh = shouldReloadAutomatically && !_blockAutoRefresh;
        if (shouldRefresh) {
            final Calendar instance = Calendar.getInstance();
            final int dayOfYear = instance.get(Calendar.DAY_OF_YEAR);
            final SharedPreferences sharedPreferences = getAutoUpdatePreferences();
            final int lastUpdate = sharedPreferences.getInt(SHARED_PREFERENCES_LAST_UPDATE_KEY, -1);
            if (dayOfYear != lastUpdate) {
                requestDataForMensa(dataListener, userPreferences);
            }
        }
        _blockAutoRefresh = false;
        return shouldRefresh;
    }

    private SharedPreferences getAutoUpdatePreferences() {
        return _activity.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void requestDataForMensa(@NonNull DataListener dataListener, @NonNull UserPreferences userPreferences) {
        final boolean mensaSelected = userPreferences.validator().hasMensaSelected();
        if (mensaSelected) {
            final Integer cityId = userPreferences.getSelectedCityId();
            final Integer mensaId = userPreferences.getSelectedMensaId();
            final Mensa mensa = MensaFactory.getMensa(cityId, mensaId);
            requestData(dataListener, mensa);
        }
    }

    private synchronized void requestData(@NonNull DataListener dataListener, @NonNull Mensa mensa) {
        cancelRunningRequests();
        final RequestDataTask requestDataTask = new RequestDataTask(_activity, this, dataListener);
        _requestDataTask = requestDataTask;
        requestDataTask.execute();
    }

    private void cancelRunningRequests() {
        if (_requestDataTask != null) {
            final AsyncTask.Status status = _requestDataTask.getStatus();
            if (status != AsyncTask.Status.FINISHED) {
                _requestDataTask.cancel(true);
            }
        }
    }

    public void setData(@Nullable List<Day> data) {
        if (data != null) {
            MealGroupBasedSorter.sort(data);
        }
        _data = (data != null) ? data : new ArrayList<Day>(0);
        _state = State.Initialized;
    }

    public void setLastUpdateDayOfYear() {
        final Calendar instance = Calendar.getInstance();
        final int dayOfYear = instance.get(Calendar.DAY_OF_YEAR);
        final SharedPreferences sharedPreferences = getAutoUpdatePreferences();
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SHARED_PREFERENCES_LAST_UPDATE_KEY, dayOfYear);
        commitOrApply(editor);
    }

    private void commitOrApply(@NonNull SharedPreferences.Editor editor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    @NonNull
    public List<Day> getData() {
        return _data;
    }

    public boolean isInitialized() {
        return _state == State.Initialized;
    }

    public boolean hasRequestedData() {
        return _state == State.Requested || _state == State.Initialized;
    }

    public static interface DataListener {
        public void onNetworkError();
        public void onUnknownError();
        public void onDataReceived(@Nullable List<Day> data);
    }
}

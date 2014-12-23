package net.pherth.omnomagon.data;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import net.pherth.omnomagon.settings.UserPreferences;

import java.util.ArrayList;
import java.util.List;

public class DataProvider {

    private enum State {
        Uninitialized, Requested, Initialized
    }

    private final Activity _activity;
    private List<Day> _data = new ArrayList<Day>(0);
    private State _state = State.Uninitialized;
    private RequestDataTask _requestDataTask;

    public DataProvider(@NonNull Activity activity) {
        _activity = activity;
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

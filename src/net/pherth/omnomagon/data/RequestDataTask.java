package net.pherth.omnomagon.data;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.List;

public class RequestDataTask extends AsyncTask<Mensa,Void,List<Day>> implements DialogInterface.OnCancelListener {

    private static enum Error {
        networkError, unknownError
    }

    private final WeakReference<Activity> _activity;
    private final WeakReference<DataProvider> _dataProvider;
    private final WeakReference<DataProvider.DataListener> _dataListener;
    private Error _error;

    public RequestDataTask(@NonNull Activity activity, @NonNull DataProvider dataProvider, @NonNull DataProvider.DataListener dataListener) {
        _activity = new WeakReference<Activity>(activity);
        _dataProvider = new WeakReference<DataProvider>(dataProvider);
        _dataListener = new WeakReference<DataProvider.DataListener>(dataListener);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        cancel(true);
    }

    @Nullable
    @Override
    protected List<Day> doInBackground(Mensa... params) {
        List<Day> data = null;
        final Mensa mensa = params[0];
        final Activity activity = _activity.get();
        if (activity != null && !activity.isFinishing()) {
            final ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                try {
                    data = mensa.getMeals();
                } catch (Exception ignore) {
                    _error = Error.unknownError;
                }
            } else {
                _error = Error.networkError;
            }
        }
        return data;
    }

    @Override
    protected void onPostExecute(@Nullable List<Day> days) {
        final DataProvider dataProvider = _dataProvider.get();
        if (dataProvider != null) {
            final DataProvider.DataListener dataListener = _dataListener.get();
            if (dataListener != null) {
                if (_error != null) {
                    if (_error == Error.networkError) {
                        dataListener.onNetworkError();
                    } else {
                        dataListener.onUnknownError();
                    }
                } else {
                    dataProvider.setData(days);
                    dataProvider.setLastUpdateDayOfYear();
                    dataListener.onDataReceived(days);
                }
            }
        }
    }
}

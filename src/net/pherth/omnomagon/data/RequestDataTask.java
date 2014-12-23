package net.pherth.omnomagon.data;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Random;

public class RequestDataTask extends AsyncTask<Void,Void,List<Day>> implements DialogInterface.OnCancelListener {

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
    protected List<Day> doInBackground(Void... params) {
        final Activity activity = _activity.get();
        final String message;
        List<Day> data = null;
        final Random random = new Random();
        final int anInt = random.nextInt(5);
        switch (anInt) {
            case 0:
                _error = Error.networkError;
                message = "Generating Network Error";
                break;
            case 1:
                _error = Error.unknownError;
                message = "Generating Unknown Error";
                break;
            case 2:
                data = DummyDataProvider.generateCorruptedData();
                message = "Generating corrupted data";
                break;
            case 3:
                data = DummyDataProvider.generateDummyData();
                message = "Generating valid data";
                break;
            case 4:
                message = "Generating no data";
                break;
            default:
                message = "woot?";
                break;
        }
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException ignore) { }
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

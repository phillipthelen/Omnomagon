package net.pherth.omnomagon.data;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import net.pherth.omnomagon.data.mensa.DatabaseSnapshot;

import java.lang.ref.WeakReference;
import java.util.List;

public class RestoreDataTask extends AsyncTask<Mensa,Void,List<Day>> implements DialogInterface.OnCancelListener {

    private final WeakReference<Activity> _activity;
    private final WeakReference<DataProvider> _dataProvider;
    private final WeakReference<DataProvider.DataListener> _dataListener;

    public RestoreDataTask(@NonNull Activity activity, @NonNull DataProvider dataProvider, @NonNull DataProvider.DataListener dataListener) {
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
        final DatabaseSnapshot mensa = (DatabaseSnapshot) params[0];
        final Activity activity = _activity.get();
        if (activity != null && !activity.isFinishing()) {
            data = mensa.getMeals();
        }
        return data;
    }

    @Override
    protected void onPostExecute(@Nullable List<Day> days) {
        final DataProvider dataProvider = _dataProvider.get();
        if (dataProvider != null && days != null) {
            final DataProvider.DataListener dataListener = _dataListener.get();
            if (dataListener != null) {
                final boolean initialized = dataProvider.isInitialized();
                if (!initialized) {
                    dataProvider.setData(days);
                    dataListener.onDataReceived(days);
                }
            }
        }
    }
}

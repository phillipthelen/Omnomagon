package net.pherth.omnomagon.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataProvider {

    private enum State {
        Uninitialized, Requested, Initialized
    }

    private final WeakReference<Context> _context;
    private List<Day> _data = new ArrayList<Day>(0);
    private State _state = State.Uninitialized;

    public DataProvider(@NonNull Context context) {
        _context = new WeakReference<Context>(context);
    }

    public void requestData(@NonNull DataListener dataListener) {
        final List<Day> days;
        final Random random = new Random();
        final Context context = _context.get();
        if (random.nextBoolean()) {
            days = DummyDataProvider.generateCorruptedData();
            if (context != null) {
                Toast.makeText(context, "Corrupted Data", Toast.LENGTH_SHORT).show();
            }
        } else {
            days = DummyDataProvider.generateDummyData();
            if (context != null) {
                Toast.makeText(context, "Valid Data", Toast.LENGTH_SHORT).show();
            }
        }
        _data = days;
        _state = State.Initialized;
        dataListener.onDataReceived(days);
    }

    public void setData(@NonNull List<Day> data) {
        MealGroupBasedSorter.sort(data);
        _data = data;
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
        public void onDataReceived(@NonNull List<Day> data);
    }
}

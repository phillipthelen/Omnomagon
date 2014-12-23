package net.pherth.omnomagon.data;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import net.pherth.omnomagon.R;

import java.util.ArrayList;
import java.util.List;

public class MensaFactory {

    private MensaFactory() { }

    public static Mensa getMensa(@Nullable @StringRes Integer cityId, @Nullable @StringRes Integer mensaId) {
        assertIdIsSet(cityId);
        final Mensa mensa;
        if (cityId == R.string.city_berlin) {
            assertIdIsSet(mensaId);
            mensa = new DummyMensa();
        } else if (cityId == R.string.city_ulm) {
            mensa = new DummyMensa();
        } else {
            throw new IllegalArgumentException();
        }
        return mensa;
    }

    private static void assertIdIsSet(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
    }

    private static class DummyMensa implements Mensa {

        @Nullable
        @Override
        public List<Day> getMeals() {
            return new ArrayList<Day>(0);
        }
    }
}

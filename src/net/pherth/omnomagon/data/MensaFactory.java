package net.pherth.omnomagon.data;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import net.pherth.omnomagon.R;
import net.pherth.omnomagon.data.mensa.DatabaseSnapshot;
import net.pherth.omnomagon.data.mensa.MensaBerlin;
import net.pherth.omnomagon.data.mensa.MensaUlm;

public class MensaFactory {

    private final Context _context;
    private final Resources _resources;
    private final Data _data;

    public MensaFactory(@NonNull Context context) {
        _context = context;
        _resources = context.getResources();
        _data = new Data(context);
    }

    @NonNull
    public Mensa getDatabaseSnapshotMensa() {
        return new DatabaseSnapshot(_context, _data);
    }

    @NonNull
    public Mensa getMensa(@Nullable @StringRes Integer cityId, @Nullable @StringRes Integer mensaId) {
        assertIdIsSet(cityId);
        final Mensa mensa;
        if (cityId == R.string.city_berlin) {
            assertIdIsSet(mensaId);
            final String resourceName = _resources.getResourceEntryName(mensaId);
            final String mensaName = resourceName.replaceFirst("be_", "");
            mensa = new MensaBerlin(mensaName, _data);
        } else if (cityId == R.string.city_ulm) {
            mensa = new MensaUlm(_data);
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
}

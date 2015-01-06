package net.pherth.omnomagon.about;

import android.support.annotation.NonNull;

interface VersionListener {
    void onUpdate(@NonNull String tag);
}

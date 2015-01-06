package net.pherth.omnomagon.header;

import android.support.annotation.NonNull;

public class BitmapDimensions {

    private final int _height;
    private final int _width;
    private Float _ratio;

    @NonNull
    public static BitmapDimensions of(int height, int width) {
        return new BitmapDimensions(height, width);
    }

    public BitmapDimensions(int height, int width) {
        _height = height;
        _width = width;
    }

    public int getHeight() {
        return _height;
    }

    public int getWidth() {
        return _width;
    }

    public float getRatio() {
        if (_ratio == null) {
            _ratio = _width != 0 ? ((float) _height) / ((float) _width) : 0;
        }
        return _ratio;
    }
}

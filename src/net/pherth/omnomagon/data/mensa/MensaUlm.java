package net.pherth.omnomagon.data.mensa;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import net.pherth.omnomagon.data.Data;
import net.pherth.omnomagon.data.Day;
import net.pherth.omnomagon.data.Mensa;

import java.io.*;
import java.net.URL;
import java.util.List;

public class MensaUlm implements Mensa {

    private static final String BASE_URL = "http://www.uni-ulm.de/mensaplan/mensaplan.xml";

    private final Data _data;

    public MensaUlm(@NonNull Data data) {
        _data = data;
    }

    @Nullable
    public String getXML() {
        String html = null;
        InputStream byteStream = null;
        Reader characterStream = null;
        BufferedReader bufferedReader = null;
        try {
            final URL url = new URL(BASE_URL);
            byteStream = url.openStream();
            final StringBuilder stringBuilder = new StringBuilder();
            characterStream = new InputStreamReader(byteStream, "utf-8");
            bufferedReader = new BufferedReader(characterStream);
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
            html = stringBuilder.toString();
        } catch (IOException e) {
            Log.e("RSS Handler IO", e.getMessage() + " >> " + e.toString());
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (characterStream != null) {
                    characterStream.close();
                }
                if (byteStream != null) {
                    byteStream.close();
                }
            } catch (IOException ignore) { }
        }
        return html;
    }

    @Nullable
    @Override
    public List<Day> getMeals() {
        return _data.parseData(this);
    }

}

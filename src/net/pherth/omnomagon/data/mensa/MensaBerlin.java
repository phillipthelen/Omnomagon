package net.pherth.omnomagon.data.mensa;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import net.pherth.omnomagon.data.Data;
import net.pherth.omnomagon.data.Day;
import net.pherth.omnomagon.data.Mensa;
import net.pherth.omnomagon.data.RSSHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class MensaBerlin implements Mensa {

    private static final String BASE_URL = "http://www.studentenwerk-berlin.de/speiseplan/rss/%s/woche/lang/1";

    private final String _url;
    private final Data _data;
    private final RSSHandler _rssHandler = new RSSHandler();

    public MensaBerlin(@NonNull String mensaName, @NonNull Data data) {
        _url = String.format(BASE_URL, mensaName);
        _data = data;
    }

    @Nullable
    public String getHTML() {
        String html = null;
        InputStream byteStream = null;
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            final URL url = new URL(_url);
            xr.setContentHandler(_rssHandler);
            byteStream = url.openStream();
            final InputSource inputSource = new InputSource(byteStream);
            xr.parse(inputSource);
            html = _rssHandler.getHTML();
        } catch (IOException e) {
            Log.e("RSS Handler IO", e.getMessage() + " >> " + e.toString());
        } catch (SAXException e) {
            Log.e("RSS Handler SAX", e.toString());
        } catch (ParserConfigurationException e) {
            Log.e("RSS Handler Parser Config", e.toString());
        } finally {
            try {
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

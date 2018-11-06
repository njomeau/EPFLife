package ch.epfl.sweng.zuluzulu.URLTools;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import io.opencensus.common.Function;

public class UrlHandler extends AsyncTask<String, Void, Pair<String, List<String>>> {
    private final static String TAG = "UrlHandler";

    // Function that will be executed onPostExecute
    private Function<Pair<String, List<String>>, Void> listener;

    // The function that will parse the data
    private Function<BufferedReader, List<String>> parser;

    /**
     * Create a new UrlHandler
     *
     * @param listener The callback function that will be use on PostExecute
     */
    public UrlHandler(Function<Pair<String, List<String>>, Void> listener, Function<BufferedReader, List<String>> parser) {
        this.listener = listener;
        this.parser = parser;
    }


    @Override
    protected Pair<String, List<String>> doInBackground(String... urls) {
        if (urls.length > 0) {
            Pair<String, List<String>> result = new Pair<>(urls[0], parseUrl(urls[0]));
            return result;
        }
        else
            return null;
    }


    @Override
    protected void onPostExecute(Pair<String, List<String>> value) {
        if(null != value && value.second != null){
            listener.apply(value);
        } else {
            listener.apply(null);
        }
    }


    /**
     * Connect to the URL, check if the response code is OK (200)
     *
     * @param url Url
     * @return HttpURLConnection or null of response is not OK
     * @throws IOException Throw exception if it cannot connect
     */
    private HttpURLConnection connect(String url) throws IOException {
        // Open url
        URL aURL = openUrl(url);

        // Open connection
        HttpURLConnection UrlConnection = (HttpURLConnection) aURL.openConnection();
        UrlConnection.setRequestProperty("Cookie", "gdpr=accept");

        // Get HTTP response code
        int code = UrlConnection.getResponseCode();

        // Redirect if needed
        if (code == HttpURLConnection.HTTP_MOVED_TEMP
                || code == HttpURLConnection.HTTP_MOVED_PERM) {
            String newUrl = UrlConnection.getHeaderField("Location");
            UrlConnection = (HttpURLConnection) new URL(newUrl).openConnection();
            UrlConnection.setRequestProperty("Cookie", "gdpr=accept");
            code = UrlConnection.getResponseCode();
        }

        if (code != HttpURLConnection.HTTP_OK) {

            Log.d(TAG, "No 200 response code");
            return null;
        }
        UrlConnection.connect();



        return UrlConnection;
    }

    /**
     * Connect to the URL, parse it and return the values found
     *
     * @param url The URL
     * @return T Return object of type T with all the values founded
     */
    private List<String> parseUrl(String url) {

        HttpURLConnection urlConnection;

        try {
            // Connect to the url
            urlConnection = connect(url);
        } catch (IOException e) {
            Log.d(TAG, "Cannot connect to the URL");
            e.printStackTrace();
            return null;
        }

        if (urlConnection == null) {
            Log.d(TAG, "null UrlConnection");
            return null;
        }

        List<String> datas = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            datas = parser.apply(bufferedReader);
            bufferedReader.close();
        } catch (IOException e) {
            Log.d(TAG, "Cannot read the page");
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        return datas;
    }


    /**
     * Open the url
     *
     * @param url url
     * @return a URL object
     * @throws MalformedURLException On bad url
     */
    private URL openUrl(String url) throws MalformedURLException {
        return new URL(url);
    }


}
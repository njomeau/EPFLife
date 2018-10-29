package ch.epfl.sweng.zuluzulu.URLTools;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.opencensus.common.Function;

public class AssociationsUrlHandler<T> extends AsyncTask<String, Void, T> {
    private final static String TAG = "AssociationsUrlHandler";

    // Function that will be executed onPostExecute
    private Function<T, Void> listener;

    // The function that will parse the data
    private Function<BufferedReader, T> parser;

    /**
     * Create a new AssociationUrlHandler
     *
     * @param listener The callback function that will be use on PostExecute
     */
    public AssociationsUrlHandler(Function<T, Void> listener, Function<BufferedReader, T> parser) {
        this.listener = listener;
        this.parser = parser;
    }


    @Override
    protected T doInBackground(String... urls) {
        T result = parseUrl(urls[0]);

        return result;
    }


    @Override
    protected void onPostExecute(T strings) {
        listener.apply(strings);
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
        UrlConnection.connect();

        // Get HTTP response code
        int code = UrlConnection.getResponseCode();


        if (code != 200) {
            // Not OK response
            UrlConnection.disconnect();
            Log.d(TAG, "No 200 response code");
            return null;
        }

        return UrlConnection;
    }

    /**
     * Connect to the URL, parse it and return the values found
     *
     * @param url The URL
     * @return T Return object of type T with all the values founded
     */
    private T parseUrl(String url) {

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


        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
        } catch (IOException e) {
            Log.d(TAG, "Cannot read the page");
            e.printStackTrace();
            urlConnection.disconnect();
            return null;
        }

        T datas = parser.apply(in);

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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
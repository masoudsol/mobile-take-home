package com.ricknmortyawesomeapp.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ricknmortyawesomeapp.modules.models.CharModel;
import com.ricknmortyawesomeapp.modules.models.DataModel;
import com.ricknmortyawesomeapp.modules.repositories.RickRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class APIServices {
    private static final String TAG = "APIServices";

    public interface NetworkListener {
        void onEvent(Object response, Exception error);   //method, which can have parameters
    }

    public interface CompletionListener {
        void onCompletion(Boolean success, Exception error);
    }

    private Context context;
    private RickRepository dataProvider;

    public APIServices(Context context) {
        this.context = context;
        dataProvider = RickRepository.getInstance();
    }

    public void fetchAllEpisodes(final String url, final CompletionListener completionListener) {
        NetworkListener networkListener = new NetworkListener() {
            @Override
            public void onEvent(final Object response, final Exception error) {
                if (error == null) {
                    DataModel episodeModel = new Gson().fromJson((String) response, DataModel.class);
                    dataProvider.appendDataSet(episodeModel.results);

                    String secondUrl = episodeModel.info.next;
                    if (!secondUrl.equals("")) {
                        requestAPI(secondUrl,this);
                    } else {
                        completionListener.onCompletion(true, error);
                    }

                }

            }
        };

        requestAPI(url, networkListener);
    }

    public void fetchAllCharacters(final String url, final CompletionListener completionListener) {
        NetworkListener networkListener = new NetworkListener() {
            @Override
            public void onEvent(Object response, Exception error) {
                if (error == null) {
                    CharModel episodeModel = new Gson().fromJson((String) response, CharModel.class);
                    dataProvider.setCharcters(episodeModel.results);

                    String secondUrl = episodeModel.info.next;
                    if (!secondUrl.equals("")) {
                        requestAPI(secondUrl,this);
                    } else {
                        completionListener.onCompletion(true, error);
                    }
                }
            }
        };

        requestAPI(url, networkListener);
    }

    public void getPhoto(String url, final NetworkListener networkListener){

        // Image was not found in cache; load it from the server
        URL serverURL;
        try {
            serverURL = new URL(url);
        } catch (MalformedURLException exception) {
            throw new RuntimeException(exception);
        }

    }

    private void requestAPI(String url, final NetworkListener networkListener) {
        // Request a string response\
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            networkListener.onEvent(response, null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Error handling
                error.printStackTrace();
                networkListener.onEvent(null, error);

            }
        });

        // Add the request to the queue
        Volley.newRequestQueue(context).add(stringRequest);
    }

    /* Start a thread to send http request to web server use HttpURLConnection object. */
    private void startSendHttpRequestThread(final String reqUrl, final NetworkListener networkListener)
    {
        Thread sendHttpRequestThread = new Thread()
        {
            @Override
            public void run() {
                // Maintain http url connection.
                HttpURLConnection httpConn = null;

                // Read text input stream.
                InputStreamReader isReader = null;

                // Read text into buffer.
                BufferedReader bufReader = null;

                // Save server response text.
                StringBuffer readTextBuf = new StringBuffer();

                try {
                    // Create a URL object use page url.
                    URL url = new URL(reqUrl);

                    // Open http connection to web server.
                    httpConn = (HttpURLConnection)url.openConnection();

                    // Set connection timeout and read timeout value.
                    httpConn.setConnectTimeout(10000);
                    httpConn.setReadTimeout(10000);

                    httpConn.setDoInput(true);
                    httpConn.connect();
                    InputStream input = httpConn.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);

                    networkListener.onEvent(myBitmap,null);

                }catch(IOException ex)
                {
                    Log.e(TAG, ex.getMessage(), ex);
                }finally {
                    httpConn.disconnect();
                }
            }
        };
        // Start the child thread to request web page.
        sendHttpRequestThread.start();
    }
}

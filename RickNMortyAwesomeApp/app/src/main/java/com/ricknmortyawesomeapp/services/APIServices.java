package com.ricknmortyawesomeapp.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.ricknmortyawesomeapp.modules.models.CharModel;
import com.ricknmortyawesomeapp.modules.models.DataModel;
import com.ricknmortyawesomeapp.modules.repositories.RickRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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

    public void fetchPhoto(final String url, final CompletionListener completionListener){

        // Image was not found in cache; load it from the server
        downloadImage(url, new NetworkListener() {
            @Override
            public void onEvent(Object response, Exception error) {
                if (error == null) {
                    Bitmap bitmap = (Bitmap) response;
                    dataProvider.setPhotoThumbnails(url, bitmap);
                    completionListener.onCompletion(true, null);
                } else {
                    completionListener.onCompletion(false,error);
                }
            }
        });

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

                }else {
                    completionListener.onCompletion( false, error);
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
                }else {
                    completionListener.onCompletion( false, error);
                }
            }
        };

        requestAPI(url, networkListener);
    }

    private void requestAPI(final String url, final NetworkListener networkListener) {
        Thread sendHttpRequestThread = new Thread()
        {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                try {
                    URL connectionURL = new URL(url);

                    urlConnection = (HttpURLConnection) connectionURL.openConnection();

                    int responseCode = urlConnection.getResponseCode();

                    if(responseCode == HttpURLConnection.HTTP_OK){
                        final String result = readStream(urlConnection.getInputStream());
                        Log.v("response json", result);
                        Handler mainHandler = new Handler(context.getMainLooper());
                        mainHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                networkListener.onEvent(result,null);
                            }
                        });
                    } else {
                        networkListener.onEvent(null,null);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    networkListener.onEvent(null,e);
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        };

        sendHttpRequestThread.start();

    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    /* Start a thread to send http request to web server use HttpURLConnection object. */
    private void downloadImage(final String reqUrl, final NetworkListener networkListener)
    {
        Thread sendHttpRequestThread = new Thread()
        {
            @Override
            public void run() {
                // Maintain http url connection.
                HttpURLConnection httpConn = null;

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
                    final Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    Handler mainHandler = new Handler(context.getMainLooper());
                    mainHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            networkListener.onEvent(myBitmap,null);
                        }
                    });


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

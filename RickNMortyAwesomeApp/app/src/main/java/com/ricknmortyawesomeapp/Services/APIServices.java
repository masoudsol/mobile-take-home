package com.ricknmortyawesomeapp.Services;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class APIServices {
    public interface NetworkListener{
        void onEvent(Object response, Exception error);   //method, which can have parameters
    }

    private Context context;

    public APIServices(Context context) {
        this.context = context;
    }

    public void fetchAllEpisodes(final NetworkListener networkListener) {
        requestAPI("https://rickandmortyapi.com/api/episode/", new NetworkListener() {
            @Override
            public void onEvent(Object response, Exception error) {
                networkListener.onEvent(response, error);
            }
        });
    }

    public void requestAPI(String url, final NetworkListener networkListener) {
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
}

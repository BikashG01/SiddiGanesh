package com.infobrain.siddiganesh.Networks_files;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by bikas on 7/18/2017.
 */

public class Network_connection {
    private static Network_connection mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;
    private Network_connection(Context context){
        mCtx=context;
        requestQueue=getRequestQueue();
    }

    public RequestQueue getRequestQueue()
    {
        if (requestQueue==null){
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized Network_connection getInstance(Context context){
        if (mInstance==null){
            mInstance= new Network_connection(context);

        }
        return mInstance;
    }
    public <T> void addToRequestque(Request<T> request){
        requestQueue.add(request);
    }
}

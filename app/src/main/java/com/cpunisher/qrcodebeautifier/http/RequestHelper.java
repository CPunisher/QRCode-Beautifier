package com.cpunisher.qrcodebeautifier.http;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestHelper {
    private static RequestHelper instance;
    private static Context ctx;

    private RequestQueue requestQueue;

    private RequestHelper(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized RequestHelper getInstance(Context context) {
        if (instance == null) {
            instance = new RequestHelper(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}

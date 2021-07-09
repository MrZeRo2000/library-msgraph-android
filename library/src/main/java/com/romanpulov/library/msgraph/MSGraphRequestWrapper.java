
package com.romanpulov.library.msgraph;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MSGraphRequestWrapper {
    private static final String TAG = MSGraphRequestWrapper.class.getSimpleName();

    public static class VolleyResult {
        public final JSONObject response;
        public final VolleyError error;

        public VolleyResult(JSONObject response, VolleyError error) {
            this.response = response;
            this.error = error;
        }
    }

    public static interface OnVolleyResultListener {
        void onVolleyRequestCompleted(VolleyResult volleyResult);
    }

    private OnVolleyResultListener mOnVolleyResultListener;

    public void setOnVolleyResultListener(OnVolleyResultListener mOnVolleyResultListener) {
        this.mOnVolleyResultListener = mOnVolleyResultListener;
    }

    // See: https://docs.microsoft.com/en-us/graph/deployments#microsoft-graph-and-graph-explorer-service-root-endpoints
    public static final String MS_GRAPH_ROOT_ENDPOINT = "https://graph.microsoft.com/";

    public static String getErrorResponseBody(VolleyError error) {
        return new String(error.networkResponse.data, StandardCharsets.UTF_8);
    }

    /**
     * Use Volley to make an HTTP request with
     * 1) a given MSGraph resource URL
     * 2) an access token
     * to obtain MSGraph data.
     **/
    public static void callGraphAPIUsingVolley(@NonNull final Context context,
                                               @NonNull final String graphResourceUrl,
                                               @NonNull final String accessToken,
                                               @NonNull final Response.Listener<JSONObject> responseListener,
                                               @NonNull final Response.ErrorListener errorListener) {
        Log.d(TAG, "Starting volley request to graph");

        /* Make sure we have a token to send to graph */
        if (accessToken.length() == 0) {
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject parameters = new JSONObject();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, graphResourceUrl,
                parameters, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        Log.d(TAG, "Adding HTTP GET to Queue, Request: " + request.toString());

        request.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public void callGraphAPIForResult(
            @NonNull final Context context,
            @NonNull final String graphResourceUrl,
            @NonNull final String accessToken
    ) {
        Log.d(TAG, "Starting volley request to graph");

        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject parameters = new JSONObject();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                graphResourceUrl,
                parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (mOnVolleyResultListener != null) {
                            mOnVolleyResultListener.onVolleyRequestCompleted(new VolleyResult(response, null));
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (mOnVolleyResultListener != null) {
                            mOnVolleyResultListener.onVolleyRequestCompleted(new VolleyResult(null, error));
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        Log.d(TAG, "Adding HTTP GET to Queue, Request: " + request.toString());

        request.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
}
package anjelloatoz.com.nodescode.Network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import anjelloatoz.com.nodescode.BuildConfig;

/**
 * Created by Anjelloatoz on 8/15/18.
 */

public class NetworkBroker {
    private static String TAG = "NetworkBroker";

    private static String BASE_URL = "https://api.themoviedb.org";
    private static String API_VERSION = "/3";
    private static String URL = BASE_URL + API_VERSION;
    private static String API_KEY = "&api_key="+ BuildConfig.API_KEY;
    public static String PATH_SEARCH = URL + "/search/movie?"+"query=";
    public static String PATH_DETAILS = URL + "/movie/";

    private static NetworkBroker INSTANCE;
    private final Context context;
    private RequestQueue queue;
    private OkHttpClient httpClient;

    public NetworkBroker(Context context) {
        this.context = context;
        initHttpClient();
        queue = Volley.newRequestQueue(context, new OkHttpStack(httpClient));
    }

    // Starting initialisations

    public static void initInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new NetworkBroker(context);
        }
    }

    public static NetworkBroker getInstance(){
        return INSTANCE;
    }

    private void initHttpClient() {
        httpClient = new OkHttpClient();
        httpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                String contentType = originalResponse.header("Content-Type");
                if(contentType != null && contentType.equals("application/octet-stream")) {
                    return originalResponse.newBuilder().header("Cache-Control",
                            "max-age=" + (60 * 60 * 24 * 30)).build();
                }
                return originalResponse;
            }
        });
        try {
            long SIZE_OF_CACHE = 30 * 1024 * 1024; // 30 MiB
            Cache responseCache = new Cache(context.getCacheDir(), SIZE_OF_CACHE);
            httpClient.setCache(responseCache);
        } catch (Exception e) {
            e.printStackTrace();
        }
        httpClient.setReadTimeout(30, TimeUnit.SECONDS);
        httpClient.setConnectTimeout(30, TimeUnit.SECONDS);
    }

    // End of initialisations

    public void send(int method, String path, Object payload, final CallbackNetwork callback) {
        Gson gson = new GsonBuilder().serializeNulls().create();

        final String json = payload != null ? gson.toJson(payload) : null;

        Log.d(this.getClass().getName(), "Calling API " + method + " " + path + " with:" + json);
        String url = path+API_KEY;
        StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Network broker success response: " + response);
                callback.success(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Network broker error response: " + error);
                Log.e(NetworkBroker.class.getName(), error.toString());
                error.printStackTrace();
                if (error instanceof ServerError && ((ServerError) error).networkResponse.statusCode >= 500)
                    callback.serverFailure();
                else if (error instanceof NetworkError || error instanceof TimeoutError || error instanceof NoConnectionError)
                    callback.networkFailure();
                else
                    callback.failure(error.networkResponse == null ? 0 : error.networkResponse.statusCode, new String((error.networkResponse == null ? "".getBytes() : error.networkResponse.data)));
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return json == null ? "".getBytes() : json.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=" + getParamsEncoding();
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
                String id = networkResponse.headers.get("Location");
                com.android.volley.Response<String> result = com.android.volley.Response.success(id != null ? id : new String(networkResponse.data),
                        HttpHeaderParser.parseCacheHeaders(networkResponse));
                return result;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json");
                map.put("Accept","application/json");

                return map;
            }
        };

        if(method == Request.Method.GET){
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30*1000,//ms
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else{
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }

        queue.add(stringRequest);
    }

    public void sendGet(String path,Object payload, final CallbackNetwork callback) {
        send(Request.Method.GET, path, payload, callback);
    }
}

package anjelloatoz.com.nodescode.Network;

import com.android.volley.toolbox.HurlStack;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Anjelloatoz on 8/15/18.
 */

public class OkHttpStack extends HurlStack {
    private final OkUrlFactory okUrlFactory;

    public OkHttpStack(OkHttpClient httpClient) {
        this(new OkUrlFactory(httpClient));
    }

    public OkHttpStack(OkUrlFactory okUrlFactory) {
        if (okUrlFactory == null) {
            throw new NullPointerException("OkHttpClient must not be null");
        }
        this.okUrlFactory = okUrlFactory;
    }
    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        return okUrlFactory.open(url);
    }
}
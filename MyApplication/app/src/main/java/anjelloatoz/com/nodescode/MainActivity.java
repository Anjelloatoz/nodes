package anjelloatoz.com.nodescode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import anjelloatoz.com.nodescode.Network.GeneralCallback;
import anjelloatoz.com.nodescode.Service.ServiceProxy;

/**
 * Created by Anjelloatoz on 8/15/18.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String query = "English Patient"; //We use a test movie name.
        query = query.replaceAll(" ", "%20");
        String movie_id = "409"; //We use a test id.

        ServiceProxy.getInstance().getMovieDetails(movie_id, new GeneralCallback() {
            @Override
            public void success(Object result) {
                Log.d(this.getClass().getName()+" SUCCESS: ", result.toString());
            }

            @Override
            public void failure(String message) {
                Log.e(this.getClass().getName()+" FAILURE: ", message);
            }
        });
    }
}

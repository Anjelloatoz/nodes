package anjelloatoz.com.nodescode.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import anjelloatoz.com.nodescode.Network.GeneralCallback;
import anjelloatoz.com.nodescode.R;
import anjelloatoz.com.nodescode.Service.ServiceProxy;

/**
 * Created by Anjelloatoz on 8/15/18.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String query = "Man"; //We use a test movie name.
        query = query.replaceAll(" ", "%20");
        String movie_id = "409"; //We use a test id.

        ServiceProxy.getInstance().searchMovies(query, new GeneralCallback() {
            @Override
            public void success(Object result) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }
}

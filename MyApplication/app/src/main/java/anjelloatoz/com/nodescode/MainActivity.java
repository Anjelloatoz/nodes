package anjelloatoz.com.nodescode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import anjelloatoz.com.nodescode.Network.CallbackNetwork;
import anjelloatoz.com.nodescode.Network.NetworkBroker;

/**
 * Created by Anjelloatoz on 8/15/18.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Test code. Network calls will not be in this layer.
        String query = "English Patient";
        query = query.replaceAll(" ", "%20");

        NetworkBroker.getInstance().sendGet(NetworkBroker.PATH_SEARCH+query, query, new CallbackNetwork() {
            @Override
            public void success(Object result) {
                Log.d(this.getClass().getName()+" SUCCESS: ", result.toString());
            }

            @Override
            public void failure(int errorCode, String message) {
                Log.e(this.getClass().getName()+" FAILURE: ", message);
            }

            @Override
            public void networkFailure() {
                Log.e(this.getClass().getName(), "NETWORK FAILURE");
            }

            @Override
            public void serverFailure() {
                Log.e(this.getClass().getName(), "SERVER FAILURE");
            }
        });
        //End of test code
    }
}

package anjelloatoz.com.nodescode;

import android.app.Application;

import anjelloatoz.com.nodescode.Network.NetworkBroker;

/**
 * Created by Anjelloatoz on 8/15/18.
 */

public class NodesCodeTestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkBroker.initInstance(getApplicationContext());
    }
}

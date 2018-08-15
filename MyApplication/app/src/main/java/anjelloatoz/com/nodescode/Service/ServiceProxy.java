package anjelloatoz.com.nodescode.Service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import anjelloatoz.com.nodescode.Network.CallbackNetwork;
import anjelloatoz.com.nodescode.Network.GeneralCallback;
import anjelloatoz.com.nodescode.Network.NetworkBroker;

/**
 * Created by Anjelloatoz on 8/15/18.
 */

public class ServiceProxy {
    private static ServiceProxy INSTANCE;
    Gson gson = new GsonBuilder().serializeNulls().create();

    public static ServiceProxy getInstance(){
        if(INSTANCE == null)
            INSTANCE = new ServiceProxy();
        return INSTANCE;
    }


    public void searchMovies(String query_string, final GeneralCallback callback){
        NetworkBroker.getInstance().sendGet(NetworkBroker.PATH_SEARCH+query_string, null, new CallbackNetwork() {
            @Override
            public void success(Object result) {
                Log.d(this.getClass().getName(), "searchMovie SUCCESS: "+result.toString());
                callback.success(result);
            }

            @Override
            public void failure(int errorCode, String message) {
                Log.e(this.getClass().getName(), "searchMovie FAILURE: "+message);
                callback.failure(message);
            }

            @Override
            public void networkFailure() {
                Log.e(this.getClass().getName(), "searchMovie NETWORK FAILURE");
            }

            @Override
            public void serverFailure() {
                Log.e(this.getClass().getName(), "searchMovie SERVER FAILURE");
            }
        });
    }
}

package anjelloatoz.com.nodescode.Network;

/**
 * Created by Anjelloatoz on 8/15/18.
 */

public interface CallbackNetwork {
    public void success(Object result);
    public void failure(int errorCode, String message);
    public void networkFailure();
    public void serverFailure();
}

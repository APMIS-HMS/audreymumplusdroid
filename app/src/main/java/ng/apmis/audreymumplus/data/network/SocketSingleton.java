package ng.apmis.audreymumplus.data.network;


import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by Thadeus-APMIS on 7/9/2018.
 */

public class SocketSingleton {

    private static SocketSingleton socketInstance;
    private static final Object LOCK = new Object();
    private static Socket mSocket;

    private SocketSingleton() {
        try {
            mSocket = IO.socket("https://audrey-mum.herokuapp.com/");
        } catch (URISyntaxException e) {
            Log.v("socket error", e.getMessage());
        }
        mSocket.connect();
    }


    public static SocketSingleton getInstance() {
        Log.d("socket Found", "Socket Connecting");
        if (socketInstance == null) {
            synchronized (LOCK) {
                socketInstance = new SocketSingleton();
                Log.d("socket starts", "Socket Listening");
            }
        }
        return socketInstance;
    }

    public Socket getSocketInstance () {
        return mSocket;
    }

}

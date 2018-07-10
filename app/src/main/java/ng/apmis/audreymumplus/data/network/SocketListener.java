package ng.apmis.audreymumplus.data.network;


import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by Thadeus-APMIS on 7/9/2018.
 */

public class SocketListener {

    private static SocketListener socketInstance;
    private static final Object LOCK = new Object();
    private static Socket mSocket;

    public SocketListener() {
        try {
            mSocket = IO.socket("https://audrey-mum.herokuapp.com/");
        } catch (URISyntaxException e) {
        }
        mSocket.connect();
    }


    public static SocketListener getInstance() {
        Log.d("Socket starts", "Getting the network data source");
        if (socketInstance == null) {
            synchronized (LOCK) {
                socketInstance = new SocketListener();
                Log.d("Socket starts", "Made new network data source");
            }
        }
        return socketInstance;
    }

    public Socket getSocketInstance () {
        mSocket.connect();
        return mSocket;
    }

}

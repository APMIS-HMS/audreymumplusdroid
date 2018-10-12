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

//            IO.Options options = new IO.Options();
//            options.forceNew = true;
//            options.reconnectionAttempts = Integer.MAX_VALUE;
//            options.timeout = 10000;
//            options.query = "Authorization: " + token;
        String token =  "Bearer 1UjORFOM7Bh6wVE34Ld+PbJ6Lml0uSC68qnAEBeQl1ZLQXdm7OAFIkrkBM7PDx/yCGv2Y8lD93fV" +
                "CLHGqNLjXtkjVIQ5FVbZixT6nHtrrf+mo9H5N97JYsx9oaxB5kwp8DXCYoauJ9FjwmEZLqW/nnmF" +
                "Xs5XYpVLQ0vcI0Ebbih+RYOOXqt59NgQJ6msAy2nXlN4DnKDD928DYYnkuWgRLOmw4G1GcqrYJEy" +
                "OsJXHt9TNR0lb4TxChNRnS9CKSZ8PbbBdNaJPycowDqyGIF2vJS5lsVsgrqcTVw+91YKfBi3qQqf" +
                "3PHiiLM4413fmcb8JMqNW+9uEplcjIFF5+rLq3xDMe4ZPY8ZXfPOjEfK3oZzzyEoEgnR4ZHKv6cN" +
                "gqemPp/uGamfKwA0EdlpeUUl3iX+LvpfMtFSe9nywTSKydPDhBUiqPRcP/DdrYrOnOVj";


            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            opts.query = "Authorization=" + token;

            //mSocket = IO.socket("https://audrey-mum.herokuapp.com/");
            mSocket = IO.socket("https://slimy-rattlesnake-30.localtunnel.me/");


//            mSocket.io().on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
//                @Override
//                public void call(Object... args) {
//                    Transport transport = (Transport)args[0];
//
//                    transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
//                        @Override
//                        public void call(Object... args) {
//
//                            for (Object arg : args)
//                                Log.d("Socket req Log", arg.toString());
//                            @SuppressWarnings("unchecked")
//                            Map<String, List<String>> headers = (Map<String, List<String>>)args[0];
//
//                            // modify request headers
//                            String token =  "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6ImFjY2VzcyJ9.eyJ1c2VySWQiOiI1YTRjNTdiNDQ4ZWFhNzRhMDAwNDA5ODgiLCJpYXQiOjE1MzkyNTQxMzksImV4cCI6MTUzOTM0MDUzOSwiYXVkIjoiaHR0cHM6Ly95b3VyZG9tYWluLmNvbSIsImlzcyI6ImZlYXRoZXJzIiwic3ViIjoiYW5vbnltb3VzIiwianRpIjoiZGU5MWFmMWItNDRjYS00NzM4LWI0Y2YtMTc3OTU2MTJhZDFjIn0.Fsutua2e0kGmR7CkjMe1HfqA168kZuhMzvwZEDMsw8Q";
//
//                            List<String> h = new ArrayList<>();
//                            h.add(token);
//
//                            headers.put("Authorization", h);
//                        }
//                    });
//
//                    transport.on(Transport.EVENT_RESPONSE_HEADERS, new Emitter.Listener() {
//                        @Override
//                        public void call(Object... args) {
//                            for (Object arg : args)
//                                Log.d("Socket res Log", arg.toString());
////                            for (Object arg : args)
////                                Log.d("Socket res Log", arg.toString());
////                            @SuppressWarnings("unchecked")
////                            Map<String, List<String>> headers = (Map<String, List<String>>)args[0];
////                            // access response headers
////                            //String cookie = headers.get("Set-Cookie").get(0);
//                        }
//                    });
//                }
//            });


           // mSocket = IO.socket("https://silly-zebra-80.localtunnel.me");
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

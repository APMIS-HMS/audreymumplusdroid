package ng.apmis.audreymumplus.ui.Chat.chatforum;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Chat.ChatContextFragment;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;

/**
 * Created by Thadeus-APMIS on 6/29/2018.
 */

public class ChatForumFragment extends Fragment {

    @BindView(R.id.forums_list)
    ListView chatParentList;
    ArrayList<ChatForumModel> allForums;
    Socket mSocket;
    ChatForumAdapter chatForumAdapter;
    AppCompatActivity activity;

    {
        try {
            mSocket = IO.socket("https://audrey-mum.herokuapp.com/");
        } catch (URISyntaxException e) {
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_forums, container, false);
        ButterKnife.bind(this, rootView);
        allForums = new ArrayList<>();
        mSocket.connect();

        chatForumAdapter = new ChatForumAdapter(getActivity());
        chatParentList.setAdapter(chatForumAdapter);

        mSocket.emit("getForums", new JSONObject());

        mSocket.on("getForums", args -> {
            JSONObject jsonObject = (JSONObject) args[0];

            try {
                JSONObject job = new JSONObject(jsonObject.toString());
                JSONArray jar = job.getJSONArray("data");

                for (int i = 0 ; i < jar.length(); i++) {
                    JSONObject forumObj = (JSONObject)jar.get(i);
                    ChatForumModel eachChat = new Gson().fromJson(forumObj.toString(), ChatForumModel.class);
                    allForums.add(eachChat);
                }

                activity.runOnUiThread(() -> {
                    chatForumAdapter.setForums(allForums);
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        chatForumAdapter.setForums(allForums);

        chatParentList.setOnItemClickListener((parent, view, position, id) -> {
            ChatForumModel clicked = (ChatForumModel) parent.getItemAtPosition(position);

            Bundle bundle = new Bundle();
            bundle.putString("forumName", clicked.getName());

            ChatContextFragment myObj = new ChatContextFragment();
            myObj.setArguments(bundle);

            activity.getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, myObj)
                    .addToBackStack(null)
                    .commit();
        });


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }


    Emitter.Listener getForums() {

        ArrayList<ChatForumModel> allForums = new ArrayList<>();

        return args -> {
            JSONObject jsonObject = (JSONObject) args[0];
            Log.v("JsonObject", String.valueOf(jsonObject));

            try {
                JSONObject job = new JSONObject(jsonObject.toString());
                JSONArray jar = job.getJSONArray("data");

                for (int i = 0 ; i < jar.length(); i++) {
                    JSONObject forumObj = (JSONObject)jar.get(i);
                    ChatForumModel eachChat = new Gson().fromJson(forumObj.toString(), ChatForumModel.class);
                    allForums.add(eachChat);
                }

                    chatForumAdapter.setForums(allForums);



            } catch (JSONException e) {
                e.printStackTrace();
            }

        };
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity)getActivity()).setActionBarButton(false, "Forums");
        ((DashboardActivity)getActivity()).bottomNavVisibility(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((DashboardActivity)getActivity()).setActionBarButton(false, getString(R.string.app_name));
        ((DashboardActivity)getActivity()).bottomNavVisibility(true);
    }
}

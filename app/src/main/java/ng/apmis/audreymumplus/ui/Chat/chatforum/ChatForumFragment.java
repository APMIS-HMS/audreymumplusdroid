package ng.apmis.audreymumplus.ui.Chat.chatforum;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Chat.ChatContextFragment;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.utils.InjectorUtils;

/**
 * Created by Thadeus-APMIS on 6/29/2018.
 */

public class ChatForumFragment extends Fragment implements ForumAdapter.ClickForumListener {

    @BindView(R.id.forums_list)
    RecyclerView forumRecycler;
    ArrayList<ChatForumModel> allForums;
    Socket mSocket;
    ForumAdapter forumAdapter;
    AppCompatActivity activity;
    @BindView(R.id.empty_view)
    View emptyView;

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

        forumAdapter = new ForumAdapter(getActivity(), this);
        forumRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        forumRecycler.setAdapter(forumAdapter);

        AudreyMumplus.getInstance().diskIO().execute(() -> {
            InjectorUtils.provideRepository(activity).getUpdatedForums().observe(this, chatForums -> {
                Log.v("forum from db", String.valueOf(chatForums));
                if (chatForums != null) {
                        forumAdapter.setForums(allForums);
                } else {
                    emptyView.setVisibility(View.VISIBLE);
                }
            });
        });

        mSocket.emit("getForums", new JSONObject());

        mSocket.on("getForums", args -> {
            JSONObject jsonObject = (JSONObject) args[0];

            try {
                JSONObject job = new JSONObject(jsonObject.toString());
                JSONArray jar = job.getJSONArray("data");

                for (int i = 0 ; i < jar.length(); i++) {
                    JSONObject forumObj = (JSONObject)jar.get(i);
                    ChatForumModel eachForum = new Gson().fromJson(forumObj.toString(), ChatForumModel.class);
                    Log.v("forums", eachForum.toString());
                    allForums.add(eachForum);
                }
                AudreyMumplus.getInstance().diskIO().execute(() -> {
                    InjectorUtils.provideRepository(activity).insertAllForums(allForums);
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
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

    @Override
    public void onForumClick(ChatForumModel chatForums) {
        Bundle bundle = new Bundle();
        bundle.putString("forumName", chatForums.getName());

        ChatContextFragment myObj = new ChatContextFragment();
        myObj.setArguments(bundle);

        activity.getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, myObj)
                .addToBackStack(null)
                .commit();
    }
}

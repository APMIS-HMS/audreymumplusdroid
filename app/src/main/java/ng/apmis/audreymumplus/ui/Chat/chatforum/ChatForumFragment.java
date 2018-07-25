package ng.apmis.audreymumplus.ui.Chat.chatforum;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    ForumViewModel forumViewModel;
    @BindView(R.id.forums_list)
    RecyclerView forumRecycler;
    ArrayList<ChatForumModel> allForums;
    ArrayList<ChatForumModel> dbForums;
    ForumAdapter forumAdapter;
    AppCompatActivity activity;
    @BindView(R.id.empty_view)
    View emptyView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    Socket mSocket;
/*    {
        try {
            mSocket = IO.socket("https://audrey-mum.herokuapp.com/");
        } catch (URISyntaxException e) {
        }
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_forums, container, false);
        ButterKnife.bind(this, rootView);
        allForums = new ArrayList<>();
        dbForums = new ArrayList<>();
        mSocket = InjectorUtils.provideSocketInstance();

        forumAdapter = new ForumAdapter(getActivity(), this);
        forumRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        forumRecycler.setAdapter(forumAdapter);
        forumRecycler.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        ForumFactory forumFactory = InjectorUtils.provideForumFactory(getActivity());
        forumViewModel = ViewModelProviders.of(this, forumFactory).get(ForumViewModel.class);

        mSocket.emit("getForums", new JSONObject());

        forumViewModel.getUpdatedForums().observe(this, forumModelList -> {
            if (forumModelList.size() > 0) {
                dbForums = (ArrayList<ChatForumModel>) forumModelList;
                progressBar.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                forumAdapter.setForums(forumModelList);
            } else {
                //check internet connectivity if true setLoading and emit  get forums
                progressBar.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.VISIBLE);
            }
        });


        mSocket.on("getForums", args -> {
            JSONObject jsonObject = (JSONObject) args[0];

            try {
                JSONObject job = new JSONObject(jsonObject.toString());
                JSONArray jar = job.getJSONArray("data");

                for (int i = 0; i < jar.length(); i++) {
                    JSONObject forumObj = (JSONObject) jar.get(i);
                    ChatForumModel eachForum = new Gson().fromJson(forumObj.toString(), ChatForumModel.class);
                    allForums.add(eachForum);
                }
                Log.v("fors", String.valueOf(allForums));
                   // checkForumChanges(allForums);
                //TODO Move to check forum changes when it works
                 AudreyMumplus.getInstance().diskIO().execute(() -> {
                    InjectorUtils.provideRepository(activity).insertAllForums(allForums);
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        return rootView;
    }

    private void checkForumChanges(ArrayList<ChatForumModel> allForums) {
        Log.v("all forums", String.valueOf(allForums));
        if (allForums != null) {
            for (ChatForumModel x : allForums) {
                for (ChatForumModel y : dbForums) {
                    Log.v("true false", String.valueOf(x.get_id().equals(y.get_id())));
                    if (x.get_id().equals(y.get_id())) {
                        allForums.remove(x);
                    }
                }
            }
        }
        Log.v("remains", allForums.toString());
        /* AudreyMumplus.getInstance().diskIO().execute(() -> {
                    InjectorUtils.provideRepository(activity).insertAllForums(allForums);
                });*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).setActionBarButton(false, "Forums");
        ((DashboardActivity) getActivity()).bottomNavVisibility(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((DashboardActivity) getActivity()).setActionBarButton(false, getString(R.string.app_name));
        ((DashboardActivity) getActivity()).bottomNavVisibility(true);
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

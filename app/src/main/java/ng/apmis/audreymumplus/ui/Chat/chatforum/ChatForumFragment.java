package ng.apmis.audreymumplus.ui.Chat.chatforum;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.data.network.ChatSocketService;
import ng.apmis.audreymumplus.ui.Chat.ChatContextFragment;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.utils.InjectorUtils;
import ng.apmis.audreymumplus.utils.SharedPreferencesManager;

import static ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity.globalPerson;

/**
 * Created by Thadeus-APMIS on 6/29/2018.
 */

public class ChatForumFragment extends Fragment implements ForumAdapter.ClickForumListener {

    ForumViewModel forumViewModel;
    @BindView(R.id.forums_list)
    RecyclerView forumRecycler;

    ForumAdapter forumAdapter;
    AppCompatActivity activity;
    @BindView(R.id.empty_view)
    View emptyView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.search_bar)
    EditText searchBar;
    SharedPreferencesManager sharedPreferencesManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_forums, container, false);
        ButterKnife.bind(this, rootView);

        sharedPreferencesManager = new SharedPreferencesManager(getActivity());


        forumAdapter = new ForumAdapter(getActivity(), this);
        forumRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        forumRecycler.setAdapter(forumAdapter);
        forumRecycler.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        InjectorUtils.provideRepository(getActivity()).getPerson().observe(this, person -> {
            if (person != null) {
                List<String> forums = person.getForums();
                for (String forum : forums) {
                    InjectorUtils.provideJournalNetworkDataSource(getActivity())
                            .getChats(forum);
                }

                getActivity().startService(new Intent(getContext(), ChatSocketService.class).putStringArrayListExtra("forums", new ArrayList<>(forums)));
            }
        });


        ForumFactory forumFactory = InjectorUtils.provideForumFactory(activity);
        forumViewModel = ViewModelProviders.of(this, forumFactory).get(ForumViewModel.class);

        forumViewModel.getUpdatedForums().observe(this, forumModelList -> {
            if (forumModelList.size() > 0) {

                progressBar.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                forumAdapter.setForums(forumModelList);
            } else {
                //check internet connectivity if true setLoading and emit  get forums
                progressBar.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.VISIBLE);
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                forumAdapter.getFilter().filter(s);
            }
        });

        forumRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int lastCompletelyVisibleItemPosition = ((LinearLayoutManager) forumRecycler.getLayoutManager()).findLastCompletelyVisibleItemPosition();

                if (lastCompletelyVisibleItemPosition == forumAdapter.getItemCount() - 2 && forumAdapter.getItemCount() < sharedPreferencesManager.getTotalRoomCountOnserver()) {
                    AudreyMumplus.getInstance().networkIO().execute(() -> InjectorUtils.provideJournalNetworkDataSource(activity).getForums(forumAdapter.getItemCount()));
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.create_forum) {

            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.create_chat_forum, null);

            EditText forumName = dialogView.findViewById(R.id.forum_name);

            new AlertDialog.Builder(getActivity())
                    .setTitle("Create Forum")
                    .setView(dialogView)
                    .setNegativeButton("Cancel", ((dialog, which) -> {
                        dialog.dismiss();
                    }))
                    .setPositiveButton("Create", ((dialog, which) -> {
                        if (!TextUtils.isEmpty(forumName.getText().toString())) {
                            InjectorUtils.provideJournalNetworkDataSource(getActivity()).createForum(forumName.getText().toString().trim(), activity);
                        } else {
                            forumName.setError("Field cannot be empty!!!");
                            Toast.makeText(getActivity(), "Please enter a name", Toast.LENGTH_SHORT).show();
                        }
                    }))
                    .show();

            return true;
        }
        return super.onOptionsItemSelected(item);
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
        if (!globalPerson.getForums().contains(chatForums.getName())) {
            new AlertDialog.Builder(activity)
                    .setTitle("Dear Mum")
                    .setMessage("Request to join forum")
                    .setPositiveButton("Join", (dialog, which) ->
                            InjectorUtils.provideJournalNetworkDataSource(activity).joinForum(activity, globalPerson.getPersonId(), chatForums.getName())
                    )
                    .setNegativeButton("Cancel", (dialog, which) -> {
                    })
                    .show();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("forumName", chatForums.getName());

        ChatContextFragment myObj = new ChatContextFragment();
        myObj.setArguments(bundle);

        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, myObj)
                .addToBackStack(null)
                .commit();
    }
}

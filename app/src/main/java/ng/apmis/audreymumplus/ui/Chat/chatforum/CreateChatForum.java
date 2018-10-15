package ng.apmis.audreymumplus.ui.Chat.chatforum;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.pregnancymodule.pregnancyweeklyprogress.PregnancyWeeklyProgressModel;
import ng.apmis.audreymumplus.utils.InjectorUtils;

/**
 * Created by Thadeus-APMIS on 10/15/2018.
 */

public class CreateChatForum extends DialogFragment {

    @BindView(R.id.forum_name)
    TextInputEditText forumName;
    @BindView(R.id.create_forum)
    Button createForum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.create_chat_forum, container, false);
        ButterKnife.bind(this, rootView);

        createForum.setOnClickListener((view) ->  {
            if (!TextUtils.isEmpty(forumName.getText().toString())) {
                InjectorUtils.provideJournalNetworkDataSource(getActivity()).createForum(forumName.getText().toString().trim(), getDialog());
            } else {
                forumName.setError("Field cannot be empty!!!");
                Toast.makeText(this.getActivity(), "Please enter a name", Toast.LENGTH_SHORT).show();
            }

        });

        return rootView;
    }

}

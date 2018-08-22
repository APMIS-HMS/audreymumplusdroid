package ng.apmis.audreymumplus.ui.Home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.ui.pregnancymodule.pregnancyjournal.AddJournalFragment;

public class HomeFragment extends android.support.v4.app.Fragment implements View.OnClickListener {


    @BindView(R.id.hi_message)
    TextView hiMessage;
    OnfragmentInteractionListener onFragmentInteractionListener;

    @BindView(R.id.action_happy)
    ViewGroup actionHappy;
    @BindView(R.id.action_okay)
    ViewGroup actionOkay;
    @BindView(R.id.action_sick)
    ViewGroup actionSick;
    @BindView(R.id.action_sad)
    ViewGroup actionSad;

    @BindView(R.id.chat_group)
    ViewGroup chatGroup;
    @BindView(R.id.pill_group)
    ViewGroup pillGroup;
    @BindView(R.id.pregnancy_group)
    ViewGroup pregnancyGroup;
    @BindView(R.id.appointment_group)
    ViewGroup appointmentGroup;


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, rootView);

        actionHappy.setOnClickListener(this);
        actionOkay.setOnClickListener(this);
        actionSick.setOnClickListener(this);
        actionSad.setOnClickListener(this);
        pregnancyGroup.setOnClickListener(this);
        appointmentGroup.setOnClickListener(this);
        chatGroup.setOnClickListener(this);
        pillGroup.setOnClickListener(this);

        ((DashboardActivity)getActivity()).getPersonLive().observe(getActivity(), person -> {
            hiMessage.setText(getContext().getString(R.string.hi_message, person != null ? person.getFirstName() : null));
        });

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnfragmentInteractionListener) {
            onFragmentInteractionListener = (OnfragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_happy:
                launchAddJournalFromEmotes("Happy");
                break;
            case R.id.action_okay:
                launchAddJournalFromEmotes("Okay");
                break;
            case R.id.action_sick:
                launchAddJournalFromEmotes("Sick");
                break;
            case R.id.action_sad:
                launchAddJournalFromEmotes("Sad");
                break;
            case R.id.pregnancy_group:
                onFragmentInteractionListener.onGridItemClick("My Pregnancy");
                break;
            case R.id.appointment_group:
                onFragmentInteractionListener.onGridItemClick("My Appointments");
                break;
            case R.id.chat_group:
                onFragmentInteractionListener.onGridItemClick("Chatrooms");
                break;
            case R.id.pill_group:
                onFragmentInteractionListener.onGridItemClick("Pills");
                break;
        }
    }


    /**
     * Launches Add journal fragment when action is pressed from mood emotes on the home screen
     * @param mood String
     */
    void launchAddJournalFromEmotes(String mood) {
        Bundle addBundle = new Bundle();
        addBundle.putString("MOOD", mood);

        Fragment addJournalFragment = new AddJournalFragment();
        addJournalFragment.setArguments(addBundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, addJournalFragment)
                .addToBackStack("ADD_NEW")
                .commit();
    }


    public interface OnfragmentInteractionListener {
        void onGridItemClick (String selectedText);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity)getActivity()).setActionBarButton(false, getString(R.string.app_name));
        ((DashboardActivity)getActivity()).bottomNavVisibility(false);
    }
}

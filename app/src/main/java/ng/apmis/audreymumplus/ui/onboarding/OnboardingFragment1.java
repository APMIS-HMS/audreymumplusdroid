package ng.apmis.audreymumplus.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.LoginActivity;
import ng.apmis.audreymumplus.R;

/**
 * Created by Thadeus-APMIS on 6/25/2018.
 */

public class OnboardingFragment1 extends Fragment {

    @BindView(R.id.next_tv)
    TextView next;
    @BindView(R.id.get_started)
    Button getStarted;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.onboarding1, container, false);
        ButterKnife.bind(this, root);

        getStarted.setOnClickListener((view) -> {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });

        next.setOnClickListener((view) -> ((OnboardingActivity)getActivity()).onNextPressed());

        return root;
    }
}

package ng.apmis.audreymumplus.ui.getaudrey;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;

/**
 * Created by Thadeus-APMIS on 6/29/2018.
 */

public class GetAudreyPageTwo extends Fragment {

    @BindView(R.id.submit_btn)
    Button getAudreyButton;
    @BindView(R.id.spouse_fullname)
    TextInputEditText spouseFullname;
    @BindView(R.id.spouse_phone)
    TextInputEditText spousePhone;
    @BindView(R.id.hospital_name)
    TextInputEditText hostpitalName;
    @BindView(R.id.hostpital_state_et)
    TextInputEditText hospitalState;
    @BindView(R.id.expected_date_of_delivery_et)
    TextInputEditText expectedDateDelivery;
    @BindView(R.id.previous_children)
    TextInputEditText previousChildren ;

    private OnFragmentInteractionListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_two_get_audrey, container, false);
        ButterKnife.bind(this, rootView);

        getAudreyButton.setOnClickListener((view -> {
            if (checkFields()) {
                mListener.clickSignup(view);
            }
        }));


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    boolean checkFields () {
       /* if (spouseFullname.getText().toString().equals("")) {
            spouseFullname.setError("Please fill spouse name");
            return false;
        }
        if (spousePhone.getText().toString().equals("")) {
            spousePhone.setError("Please fill phone number");
            return false;
        }
        if (hostpitalName.getText().toString().equals("")) {
            hostpitalName.setError("Please fill hospital name");
            return false;
        }
        if (hospitalState.getText().toString().equals("")) {
            hospitalState.setError("Please hospital state");
            return false;
        }
        if (expectedDateDelivery.getText().toString().equals("")) {
            expectedDateDelivery.setError("Please fill Expected Date of Delivery");
            return false;
        }
        if (previousChildren.getText().toString().equals("")) {
            previousChildren.setError("Please enter number of previous children");
            return false;
        }

        try {
            ((GetAudreyActivity)getActivity()).registrationData.put("spousefullname", spouseFullname.getText().toString());
            ((GetAudreyActivity)getActivity()).registrationData.put("spousephone", spousePhone.getText().toString());
            ((GetAudreyActivity)getActivity()).registrationData.put("hospitalname", hostpitalName.getText().toString());((GetAudreyActivity)getActivity()).registrationData.put("hospitalstate", hospitalState.getText().toString());
            ((GetAudreyActivity)getActivity()).registrationData.put("edd", expectedDateDelivery.getText().toString());
            ((GetAudreyActivity)getActivity()).registrationData.put("previouschildred", previousChildren.getText().toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
       //TODO uncomment above code for production
        return true;
    }

    public interface OnFragmentInteractionListener {
        void clickSignup(View view);
    }

}

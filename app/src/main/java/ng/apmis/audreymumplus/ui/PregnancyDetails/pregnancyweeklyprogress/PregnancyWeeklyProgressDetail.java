package ng.apmis.audreymumplus.ui.PregnancyDetails.pregnancyweeklyprogress;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import ng.apmis.audreymumplus.R;

/**
 * Created by Thadeus-APMIS on 6/28/2018.
 */

public class PregnancyWeeklyProgressDetail extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_weekly_progress_detail, container, false);

        Bundle mArgs = getArguments();
        PregnancyWeeklyProgressModel detail = mArgs.getParcelable("today");

        TextView detailsTv = rootView.findViewById(R.id.weekly_progress_detail_tv);
        detailsTv.setText(Html.fromHtml(getString(R.string.detail_week_progress, detail.getDay(), detail.getTitle(), detail.getIntro(), detail.getBody())));


        return rootView;
    }
/*

    */
/** The system calls this only when creating the layout in a dialog. *//*

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
*/


}

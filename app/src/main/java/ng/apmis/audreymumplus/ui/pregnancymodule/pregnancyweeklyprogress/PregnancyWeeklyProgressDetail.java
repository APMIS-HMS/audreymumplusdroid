package ng.apmis.audreymumplus.ui.pregnancymodule.pregnancyweeklyprogress;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.data.database.WeeklyProgressData;

/**
 * Created by Thadeus-APMIS on 6/28/2018.
 */

public class PregnancyWeeklyProgressDetail extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_weekly_progress_detail, container, false);

        Bundle mArgs = getArguments();
        WeeklyProgressData detail = (WeeklyProgressData) mArgs.getSerializable("today");

        TextView detailsTv = rootView.findViewById(R.id.weekly_progress_detail_tv);
        detailsTv.setText(Html.fromHtml(getString(R.string.detail_week_progress, detail.getDay()+"", detail.getTitle(), detail.getIntro(), detail.getBody())));


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

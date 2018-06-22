package ng.apmis.audreymumplus.ui.Journal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;

/**
 * Created by Thadeus-APMIS on 5/21/2018.
 */

public class AddJournalFragment extends Fragment {

    @BindView(R.id.crave)
    TextInputEditText cravings;
    @BindView(R.id.save_btn)
    Button saveJournal;
/*
    @BindView(R.id.weight)
    TextInputEditText weight;
*/
    @BindView(R.id.symptoms)
    TextInputEditText symtoms;
    @BindView(R.id.user_babyscan)
    ImageView babyscan;
    @BindView(R.id.pregBel)
    ImageView pregbel;
    @BindView(R.id.note)
    TextInputEditText notes;







    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.new_journal, container, false);
        ButterKnife.bind(this, rootView);




        saveJournal.setOnClickListener((view) -> {
            if (checkFields()) {

                long date = new Date().getTime();
                String crav = cravings.getText().toString();
//                String heavy = weight.getText().toString();
                String symtom = symtoms.getText().toString();
                String noted = notes.getText().toString();

                /*cravings.setText(dailyJournal.getCravings());
                weight.setText(dailyJournal.getWeight());
                symtoms.setText(dailyJournal.getSymptoms());
*/

            }
        });



        return rootView;
    }

    boolean checkFields () {

        if (cravings.getText().toString().equals("")) {
            cravings.setError("required");
            return false;
        }
  /*      if(weight.getText().toString().equals("")){
            weight.setError("required");
            return false;
        }
  */      if(symtoms.getText().toString().equals("")){
            symtoms.setError("required");
            return false;
        }
        if(notes.getText().toString().equals("")){
            notes.setError("required");
            return false;
        }


        return true;
    }

}

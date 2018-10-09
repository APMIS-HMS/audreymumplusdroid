package ng.apmis.audreymumplus.ui.pregnancymodule.journalweekgroup;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.data.AudreyRepository;
import ng.apmis.audreymumplus.ui.pregnancymodule.journalweekgroup.JournalWeekGroup.JournalWeekPlusKickCount;


/**
 * Created by Thadeus-APMIS on 9/25/2018.
 */

public class JournalWeekGroupViewModel extends ViewModel {

    private List<JournalWeekPlusKickCount> mJournalWeekPlusKickCount = new ArrayList<>();
    private MutableLiveData<List<JournalWeekPlusKickCount>> liveJournalWeekPlusKickCount = new MutableLiveData<>();

    JournalWeekGroupViewModel(AudreyRepository audreyRepository, String week) {

        int currentWeek = Integer.parseInt(week.split(" ")[1]);

        AudreyMumplus.getInstance().diskIO().execute(() -> {
            for (int i = 1; i < currentWeek + 1 ; i++) {
                int weekJournalCount = audreyRepository.getJournalCountOnWeek("Week " + i);
                int weekTotalKick = audreyRepository.getTotalWeekKickCount("Week " + i);

                if (weekJournalCount > 0) {
                    mJournalWeekPlusKickCount.add(new JournalWeekPlusKickCount("Week " + i, weekJournalCount, weekTotalKick));
                }
            }
            liveJournalWeekPlusKickCount.postValue(mJournalWeekPlusKickCount);
        });
    }

    LiveData<List<JournalWeekPlusKickCount>> getmJournalWeekPlusKickCount() {
        return liveJournalWeekPlusKickCount;
    }
}

package ng.apmis.audreymumplus.ui.pregnancymodule.journal;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import ng.apmis.audreymumplus.data.AudreyRepository;

/**
 * This class takes care of view model to manage data lifecycle in activity
 */
public class JournalViewModel extends ViewModel {

    private LiveData<List<JournalModel>> mJournalEntry;

    JournalViewModel(AudreyRepository audreyRepository, String week) {
        if (!TextUtils.isEmpty(week)) {
            mJournalEntry = audreyRepository.getJournalByWeek(week);
        } else {
            mJournalEntry = audreyRepository.getAllJournals();
        }
    }

    public LiveData<List<JournalModel>> getSortedJournalEntries() {
        return mJournalEntry;
    }
}

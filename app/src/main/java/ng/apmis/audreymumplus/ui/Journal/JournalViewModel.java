package ng.apmis.audreymumplus.ui.Journal;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import ng.apmis.audreymumplus.data.AudreyRepository;

/**
 * This class takes care of view model to manage data lifecycle in activity
 */
public class JournalViewModel extends ViewModel {

    private LiveData<List<JournalModel>> mJournalEntry;

    JournalViewModel(AudreyRepository audreyRepository) {
        mJournalEntry = audreyRepository.getAllJournals();
    }

    public LiveData<List<JournalModel>> getmJournalEntry() {
        return mJournalEntry;
    }
}

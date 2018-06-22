package ng.apmis.audreymumplus.ui.Journal;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ng.apmis.audreymumplus.data.AudreyRepository;

/**
 * This class is a viewModelProvider class
 */

public class JournalFactory extends ViewModelProvider.NewInstanceFactory {

    AudreyRepository mRepository;

    public JournalFactory(AudreyRepository audreyRepository) {
        this.mRepository = audreyRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new JournalViewModel(mRepository);
    }
}

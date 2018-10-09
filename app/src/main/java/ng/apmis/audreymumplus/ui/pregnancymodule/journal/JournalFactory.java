package ng.apmis.audreymumplus.ui.pregnancymodule.journal;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ng.apmis.audreymumplus.data.AudreyRepository;

/**
 * This class is a viewModelProvider class
 */

public class JournalFactory extends ViewModelProvider.NewInstanceFactory {

    AudreyRepository mRepository;
    String week;

    public JournalFactory(AudreyRepository audreyRepository, String week) {
        this.mRepository = audreyRepository;
        this.week = week;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new JournalViewModel(mRepository, week);
    }
}

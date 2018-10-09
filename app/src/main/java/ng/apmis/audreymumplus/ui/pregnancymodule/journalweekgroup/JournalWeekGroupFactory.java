package ng.apmis.audreymumplus.ui.pregnancymodule.journalweekgroup;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ng.apmis.audreymumplus.data.AudreyRepository;

/**
 * Created by Thadeus-APMIS on 9/25/2018.
 */

public class JournalWeekGroupFactory extends ViewModelProvider.NewInstanceFactory {

    AudreyRepository mRepository;
    String week;

    public JournalWeekGroupFactory(AudreyRepository audreyRepository, String week) {
        this.mRepository = audreyRepository;
        this.week = week;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new JournalWeekGroupViewModel(mRepository, week);
    }
}

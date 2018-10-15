package ng.apmis.audreymumplus.ui.pregnancymodule.pregnancyweeklyprogress;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import ng.apmis.audreymumplus.data.AudreyRepository;

public class PregnancyWeeklyProgressViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AudreyRepository audreyRepository;

    public PregnancyWeeklyProgressViewModelFactory(AudreyRepository audreyRepository) {
        this.audreyRepository = audreyRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new PregnancyWeeklyProgressViewModel(audreyRepository);
    }
}

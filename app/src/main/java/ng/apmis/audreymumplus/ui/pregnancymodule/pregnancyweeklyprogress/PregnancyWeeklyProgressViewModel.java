package ng.apmis.audreymumplus.ui.pregnancymodule.pregnancyweeklyprogress;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import ng.apmis.audreymumplus.data.AudreyRepository;
import ng.apmis.audreymumplus.data.database.WeeklyProgress;
import ng.apmis.audreymumplus.data.database.WeeklyProgressData;

public class PregnancyWeeklyProgressViewModel extends ViewModel {

    private AudreyRepository audreyRepository;
    private LiveData<List<WeeklyProgressData>> data;


    public PregnancyWeeklyProgressViewModel(AudreyRepository audreyRepository) {
        this.audreyRepository = audreyRepository;

        this.data = audreyRepository.getWeeklyProgressData();
    }

    public LiveData<List<WeeklyProgressData>> getData() {
        return data;
    }

    public void bulkInsertWeeklyProgress(List<WeeklyProgressData> progressData){
        audreyRepository.bulkInsertWeeklyProgressData(progressData);
    }

}

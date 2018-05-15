package ng.apmis.audreymumplus.utils;

import android.content.Context;

import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.data.AudreyRepository;
import ng.apmis.audreymumplus.data.database.DailyJournalDatabase;
import ng.apmis.audreymumplus.data.network.MumplusNetworkDataSource;

/**
 * Created by Thadeus-APMIS on 5/15/2018.
 */

public class InjectorUtils {

    public static AudreyRepository provideRepository(Context context) {
        DailyJournalDatabase database = DailyJournalDatabase.getInstance(context.getApplicationContext());
        AudreyMumplus executors = AudreyMumplus.getInstance();
        MumplusNetworkDataSource networkDataSource =
                MumplusNetworkDataSource.getInstance(context.getApplicationContext(), executors);
        return AudreyRepository.getInstance(database.dailyJournalDao(), networkDataSource, executors);
    }

}

package ng.apmis.audreymumplus.utils;

import android.content.Context;

import com.github.nkzawa.socketio.client.Socket;

import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.data.AudreyRepository;
import ng.apmis.audreymumplus.data.database.JournalDatabase;
import ng.apmis.audreymumplus.data.network.MumplusNetworkDataSource;
import ng.apmis.audreymumplus.data.network.SocketListener;
import ng.apmis.audreymumplus.ui.Journal.JournalFactory;

/**
 * Created by Thadeus-APMIS on 5/15/2018.
 */

public class InjectorUtils {

    public static AudreyRepository provideRepository(Context context) {
        JournalDatabase database = JournalDatabase.getInstance(context.getApplicationContext());
        AudreyMumplus executors = AudreyMumplus.getInstance();
        MumplusNetworkDataSource networkDataSource =
                MumplusNetworkDataSource.getInstance(context.getApplicationContext(), executors);
        return AudreyRepository.getInstance(database.dailyJournalDao(), networkDataSource, executors);
    }

    public static JournalFactory provideJournalFactory (Context context) {
        AudreyRepository audreyRepository = provideRepository(context.getApplicationContext());
        return new JournalFactory(audreyRepository);
    }

    public static MumplusNetworkDataSource provideJournalNetworkDataSource (Context context) {
        return MumplusNetworkDataSource.getInstance(context.getApplicationContext(), AudreyMumplus.getInstance());
    }

    public static Socket provideSocketInstance () {
        return SocketListener.getInstance().getSocketInstance();
    }

}

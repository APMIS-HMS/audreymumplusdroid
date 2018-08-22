package ng.apmis.audreymumplus.utils;

import android.content.Context;

import com.github.nkzawa.socketio.client.Socket;

import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.data.AudreyRepository;
import ng.apmis.audreymumplus.data.database.JournalDatabase;
import ng.apmis.audreymumplus.data.network.MumplusNetworkDataSource;
import ng.apmis.audreymumplus.data.network.SocketSingleton;
import ng.apmis.audreymumplus.ui.Chat.ChatFactory;
import ng.apmis.audreymumplus.ui.Chat.chatforum.ForumFactory;
import ng.apmis.audreymumplus.ui.pregnancymodule.pregnancyjournal.JournalFactory;

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

    public static ForumFactory provideForumFactory (Context context) {
        AudreyRepository audreyRepository = provideRepository(context.getApplicationContext());
        return new ForumFactory(audreyRepository);
    }

    public static MumplusNetworkDataSource provideJournalNetworkDataSource (Context context) {
        return MumplusNetworkDataSource.getInstance(context.getApplicationContext(), AudreyMumplus.getInstance());
    }

    public static Socket provideSocketInstance () {
        return SocketSingleton.getInstance().getSocketInstance();
    }

    public static ChatFactory provideChatFactory(Context context, String forumName) {
        AudreyRepository audreyRepository = provideRepository(context.getApplicationContext());
        return new ChatFactory(audreyRepository, forumName);
    }

}

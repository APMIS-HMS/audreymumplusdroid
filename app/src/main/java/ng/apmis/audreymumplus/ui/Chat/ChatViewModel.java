package ng.apmis.audreymumplus.ui.Chat;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import ng.apmis.audreymumplus.data.AudreyRepository;

/**
 * Created by Thadeus-APMIS on 7/25/2018.
 */

public class ChatViewModel extends ViewModel {

    private LiveData<List<ChatContextModel>> mChatEntries;

    ChatViewModel(AudreyRepository audreyRepository, String forumName) {
        mChatEntries = audreyRepository.getUpdatedChats(forumName);
    }

    public LiveData<List<ChatContextModel>> getUpdatedChats() {
        return mChatEntries;
    }

}

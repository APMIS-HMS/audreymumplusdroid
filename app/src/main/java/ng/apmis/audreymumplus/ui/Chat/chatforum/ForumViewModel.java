package ng.apmis.audreymumplus.ui.Chat.chatforum;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import ng.apmis.audreymumplus.data.AudreyRepository;

public class ForumViewModel extends ViewModel {

    private LiveData<List<ChatForumModel>> mForumEntries;

    ForumViewModel(AudreyRepository audreyRepository) {
        mForumEntries = audreyRepository.getUpdatedForums();
    }

    public LiveData<List<ChatForumModel>> getUpdatedForums() {
        return mForumEntries;
    }

}

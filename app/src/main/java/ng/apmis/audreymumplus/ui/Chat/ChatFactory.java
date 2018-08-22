package ng.apmis.audreymumplus.ui.Chat;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ng.apmis.audreymumplus.data.AudreyRepository;
import ng.apmis.audreymumplus.ui.Chat.chatforum.ForumViewModel;

/**
 * Created by Thadeus-APMIS on 7/25/2018.
 */

public class ChatFactory extends ViewModelProvider.NewInstanceFactory {

    AudreyRepository mRepository;
    String forumName;

    public ChatFactory(AudreyRepository audreyRepository, String forumName) {
        this.mRepository = audreyRepository;
        this.forumName = forumName;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ChatViewModel(mRepository, forumName);
    }

}

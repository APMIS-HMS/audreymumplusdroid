package ng.apmis.audreymumplus.ui.Chat.chatforum;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ng.apmis.audreymumplus.data.AudreyRepository;

public class ForumFactory extends ViewModelProvider.NewInstanceFactory {

    AudreyRepository mRepository;

    public ForumFactory(AudreyRepository audreyRepository) {
        this.mRepository = audreyRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ForumViewModel(mRepository);
    }

}

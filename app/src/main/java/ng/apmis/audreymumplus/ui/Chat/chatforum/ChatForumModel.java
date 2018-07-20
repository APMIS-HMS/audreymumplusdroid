package ng.apmis.audreymumplus.ui.Chat.chatforum;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


/**
 * Created by Thadeus-APMIS on 6/29/2018.
 */
@Entity(tableName = "forums")
public class ChatForumModel {

    @PrimaryKey()
    @NonNull
    private String _id;
    private String name, forumMemberCount, forumNewChatsCount;
    private String forumIcon;

    public ChatForumModel(String _id, String name, String forumMemberCount, String forumNewChatsCount, String forumIcon) {
        this._id = _id;
        this.name = name;
        this.forumMemberCount = forumMemberCount;
        this.forumNewChatsCount = forumNewChatsCount;
        this.forumIcon = forumIcon;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getForumMemberCount() {
        return forumMemberCount;
    }

    public String getForumNewChatsCount() {
        return forumNewChatsCount;
    }

    public String getForumIcon() {
        return forumIcon;
    }

    @Override
    public String toString() {
        return "ChatForumModel{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", forumMemberCount='" + forumMemberCount + '\'' +
                ", forumNewChatsCount='" + forumNewChatsCount + '\'' +
                ", forumIcon='" + forumIcon + '\'' +
                '}';
    }
}

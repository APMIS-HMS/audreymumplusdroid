package ng.apmis.audreymumplus.ui.Chat;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "chat")
public class ChatContextModel {

    @PrimaryKey()
    @NonNull
    private String _id;
    private String forumName;
    private String message;
    private String email;
    private String userName;

    public ChatContextModel(String _id, String forumName, String message, String email, String userName) {
        this._id = _id;
        this.forumName = forumName;
        this.message = message;
        this.email = email;
        this.userName = userName;
    }

    @Ignore()
    public ChatContextModel(String forumName, String message, String email, String userName) {
        this.forumName = forumName;
        this.message = message;
        this.email = email;
        this.userName = userName;
    }

    @NonNull
    public String get_id() {
        return _id;
    }

    public String getForumName() {
        return forumName;
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName (String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "ChatContextModel{" +
                "_id='" + _id + '\'' +
                ", forumName='" + forumName + '\'' +
                ", message='" + message + '\'' +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}

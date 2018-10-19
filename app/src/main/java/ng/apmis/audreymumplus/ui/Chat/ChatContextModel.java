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
    private String createdAt;
    private String updatedAt;

    public ChatContextModel(String forumName, String message, String email, String userName) {
        this.forumName = forumName;
        this.message = message;
        this.email = email;
        this.userName = userName;
    }

    @Ignore()
    public ChatContextModel(String forumName, String message, String email, String userName, String createdAt, String updatedAt, String _id) {
        this._id = _id;
        this.forumName = forumName;
        this.message = message;
        this.email = email;
        this.userName = userName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @NonNull
    public String get_id() {
        return _id;
    }

    public void set_id(@NonNull String _id) {
        this._id = _id;
    }

    public String getForumName() {
        return forumName;
    }

    public void setForumName(String forumName) {
        this.forumName = forumName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "ChatContextModel{" +
                "_id='" + _id + '\'' +
                ", forumName='" + forumName + '\'' +
                ", message='" + message + '\'' +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}

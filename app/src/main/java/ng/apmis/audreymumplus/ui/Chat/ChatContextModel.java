package ng.apmis.audreymumplus.ui.Chat;

import org.json.JSONObject;

public class ChatContextModel {


    private String forumName;
    private String message;
    private String email;
    private String userName;

    public ChatContextModel(String forumName, String message, String email, String userName) {
        this.forumName = forumName;
        this.message = message;
        this.email = email;
        this.userName = userName;
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

    public String getUserName () {
        return userName;
    }

    public void setUserName (String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "ChatContextModel{" +
                "forumName='" + forumName + '\'' +
                ", message='" + message + '\'' +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}

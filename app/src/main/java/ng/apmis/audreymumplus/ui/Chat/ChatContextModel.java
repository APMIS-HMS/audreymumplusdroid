package ng.apmis.audreymumplus.ui.Chat;

import org.json.JSONObject;

public class ChatContextModel {


    private String forumName;
    private String message;
    private String email;

    public ChatContextModel(String forumName, String message, String email) {
        this.forumName = forumName;
        this.message = message;
        this.email = email;
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

    @Override
    public String toString() {
        return "ChatContextModel{" +
                "forumName='" + forumName + '\'' +
                ", message='" + message + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

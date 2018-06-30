package ng.apmis.audreymumplus.ui.Chat;

public class ChatContextModel {
    String userId;
    private String username;
    private int imageUri;
    private String content;

    public ChatContextModel(String id, int image, String name, String text){
        userId = id;
        imageUri = image;
        username = name;
        content = text;
    }

    public int getImageUri() {
        return imageUri;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public String getUserId () {
        return userId;
    }
}

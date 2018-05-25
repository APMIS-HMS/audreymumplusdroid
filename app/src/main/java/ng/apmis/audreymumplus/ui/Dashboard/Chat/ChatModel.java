package ng.apmis.audreymumplus.ui.Dashboard.Chat;

public class ChatModel {
    private String username;
    private int imageUri;
    private String content;

    public ChatModel(int image, String name, String text){
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
}

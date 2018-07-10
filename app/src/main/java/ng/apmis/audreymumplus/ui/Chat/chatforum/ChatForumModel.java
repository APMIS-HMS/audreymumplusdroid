package ng.apmis.audreymumplus.ui.Chat.chatforum;

/**
 * Created by Thadeus-APMIS on 6/29/2018.
 */

public class ChatForumModel {

    private String name, forumMemberCount, forumNewChatsCount;
    private int forumIcon;

    public ChatForumModel(int forumIcon, String name, String forumMemberCount, String forumNewChatsCount) {
        this.forumIcon = forumIcon;
        this.name = name;
        this.forumMemberCount = forumMemberCount;
        this.forumNewChatsCount = forumNewChatsCount;
    }

    public int getForumIcon() {
        return forumIcon;
    }

    public void setForumIcon(int forumIcon) {
        this.forumIcon = forumIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getForumMemberCount() {
        return forumMemberCount;
    }

    public void setForumMemberCount(String forumMemberCount) {
        this.forumMemberCount = forumMemberCount;
    }

    public String getForumNewChatsCount() {
        return forumNewChatsCount;
    }

    public void setForumNewChatsCount(String forumNewChatsCount) {
        this.forumNewChatsCount = forumNewChatsCount;
    }

    @Override
    public String toString() {
        return "ChatForumModel{" +
                "name='" + name + '\'' +
                ", forumMemberCount='" + forumMemberCount + '\'' +
                ", forumNewChatsCount='" + forumNewChatsCount + '\'' +
                ", forumIcon=" + forumIcon +
                '}';
    }
}

package ng.apmis.audreymumplus.ui.Chat.chatforum;

/**
 * Created by Thadeus-APMIS on 6/29/2018.
 */

public class ChatForumModel {

    private String forumName, forumMemberCount, forumNewChatsCount;
    private int forumIcon;

    public ChatForumModel(int forumIcon, String forumName, String forumMemberCount, String forumNewChatsCount) {
        this.forumIcon = forumIcon;
        this.forumName = forumName;
        this.forumMemberCount = forumMemberCount;
        this.forumNewChatsCount = forumNewChatsCount;
    }

    public int getForumIcon() {
        return forumIcon;
    }

    public void setForumIcon(int forumIcon) {
        this.forumIcon = forumIcon;
    }

    public String getForumName() {
        return forumName;
    }

    public void setForumName(String forumName) {
        this.forumName = forumName;
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
}

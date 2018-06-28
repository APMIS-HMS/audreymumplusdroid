package ng.apmis.audreymumplus.ui.PregnancyDetails.pregnancyweeklyprogress;

/**
 * Created by Thadeus-APMIS on 6/27/2018.
 */

public class PregnancyWeeklyProgressModel {

    private String day, title, intro, body;

    public PregnancyWeeklyProgressModel(String day, String title, String intro, String body) {
        this.day = day;
        this.title = title;
        this.intro = intro;
        this.body = body;
    }

    public String getDay () {
        return day;
    }

    public String getTitle() {
        return title;
    }

    public String getIntro() {
        return intro;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "PregnancyWeeklyProgressModel{" +
                "day='" + day + '\'' +
                ", title='" + title + '\'' +
                ", intro='" + intro + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}

package ng.apmis.audreymumplus.ui.PregnancyDetails.pregnancyweeklyprogress;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Thadeus-APMIS on 6/27/2018.
 */

public class PregnancyWeeklyProgressModel implements Parcelable{

    private String day, title, intro, body;

    public PregnancyWeeklyProgressModel(String day, String title, String intro, String body) {
        this.day = day;
        this.title = title;
        this.intro = intro;
        this.body = body;
    }

    protected PregnancyWeeklyProgressModel(Parcel in) {
        day = in.readString();
        title = in.readString();
        intro = in.readString();
        body = in.readString();
    }

    public static final Creator<PregnancyWeeklyProgressModel> CREATOR = new Creator<PregnancyWeeklyProgressModel>() {
        @Override
        public PregnancyWeeklyProgressModel createFromParcel(Parcel in) {
            return new PregnancyWeeklyProgressModel(in);
        }

        @Override
        public PregnancyWeeklyProgressModel[] newArray(int size) {
            return new PregnancyWeeklyProgressModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(day);
        parcel.writeString(title);
        parcel.writeString(intro);
        parcel.writeString(body);
    }
}

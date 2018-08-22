package ng.apmis.audreymumplus.ui.pregnancymodule.pregnancyjournal;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "journal")
public class JournalModel implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String mood;
    private String cravings;
    private String weight;
    private String symptoms;
    private String babyScanUri;
    private String pregnancyBellyUri;
    private String babyMovement;
    private long date;
    private String day;
    private String week;

    @Ignore
    public JournalModel(String mood, String cravings, String weight, String symptoms, String babyScanUri, String pregnancyBellyUri, String babyMovement, long date, String day, String week) {
        this.mood = mood;
        this.cravings = cravings;
        this.weight = weight;
        this.symptoms = symptoms;
        this.babyScanUri = babyScanUri;
        this.pregnancyBellyUri = pregnancyBellyUri;
        this.babyMovement = babyMovement;
        this.date = date;
        this.day = day;
        this.week = week;
    }

    public JournalModel(int id, String mood, String cravings, String weight, String symptoms, String babyScanUri, String pregnancyBellyUri, String babyMovement, long date, String day, String week) {
        this.id = id;
        this.mood = mood;
        this.cravings = cravings;
        this.weight = weight;
        this.symptoms = symptoms;
        this.babyScanUri = babyScanUri;
        this.pregnancyBellyUri = pregnancyBellyUri;
        this.babyMovement = babyMovement;
        this.date = date;
        this.day = day;
        this.week = week;
    }


    protected JournalModel(Parcel in) {
        id = in.readInt();
        mood = in.readString();
        cravings = in.readString();
        weight = in.readString();
        symptoms = in.readString();
        babyScanUri = in.readString();
        pregnancyBellyUri = in.readString();
        babyMovement = in.readString();
        date = in.readLong();
        day = in.readString();
        week = in.readString();
    }

    public static final Creator<JournalModel> CREATOR = new Creator<JournalModel>() {
        @Override
        public JournalModel createFromParcel(Parcel in) {
            return new JournalModel(in);
        }

        @Override
        public JournalModel[] newArray(int size) {
            return new JournalModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getMood() {
        return mood;
    }

    public String getCravings() {
        return cravings;
    }

    public String getWeight() {
        return weight;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public String getBabyScanUri() {
        return babyScanUri;
    }

    public String getPregnancyBellyUri() {
        return pregnancyBellyUri;
    }

    public String getBabyMovement() {
        return babyMovement;
    }

    public long getDate() {
        return date;
    }

    public String getDay () {return day; }

    public String getWeek () {
        return week;
    }

    @Override
    public String toString() {
        return "JournalModel{" +
                "id=" + id +
                ", moodEdittext='" + mood + '\'' +
                ", cravings='" + cravings + '\'' +
                ", weight='" + weight + '\'' +
                ", symptoms='" + symptoms + '\'' +
                ", babyScanUri='" + babyScanUri + '\'' +
                ", pregnancyBellyUri='" + pregnancyBellyUri + '\'' +
                ", babyMovement='" + babyMovement + '\'' +
                ", date=" + date + '\'' +
                ", day=" + day + '\'' +
                ", week=" + week +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(mood);
        parcel.writeString(cravings);
        parcel.writeString(weight);
        parcel.writeString(symptoms);
        parcel.writeString(babyScanUri);
        parcel.writeString(pregnancyBellyUri);
        parcel.writeString(babyMovement);
        parcel.writeLong(date);
        parcel.writeString(day);
        parcel.writeString(week);
    }
}

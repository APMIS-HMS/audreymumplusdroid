package ng.apmis.audreymumplus.ui.Journal;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "journal")
public class JournalModel {

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

    @Ignore
    public JournalModel(String mood, String cravings, String weight, String symptoms, String babyScanUri, String pregnancyBellyUri, String babyMovement, long date, String day) {
        this.mood = mood;
        this.cravings = cravings;
        this.weight = weight;
        this.symptoms = symptoms;
        this.babyScanUri = babyScanUri;
        this.pregnancyBellyUri = pregnancyBellyUri;
        this.babyMovement = babyMovement;
        this.date = date;
        this.day = day;
    }

    public JournalModel(int id, String mood, String cravings, String weight, String symptoms, String babyScanUri, String pregnancyBellyUri, String babyMovement, long date, String day) {
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
    }


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
                ", day=" + day +
                '}';
    }
}

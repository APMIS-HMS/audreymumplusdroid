package ng.apmis.audreymumplus.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Defines a day in a week of weekly progress
 */

@Entity(tableName = "weeklyprogressdata", indices = {@Index(value = {"_id"}, unique = true)})
public class WeeklyProgressData implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String _id;
    /**
     *Week in current progress
     */
    private int week;
    private int day;
    private String title;
    private String intro;
    private String body;


    public WeeklyProgressData(int id, String _id, int week, int day, String title, String intro, String body) {
        this.id = id;
        this._id = _id;
        this.week = week;
        this.day = day;
        this.title = title;
        this.intro = intro;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
